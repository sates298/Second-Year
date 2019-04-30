package main

import (
	"../config"
	"fmt"
	"math/rand"
	"time"
)

/*
	struct representing the boss of the company
	he creates new tasks
 */
type Boss struct{}

/*
	struct representing the worker of the company

	@id - identity of worker
	@completed - number of completed tasks
	@executed - pointer to task which is/was last executed
	@machines - pointer to structure contains arrays of machines
	@isPatient - logical value if worker is patient or not
 */
type Worker struct {
	id, completed int
	executed      *Task
	machines      *Machines
	isPatient     bool
}

/*
	struct representing the client of the company

	@id - identity of client
	@products - array of products which were taken from store in previous meeting in store
 */
type Client struct {
	id       int
	products [config.ClientsProductsMaxNo]int
}

/*
	method to get and execute task from list of tasks and write to store

	@param tasks - channel to sending requests to list of tasks server to read from it next task
	@param store - channel to sending requests to store server to write to it executed task
	@param service - channel to sending complains to service each time when we find broken machine

	firstly worker read task by sending to server (by tasks chan *ReadTaskOp) request
	subsequently he set new executed task
	next he executes this task
	at the end he creates new WriteStoreOp to send request to write new product to store
	finally worker increases his completed rating
 */
func (w *Worker) getAndExecute(tasks chan *ReadTaskOp, store chan *WriteStoreOp, service chan *ComplainOp) {

	read := &ReadTaskOp{resp: make(chan *Task)}
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

	@param service - channel to sending complains to service each time when we find broken machine

	@local variables
		request - structure to send and get task to and from machine
		doneNotPatient - bool used in loop for not patient workers
		done - bool used to find out if executed task was executed appropriate

		index - index of random machine which will be used by worker
		machine - random machine to execute task

	in method is loop while task wont be executed appropriate
	later is switch to distinguish operator (addition or multiplication)
	in every case is at the beginning randomize machine to execute task
	if worker is patient he sends request to machine and waits for response.
	Otherwise is loop while worker find free machine.
	At the and each case worker checks if result is equal 0, if it's he sends complain to service for this machine
	and goes next loop, but if it isn't variable done is equal true and loop is finished
 */
func (w *Worker) execute(service chan *ComplainOp) {
	request := &SendToMachineOp{current: w.executed, resp: make(chan bool)}
	doneNotPatient := false
	done := false

	amount := 0
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
				w.sendComplain(index, MULMACHINE, machine.collisionNo, service)
				if !PeacefulMode && amount < 5 {
					fmt.Println("Worker: ", w.id, " send request to service about mul machine", machine.id)
					amount++
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
				w.sendComplain(index, ADDMACHINE, machine.collisionNo, service)
				if !PeacefulMode && amount < 5 {
					fmt.Println("Worker: ", w.id, " send request to service about add machine ", machine.id)
					amount++
				}
			}
		}
	}

}

/*
	method to complaining for broken machines

	@param machineIndex - index broken machine in array of machines
	@param machineType - type of broken machine (ADDMACHINE (=0) or MULMACHINE (=1))
	@param service - channel to sending complain

	worker creates new ComplainOp by put into all parameters and nex he sends it to service
 */
func (w *Worker) sendComplain(machineIndex int, machineType int, collisionNumber int, service chan *ComplainOp) {
	complain := &ComplainOp{machineType: machineType, machineIndex: machineIndex, collisionNo: collisionNumber}
	service <- complain
}

/*
	method having infinite loop with sleep and randomly activator of getAndExecute() method

	@param tasks - channel to sending requests to list of tasks server to read from it next task
	@param store - channel to sending requests to store server to write to it executed task
	@param service - channel to sending complains to service each time when we find broken machine

	method sleep after each loop amount of WorkerSpeed
	random activator of getProduct method is focused on WorkerSensitivity.
	If random number mod 100 is less than sensitivity, worker execute next task, otherwise worker sleep again (nest loop)
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

	@param tasks - channel to sending requests to list of tasks server to write new task to it
	firstly boss creates random operator (+, - or *), later during creating structure ,to send to list of tasks server,
	he creates new task with random numbers form 1 to 100, and later he randomizes new second number until
	numbers will be different
	finally he sends structure to server
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
	method having infinite loop with sleep and randomly activator of createTask(tasks) method

	@param tasks - channel to sending requests to list of tasks server to write new task to it

	method sleep after each loop amount of BossSpeed
	random activator of getProduct method is focused on BossSensitivity.
	If random number mod 100 is less than sensitivity, boss create new task, otherwise boss sleep again (nest loop)
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
	method to get products from store

	@param store - channel to sending requests to store server to get product from store

	firstly client randomizes how many products he want to get (range from 0 to ClientsProductMaxNo)
	next he sends drawn a number of times the same request and he saves each product what he gets
	requests are sending not every at the same time, but with 50% chance in loop
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
	method having infinite loop with sleep and randomly activator of getProduct(store) method

	@param store - channel to sending requests to store server to get product from store

	method sleep after each loop amount of ClientSpeed
	random activator of getProduct method is focused on ClientSensitivity.
	If random number mod 100 is less than sensitivity, client gets new products, otherwise client sleep again (nest loop)
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
