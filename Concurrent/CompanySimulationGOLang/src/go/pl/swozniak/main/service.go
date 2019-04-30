package main

import (
	"../config"
	"fmt"
	"time"
)

type ComplainOp struct {
	machineType  int
	machineIndex int
	collisionNo  int
}

type ServiceWorker struct {
	id       int
	isBusy   bool
	machines *Machines
}

type Service struct {
	complains      chan *ComplainOp
	response       chan *ComplainOp
	workers        [config.ServiceWorkersNo]*ServiceWorker
	addMachines    [config.AddMachineNo]bool
	addMachinesLog [config.AddMachineNo]int
	mulMachines    [config.MulMachineNo]bool
	mulMachinesLog [config.MulMachineNo]int
}

func (s *Service) run() {
	for i := 0; i < config.AddMachineNo; i++ {
		s.addMachines[i] = true
		s.addMachinesLog[i] = 0
	}
	for i := 0; i < config.MulMachineNo; i++ {
		s.mulMachines[i] = true
		s.mulMachinesLog[i] = 0
	}

	resp := make(chan *ComplainOp)

	for {
		select {
		case response := <-resp:
			switch response.machineType {
			case ADDMACHINE:
				s.addMachines[response.machineIndex] = true
			case MULMACHINE:
				s.mulMachines[response.machineIndex] = true
			}
		case complain := <-s.checkAvailability():
			inProgress := false
			number := complain.collisionNo
			switch complain.machineType {
			case ADDMACHINE:
				if s.addMachinesLog[complain.machineIndex] < number {
					s.addMachines[complain.machineIndex] = false
					s.addMachinesLog[complain.machineIndex] = number
				} else {
					inProgress = true
				}
			case MULMACHINE:
				if s.mulMachinesLog[complain.machineIndex] < number {
					s.mulMachines[complain.machineIndex] = false
					s.mulMachinesLog[complain.machineIndex] = number
				} else {
					inProgress = true
				}
			}
			if !inProgress {
				for _, w := range s.workers {
					if !w.isBusy {
						w.isBusy = true
						go w.fix(complain, resp)
						break
					}
				}
			}
		}
	}
}

func (s *Service) checkAvailability() chan *ComplainOp {
	for _, sw := range s.workers {
		if !sw.isBusy {
			return s.complains
		}
	}
	return nil
}

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
