/*	@Author:
 *	@Index:
 */
package main

import (
	"../config"
	"sync"
)
var PeacefulMode = true

/*
	main function with program's bases and run right methods and function in different goroutines
 */
func main() {

	var workers [config.WorkersNo]worker
	var clients [config.ClientsNo]client
	var taskCreator boss

	done := make(chan bool)
	tasks := tasksList{make(chan task, config.TasksMaxNo), &sync.Mutex{}}
	store := store{make(chan int, config.ProductsMaxNo), &sync.Mutex{}}

	for i:=0; i< config.WorkersNo; i++ {
		workers[i] = worker{id: i}
	}

	for i:=0; i<config.ClientsNo; i++{
		clients[i] = client{id: i, store: &store}
	}

	go taskCreator.run(tasks)
	for _, w := range workers {
		go w.run(tasks, store)
	}
	for _, c := range clients{
		go c.run()
	}

	go guiRun(tasks, store)

	<-done
}