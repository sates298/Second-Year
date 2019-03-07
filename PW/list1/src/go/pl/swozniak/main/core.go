package main

import (
	"../config"
	"fmt"
	"math/rand"
	"sync"
	"time"
)

//struct representing the one task
type task struct {
	first, second int
	op            string
}

//struct representing the boss of the company
type boss struct{}

//struct representing the worker of the company
type worker struct {
	id       int
	executed task
}

//struct representing the client of the company
type client struct {
	id, product int
	store       *store
}

//structure containing all tasks as channel and mutex using to lock access to this channel
type tasksList struct {
	tasksChan chan task
	mutex     *sync.Mutex
}

//structure containing all products in store as channel and mutex using to lock access to this channel
type store struct {
	results chan int
	mutex   *sync.Mutex
}

/*
	method containing two loops and switch to execute task
	first loop is responsible for getting task from list of tasks without deadlock
	second loop is responsible for setting result - product to store
	parameters are structures of taskList to get next task from list, and store to set new product in there
 */
func (w worker) getAndExecute(tasks tasksList, store store) {

	wait := true
	for wait {
		tasks.mutex.Lock()
		select {
		case w.executed = <-tasks.tasksChan:
			wait = false
		default:
		}
		/*
		if len(tasks.tasksChan) > 0 {
			w.executed = <-tasks.tasksChan
			wait = false
		}*/
		tasks.mutex.Unlock()
	}

	result := 0
	switch w.executed.op {
	case "+":
		result = w.executed.first + w.executed.second
	case "-":
		result = w.executed.first - w.executed.second
	case "*":
		result = w.executed.first * w.executed.second
	default:
		result = -1
	}

	done := false

	for !done {
		store.mutex.Lock()
		select {
		case store.results <- result:
			if !PeacefulMode {
				fmt.Println("Worker: ", w, ", Result: ", result)
			}
			done = true
		default:
		}
		/*if len(store.results) != config.ProductsMaxNo {
			if !PeacefulMode {
				fmt.Println("Worker: ", w, ", Result: ", result)
			}
			store.results <- result
			done = true
		}*/
		store.mutex.Unlock()
	}
}

/*
	method having infinite loop with sleep and randomly activator of getAndExecute() method
	parameters are struct of tasksList and store which are used with getAndExecute() method
 */
func (w worker) run(tasks tasksList, store store) {
	for {
		time.Sleep(config.WorkerSpeed)
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
	loop is needed to wait with adding new task to channel of tasks,
	to avoid deadlock caused locked mutex
	parameter is taskList to which we adding new tasks
*/
func (b boss) createTask(tasks tasksList) {
	var operator string
	number := rand.Int() % 3
	switch number {
	case 0:
		operator = "+"
	case 1:
		operator = "-"
	default:
		operator = "*"
	}

	newTask := task{op: operator, first: rand.Int() % 100, second: rand.Int() % 100}
	isntDone := true
	for isntDone {
		tasks.mutex.Lock()
		select{
		case tasks.tasksChan <- newTask:
			if !PeacefulMode {
				fmt.Println("Task: ", newTask)
			}
			isntDone = false
		default:
		}
		/*if len(tasks.tasksChan) != config.TasksMaxNo {
			if !PeacefulMode {
				fmt.Println("Task: ", newTask)
			}
			tasks.tasksChan <- newTask
			isntDone = false
		}*/
		tasks.mutex.Unlock()
	}
}

/*
	method having infinite loop with sleep and randomly activator of createTask() method
	parameter is a struct of tasksList which is used with createTask() method
 */
func (b boss) run(tasks tasksList) {
	for {
		time.Sleep(config.BossSpeed)
		number := rand.Int() % 100
		if number < config.BossSensitive {
			b.createTask(tasks)
		}
	}
}

/*
	method setting quantity of products which will be taken from store by client
	infinite loop is responsible by choosing randomly which products that client will take
 */
func (c client) getProduct() {
	numberOfProducts := 1 + rand.Int()%config.ClientsProductsMaxNo
	var temp [config.ClientsProductsMaxNo]int
	iterator := 0
	c.store.mutex.Lock()
	if len(c.store.results) < numberOfProducts {
		numberOfProducts = len(c.store.results)
	}
	for {
		if iterator == numberOfProducts {
			break
		}
		if rand.Int()%2 == 1 {
			c.product = <-c.store.results
			temp[iterator] = c.product
			iterator++
		} else {
			temporary := <-c.store.results
			c.store.results <- temporary
		}

	}
	c.store.mutex.Unlock()
	if iterator > 0 && !PeacefulMode {
		fmt.Println("Client: {", c.id, ", ", temp[:iterator], "}")
	}
}

/*
	method having infinite loop with sleep and randomly activator of getProduct() method
 */
func (c client) run() {
	for {
		time.Sleep(config.ClientSpeed)
		number := rand.Int() % 100
		if number < config.ClientSensitive {
			c.getProduct()
		}
	}
}
