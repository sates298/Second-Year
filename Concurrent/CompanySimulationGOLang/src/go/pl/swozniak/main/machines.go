package main

import (
	"../config"
	"fmt"
	"math/rand"
	"time"
)

//constants to distinguish type of machines
const ADDMACHINE = 0
const MULMACHINE = 1

/*
	structure to communicate with machine

	@current - sent task
	@resp - channel to let know that machine finished its job
 */
type SendToMachineOp struct {
	current *Task
	resp    chan bool
}

/*
	structure to storage all machines

	@addMachines - array of pointers to adding machines
	@mulMachines - array of pointers to multiplying machines
 */
type Machines struct {
	addMachines [config.AddMachineNo]*AddingMachine
	mulMachines [config.MulMachineNo]*MultiplyingMachine
}

/*
	structure to represent machine used to multiplication

	@id - identity of machine
	@isBroken - bool representing if machine is broken or not
	@requests - channel to communicate with machine
	@backdoor - channel to fix machine if isBroken
	@collisionNo - number of current collision in machine
 */
type MultiplyingMachine struct {
	id          int
	isBroken    bool
	requests    chan *SendToMachineOp
	backdoor    chan bool
	collisionNo int
}

/*
	structure to represent machine used to addition

	@id - identity of machine
	@isBroken - bool representing if machine is broken or not
	@requests - channel to communicate with machine
	@backdoor - channel to fix machine if isBroken
	@collisionNo - number of current collision in machine
 */
type AddingMachine struct {
	id          int
	isBroken    bool
	requests    chan *SendToMachineOp
	backdoor    chan bool
	collisionNo int
}

/*
	method with infinite loop to simulate adding machine

	in loop is select
	first case is for backdoor channel when machine is broken and service worker is trying to fix it
	second case is for requests from workers
	if machine is broken result of sent task is 0 and is resent,
	otherwise machine sleep random time depended of AddMachineSpeed
	next executes task with right operation
	subsequently machine is became broken with probability equal AddMachineReliability and increases collisionNo
 */
func (m *AddingMachine) run() {
	for {
		select {
		case <-m.backdoor:
			m.isBroken = false

		case curr := <-m.requests:
			sleep := config.AddMachineSpeed * rand.Intn(100)
			time.Sleep(time.Duration(sleep))
			if !m.isBroken {
				task := curr.current
				switch task.op {
				case "-":
					task.result = task.first - task.second
				default:
					task.result = task.first + task.second
				}

				if !PeacefulMode {
					fmt.Println("Add Machine: ", m.id, " executing task: {", task.first, " ", task.op, " ",
						task.second, " = ", task.result, "}")
				}

				random := rand.Int() % 100
				if random >= config.AddMachineReliability {
					m.isBroken = true
					m.collisionNo += 1
					if !PeacefulMode {
						fmt.Println("Add Machine: ", m.id, " is broken now")
					}
				}
			} else {
				task := curr.current
				task.result = 0
			}
			curr.resp <- true

		}
	}
}

/*
	method with infinite loop to simulate multiplying machine

	in loop is select
	first case is for backdoor channel when machine is broken and service worker is trying to fix it
	second case is for requests from workers
	if machine is broken result of sent task is 0 and is resent,
	otherwise machine sleep random time depended of MulMachineSpeed
	next executes task
	subsequently machine is became broken with probability equal MulMachineReliability and increases collisionNo
 */
func (m *MultiplyingMachine) run() {
	for {
		select {
		case <-m.backdoor:
			m.isBroken = false
		case curr := <-m.requests:
			sleep := config.MulMachineSpeed * rand.Intn(100)
			time.Sleep(time.Duration(sleep))

			if !m.isBroken {
				task := curr.current
				task.result = task.first * task.second

				if !PeacefulMode {
					fmt.Println("Mul Machine: ", m.id, " executing task: {", task.first, " * ",
						task.second, " = ", task.result, "}")
				}

				random := rand.Int() % 100
				if random >= config.MulMachineReliability {
					m.isBroken = true
					m.collisionNo += 1
					if !PeacefulMode {
						fmt.Println("Mul Machine: ", m.id, " is broken now")
					}
				}
			} else {
				task := curr.current
				task.result = 0
			}
			curr.resp <- true

		}
	}
}
