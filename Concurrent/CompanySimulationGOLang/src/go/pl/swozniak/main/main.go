/*	@Author:
 *	@Index:
 */
package main

import (
	"../config"
)

/*
	main function with program's bases and run right methods and function in different goroutines
 */
func main() {

	var workers [config.WorkersNo]*Worker
	var clients [config.ClientsNo]*Client
	var taskCreator Boss

	done := make(chan bool)

	readTasks := make(chan *ReadTaskOp)
	writeTasks := make(chan *WriteTaskOp)
	getAllTasks := make(chan *GetAllTasksOp)
	taskServer := &TasksServer{reads: readTasks, writes: writeTasks, getAll: getAllTasks}

	readStore := make(chan *ReadStoreOp)
	writeProduct := make(chan *WriteStoreOp)
	getAllProducts := make(chan *GetAllStoreOp)
	storeServer := &StoreServer{reads: readStore, writes: writeProduct, getAll: getAllProducts}

	go taskServer.run()
	go storeServer.run()

	for i := 0; i < config.WorkersNo; i++ {
		workers[i] = &Worker{id: i, completed: 0}
	}

	for i := 0; i < config.ClientsNo; i++ {
		clients[i] = &Client{id: i}
	}

	go taskCreator.run(writeTasks)
	for _, w := range workers {
		go w.run(readTasks, writeProduct)
	}
	for _, c := range clients {
		go c.run(readStore)
	}

	go guiRun(getAllTasks, getAllProducts)

	<-done
}
