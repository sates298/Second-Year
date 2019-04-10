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
	addMachines   *[config.AddMachineNo]AddingMachine
	mulMachines   *[config.MulMachineNo]MultiplyingMachine
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
func (w *Worker) getAndExecute(tasks chan *ReadTaskOp, store chan *WriteStoreOp) {

	read := &ReadTaskOp{resp: make(chan Task)}
	tasks <- read

	w.executed = <-read.resp
	request := SendToMachineOp{current: &w.executed, resp: make(chan bool)}
	done := false
	switch w.executed.op {
	case "*":
		machine := w.mulMachines[rand.Intn(config.MulMachineNo)]
		if !w.isPatient {
			for !done {
				select {
				case machine.requests <- request:
					done = <-request.resp
				case <-time.After(500 * time.Millisecond):
					machine = w.mulMachines[rand.Intn(config.MulMachineNo)]
				}
			}
		} else {
			machine.requests <- request
			<-request.resp
		}
	default:
		machine := w.addMachines[rand.Intn(config.AddMachineNo)]
		if !w.isPatient {
			for !done {
				select {
				case machine.requests <- request:
					done = <-request.resp
				case <-time.After(500 * time.Millisecond):
					machine = w.addMachines[rand.Intn(config.AddMachineNo)]
				}
			}
		} else {
			machine.requests <- request
			<-request.resp
		}
	}

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
	method having infinite loop with sleep and randomly activator of getAndExecute() method
	parameters are struct of ReadTaskOp and WriteStoreOp which are used with getAndExecute() method
 */
func (w *Worker) run(tasks chan *ReadTaskOp, store chan *WriteStoreOp) {
	for {
		time.Sleep(time.Duration(config.WorkerSpeed * time.Millisecond))
		number := rand.Int() % 100
		if number < config.WorkerSensitive {
			w.getAndExecute(tasks, store)
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
		newTask: Task{op: operator, first: rand.Int() % 100, second: rand.Int() % 100},
		resp:    make(chan bool)}

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
