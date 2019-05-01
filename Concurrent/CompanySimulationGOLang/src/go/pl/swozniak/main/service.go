package main

import (
	"../config"
	"fmt"
	"time"
)

/*
	structure to represent complain

	@machineType - which type of machine is complained (ADDMACHINe (=0) or MULMACHINE (=1))
	@machineIndex - machine id
	@collisionNo - which collision of that machine which worker complains
 */
type ComplainOp struct {
	machineType  int
	machineIndex int
	collisionNo  int
}

/*
	structure to represent service worker which will fix machines

	@id - worker identity
	@isBusy - is worker is busy with repairing machine or not
	@machines - pointer to machines structure contains all machines
 */
type ServiceWorker struct {
	id       int
	isBusy   bool
	machines *Machines
}

/*
	structure representing service

	@complains - channel to complaining
	@response - channel to communicate with service workers if they finish their job
	@workers - array of pointers to service workers
	@addMachines - bool array checking if service knows about broken machine or not
	@addMachinesFixStatus - bool array contained if service sends free worker to that machine
	@addMachineLogs - array storage numbers of last document collision/break in machine
	@mulMachines - bool array checking if service knows about broken machine or not
	@mulMachinesFixStatus - bool array contained if service sends free worker to that machine
	@mulMachineLogs - array storage numbers of last document collision/break in machine
 */
type Service struct {
	complains chan *ComplainOp
	response  chan *ComplainOp
	workers   [config.ServiceWorkersNo]*ServiceWorker

	addMachines          [config.AddMachineNo]bool
	addMachinesFixStatus [config.AddMachineNo]bool
	addMachinesLog       [config.AddMachineNo]int

	mulMachines          [config.MulMachineNo]bool
	mulMachinesFixStatus [config.MulMachineNo]bool
	mulMachinesLog       [config.MulMachineNo]int
}
/*
	method to simulate service work

	firstly service sets each array as default and make response channel
	next is infinite loop
	there is select with two cases
		first is to catch responses from service workers about fixed machine and
			in this case service changes correctly fields in arrays
		second is to catch complains from company workers
			if complain is about collision which was actually catch service ignore it
			otherwise changes correctly fields in arrays and set in service that machine as broken
	at the end of loop is send available worker to broken adding machine and
		another available worker to multiplying machine
 */
func (s *Service) run() {
	for i := 0; i < config.AddMachineNo; i++ {
		s.addMachines[i] = true
		s.addMachinesFixStatus[i] = false
		s.addMachinesLog[i] = 0
	}
	for i := 0; i < config.MulMachineNo; i++ {
		s.mulMachines[i] = true
		s.mulMachinesFixStatus[i] = false
		s.mulMachinesLog[i] = 0
	}

	s.response = make(chan *ComplainOp)

	for {
		select {
		case response := <-s.response:
			switch response.machineType {
			case ADDMACHINE:
				s.addMachines[response.machineIndex] = true
				s.addMachinesFixStatus[response.machineIndex] = false
			case MULMACHINE:
				s.mulMachines[response.machineIndex] = true
				s.mulMachinesFixStatus[response.machineIndex] = false
			}
		case complain := <-s.complains:
			number := complain.collisionNo
			switch complain.machineType {
			case ADDMACHINE:
				if s.addMachinesLog[complain.machineIndex] < number && s.addMachines[complain.machineIndex] {
					s.addMachines[complain.machineIndex] = false
					s.addMachinesLog[complain.machineIndex] = number
				}
			case MULMACHINE:
				if s.mulMachinesLog[complain.machineIndex] < number && s.mulMachines[complain.machineIndex] {
					s.mulMachines[complain.machineIndex] = false
					s.mulMachinesLog[complain.machineIndex] = number
				}
			}
		}

		worker := s.checkAvailability()

		if worker != nil {
			for i := 0; i < config.AddMachineNo; i++ {
				if !s.addMachines[i] && !s.addMachinesFixStatus[i] {
					worker.isBusy = true
					s.addMachinesFixStatus[i] = true
					go worker.fix(&ComplainOp{machineType: ADDMACHINE, machineIndex: i}, s.response)
					break
				}
			}
		}

		worker = s.checkAvailability()

		if worker != nil {
			for i := 0; i < config.MulMachineNo; i++ {
				if !s.mulMachines[i] && !s.mulMachinesFixStatus[i] {
					worker.isBusy = true
					s.mulMachinesFixStatus[i] = true
					go worker.fix(&ComplainOp{machineType: MULMACHINE, machineIndex: i}, s.response)
					break
				}
			}
		}

	}
}

/*
	method to get next free service worker or if no one is free, return nil
 */
func (s *Service) checkAvailability() *ServiceWorker {
	for _, sw := range s.workers {
		if !sw.isBusy {
			return sw
		}
	}
	return nil
}

/*
	method to fix machine

	@param complain - pointer to structure which contains stats of broken machine
	@param resp - channel to send confirm to service if machine was repaired


 */
func (sw *ServiceWorker) fix(complain *ComplainOp, resp chan *ComplainOp) {
	time.Sleep(config.ServiceWorkerSpeed * time.Millisecond)
	machineType := complain.machineType
	index := complain.machineIndex

	switch machineType {
	case ADDMACHINE:
		machine := sw.machines.addMachines[index]

		machine.backdoor <- true
		if !PeacefulMode {
			fmt.Println("Service Worker {", sw.id, "} fixed add machine: {", machine.id, "}")
		}

	case MULMACHINE:
		machine := sw.machines.mulMachines[index]

		machine.backdoor <- true
		if !PeacefulMode {
			fmt.Println("Service Worker {", sw.id, "} fixed mul machine: {", machine.id, "}")
		}
	}

	resp <- complain

	sw.isBusy = false
}
