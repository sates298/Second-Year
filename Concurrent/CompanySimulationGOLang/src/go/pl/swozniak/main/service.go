package main

import (
	"../config"
	"fmt"
	"time"
)

type ComplainOp struct {
	machineType  int
	machineIndex int
}

type ServiceWorker struct {
	id       int
	isBusy   bool
	machines *Machines
}

type Service struct {
	complains   chan *ComplainOp
	response    chan *ComplainOp
	workers     [config.ServiceWorkersNo]*ServiceWorker
	addMachines [config.AddMachineNo]bool
	mulMachines [config.MulMachineNo]bool
}

func (s *Service) run() {
	for i := 0; i < config.AddMachineNo; i++ {
		s.addMachines[i] = true
	}
	for i := 0; i < config.MulMachineNo; i++ {
		s.mulMachines[i] = true
	}

	s.response = make(chan *ComplainOp)

	for {
		select {
		case response := <-s.response:
			switch response.machineType {
			case ADDMACHINE:
				s.addMachines[response.machineIndex] = true
			case MULMACHINE:
				s.mulMachines[response.machineIndex] = true
			}
		case complain := <-s.complains:
			inProgress := false
			switch complain.machineType {
			case ADDMACHINE:
				if s.addMachines[complain.machineIndex] {
					s.addMachines[complain.machineIndex] = false
				} else {
					inProgress = true
				}
			case MULMACHINE:
				if s.mulMachines[complain.machineIndex] {
					s.mulMachines[complain.machineIndex] = false
				} else {
					inProgress = true
				}
			}
			if !inProgress {
				var worker *ServiceWorker
				worker = nil
				iterator := 0
				for worker == nil {
					if iterator >= config.ServiceWorkersNo {
						iterator = 0
					} else {
						if !s.workers[iterator].isBusy {
							worker = s.workers[iterator]
							worker.isBusy = true
						}
						iterator++
					}
				}

				go worker.fix(complain, s.response)
			}
		}
	}
}

func (sw *ServiceWorker) fix(complain *ComplainOp, resp chan *ComplainOp) {
	time.Sleep(config.ServiceWorkerSpeed)
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
