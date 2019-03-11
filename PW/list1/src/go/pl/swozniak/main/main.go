/*	@Author:
 *	@Index:
 */
package main

import (
	"../config"
	"sync"
)

/*
	main function with program's bases and run right methods and function in different goroutines
 */
func main() {

	var workers [config.WorkersNo]Worker
	var clients [config.ClientsNo]Client
	var taskCreator Boss

	done := make(chan bool)
	//tasks := TasksList{make(chan Task, config.TasksMaxNo), &sync.Mutex{}}
	store := Store{make(chan int, config.ProductsMaxNo), &sync.Mutex{}}

	readTasks := make(chan *ReadTaskOp)
	writeTasks := make(chan *WriteTaskOp)
	getAllTasks := make(chan *GetAllTasksOp)
	taskServer := &TasksServer{reads: readTasks, writes:writeTasks ,getAll: getAllTasks}

	go taskServer.run()

	for i:=0; i< config.WorkersNo; i++ {
		workers[i] = Worker{id: i}
	}

	for i:=0; i<config.ClientsNo; i++{
		clients[i] = Client{id: i, store: &store}
	}

	go taskCreator.run(writeTasks)
	for _, w := range workers {
		go w.run(readTasks, store)
	}
	for _, c := range clients{
		go c.run()
	}

	go guiRun(getAllTasks, store)

	<-done
}


