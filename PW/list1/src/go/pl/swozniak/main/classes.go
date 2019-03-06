package main

import (
	"../config"
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type task struct {
	first, second int
	op            string
}

type boss struct{}

type worker struct {
	id       int
	executed task
}

type client struct {
	id, product int
	store       *store
}

type tasksList struct {
	tasksChan chan task
	mutex     *sync.Mutex
}

type store struct {
	results chan int
	mutex   *sync.Mutex
}

func (w worker) getAndExecute(tasks tasksList, store store) {

	w.executed = <-tasks.tasksChan

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
		if len(store.results) != config.ProductsMaxNo {
			fmt.Println("Worker: ", w, ", Result: ", result)
			store.results <- result
			done = true
		}
		store.mutex.Unlock()
	}

}

func (w worker) run(tasks tasksList, store store) {
	for {
		time.Sleep(config.WorkerSpeed)
		number := rand.Int() % 100
		if number < config.WorkerSensitive {
			w.getAndExecute(tasks, store)
		}
	}
}

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
	fmt.Println("Task: ", newTask)
	tasks.tasksChan <- newTask

}

func (b boss) run(tasks tasksList) {
	for {
		time.Sleep(config.BossSpeed)
		number := rand.Int() % 100
		if number < config.BossSensitive {
			b.createTask(tasks)
		}
	}
}

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
	if iterator > 0 {
		fmt.Println("Client: {", c.id, ", ", temp[:iterator], "}")
	}

}

func (c client) run() {
	for {
		time.Sleep(config.ClientSpeed)
		number := rand.Int() % 100
		if number < config.ClientSensitive {
			c.getProduct()
		}
	}
}
