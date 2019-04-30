package main

import (
	"../config"
	"fmt"
	"math/rand"
	"time"
)

const ADDMACHINE = 0
const MULMACHINE = 1


type SendToMachineOp struct {
	current *Task
	resp    chan bool
}

type Machines struct{
	addMachines   [config.AddMachineNo]*AddingMachine
	mulMachines   [config.MulMachineNo]*MultiplyingMachine
}

type MultiplyingMachine struct {
	id       int
	isBroken bool
	requests chan *SendToMachineOp
	backdoor chan bool
}

type AddingMachine struct {
	id       int
	isBroken bool
	requests chan *SendToMachineOp
	backdoor chan bool
}

func (m AddingMachine) run() {
	for {
		select {
		case <-m.backdoor:
			m.isBroken = false
			if !PeacefulMode {
				fmt.Println("Add Machine: ", m.id, " is fixed now")
			}
		case curr := <-m.requests:
			if !m.isBroken {
				sleep := config.AddMachineSpeed * rand.Intn(100)
				time.Sleep(time.Duration(sleep))
				task := curr.current
				switch task.op {
				case "-":
					task.result = task.first - task.second
				default:
					task.result = task.first + task.second
				}

				if !PeacefulMode{
					fmt.Println("Add Machine: ", m.id, " executing task: {", task.first, " ", task.op, " ",
						task.second, " = ", task.result, "}")
				}

				random := rand.Int() % 100
				if random >= config.AddMachineReliability {
					m.isBroken = true
					if !PeacefulMode{
						fmt.Println("Add Machine: ", m.id, " is broken now")
					}
				}
			}else{
				task := curr.current
				task.result = 0
			}
			curr.resp <- true

		}
	}
}

func (m MultiplyingMachine) run() {
	for {
		select {
		case <-m.backdoor:
			m.isBroken = false
			if !PeacefulMode {
				fmt.Println("Mul Machine: ", m.id, " is fixed now")
			}
		case curr := <-m.requests:
			if !m.isBroken {
				curr := <-m.requests
				sleep := config.MulMachineSpeed * rand.Intn(100)
				time.Sleep(time.Duration(sleep))
				task := curr.current
				task.result = task.first * task.second

				if !PeacefulMode{
					fmt.Println("Mul Machine: ", m.id, " executing task: {", task.first, " * ",
						task.second, " = ", task.result, "}")
				}


				random := rand.Int() % 100
				if random >= config.MulMachineReliability {
					m.isBroken = true
					if !PeacefulMode{
						fmt.Println("Mul Machine: ", m.id, " is broken now")
					}
				}
			}else{
				task := curr.current
				task.result = 0
			}
			curr.resp <- true

		}
	}
}