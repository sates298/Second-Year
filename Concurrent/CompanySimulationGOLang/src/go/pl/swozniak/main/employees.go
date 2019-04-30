package main

import (
	"../config"
	"fmt"
	"math/rand"
	"time"
)

//struct representing the boss of the company
type Boss struct{}

//struct representing the worker of the company
type Worker struct {
	id, completed int
	executed      Task
	machines      *Machines
	isPatient     bool
}

//struct representing the client of the company
type Client struct {
	id       int
	products [config.ClientsProductsMaxNo]int
}

/*
	method containing switch to execute task and two communicators with servers
	first communicator is responsible for getting task from list of tasks without deadlock
	second communicator is responsible for setting result - product to store
	parameters are structures of ReadTaskOp to get next task from list, and WriteStoreOp to set new product in there
 */
func (w *Worker) getAndExecute(tasks chan *ReadTaskOp, store chan *WriteStoreOp, service chan *ComplainOp) {

	read := &ReadTaskOp{resp: make(chan Task)}
	tasks <- read

	w.executed = <-read.resp

	w.execute(service)

	write := &WriteStoreOp{
		newResult: w.executed.result,
		resp:      make(chan bool)}
	store <- write

	<-write.resp

	w.completed += 1
	if !PeacefulMode {
		fmt.Println("Worker: {id:", w.id, " task: {", w.executed.first,
			w.executed.second, w.executed.op, "} Result: ", w.executed.result)
	}

}

/*
	method for executing w.executed task and if machine was broken worker send to service complain
 */
func (w *Worker) execute(service chan *ComplainOp) {
	request := &SendToMachineOp{current: &w.executed, resp: make(chan bool)}
	doneNotPatient := false
	done := false
	for !done {
		switch w.executed.op {
		case "*":
			index := rand.Intn(config.MulMachineNo)
			machine := w.machines.mulMachines[index]
			if !w.isPatient {
				for !doneNotPatient {
					select {
					case machine.requests <- request:
						doneNotPatient = <-request.resp
					case <-time.After(500 * time.Millisecond):
						machine = w.machines.mulMachines[rand.Intn(config.MulMachineNo)]
					}
				}
			} else {
				machine.requests <- request
				<-request.resp
			}

			if w.executed.result != 0 {
				done = true
			} else {
				w.sendComplain(index, MULMACHINE, service)
				if !PeacefulMode {
					fmt.Println("Worker: ", w.id, " send request to service about mul machine", machine.id)
				}
			}

		default:
			index := rand.Intn(config.AddMachineNo)
			machine := w.machines.addMachines[index]
			if !w.isPatient {
				for !doneNotPatient {
					select {
					case machine.requests <- request:
						doneNotPatient = <-request.resp
					case <-time.After(500 * time.Millisecond):
						machine = w.machines.addMachines[rand.Intn(config.AddMachineNo)]
					}
				}
			} else {
				machine.requests <- request
				<-request.resp
			}

			if w.executed.result != 0 {
				done = true
			} else {
				w.sendComplain(index, ADDMACHINE, service)
				if !PeacefulMode {
					fmt.Println("Worker: ", w.id, " send request to service about add machine ", machine.id)
				}
			}
		}
	}
}

/*
	method to complaining for broken machines
 */
func (w *Worker) sendComplain(machineIndex int, machineType int, service chan *ComplainOp) {
	complain := &ComplainOp{machineType: machineType, machineIndex: machineIndex}
	service <- complain
}

/*
	method having infinite loop with sleep and randomly activator of getAndExecute() method
	parameters are struct of ReadTaskOp and WriteStoreOp which are used with getAndExecute() method
 */
func (w *Worker) run(tasks chan *ReadTaskOp, store chan *WriteStoreOp, service chan *ComplainOp) {
	for {
		time.Sleep(time.Duration(config.WorkerSpeed * time.Millisecond))
		number := rand.Int() % 100
		if number < config.WorkerSensitive {
			w.getAndExecute(tasks, store, service)
		}
	}
}

/*
	method to create new task
	it chooses randomly an operator (+, - or *) and randomly two integers in range 0 to 99
	after create with this parameters new task
	communicator is needed to wait with adding new task to channel of tasks,
	to avoid deadlock caused locked mutex
	parameter is WriteTaskOp to which we send info about adding new tasks
*/
func (b *Boss) createTask(tasks chan *WriteTaskOp) {
	var operator string
	number := rand.Int() % 3
	switch number {
	case 0:
		operator = "+"
	case 1:
		operator = "-"
	case 2:
		operator = "*"
	}

	write := &WriteTaskOp{
		newTask: Task{op: operator, first: 1 + rand.Int()%100, second: 1 + rand.Int()%100},
		resp:    make(chan bool)}

	for write.newTask.first == write.newTask.second {
		write.newTask.second = 1 + rand.Int()%100
	}

	tasks <- write

	<-write.resp

	if !PeacefulMode {
		fmt.Println("Task: ", write.newTask)
	}
}

/*
	method having infinite loop with sleep and randomly activator of createTask() method
	parameter is a struct of tasksList which is used with createTask() method
 */
func (b *Boss) run(tasks chan *WriteTaskOp) {
	for {
		time.Sleep(time.Duration(config.BossSpeed * time.Millisecond))
		number := rand.Int() % 100
		if number < config.BossSensitive {
			b.createTask(tasks)
		}
	}
}

/*
	method setting quantity of products which will be taken from store by client
	loop is responsible by choosing randomly which products that client will take
 */
func (c *Client) getProduct(store chan *ReadStoreOp) {
	numberOfProducts := 1 + rand.Int()%config.ClientsProductsMaxNo
	var temp [config.ClientsProductsMaxNo]int
	iterator := 0
	read := &ReadStoreOp{resp: make(chan int)}

	for iterator < numberOfProducts {
		if rand.Int()%100 < 50 {
			store <- read
			temp[iterator] = <-read.resp
			iterator++
		}
	}

	c.products = temp
	if iterator > 0 && !PeacefulMode {
		fmt.Println("Client: {", c.id, ", ", c.products[:iterator], "}")
	}
}

/*
	method having infinite loop with sleep and randomly activator of getProduct() method
 */
func (c *Client) run(store chan *ReadStoreOp) {
	for {
		time.Sleep(time.Duration(config.ClientSpeed * time.Millisecond))
		number := rand.Int() % 100
		if number < config.ClientSensitive {
			c.getProduct(store)
		}
	}
}
