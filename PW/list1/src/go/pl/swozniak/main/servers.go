package main

import (
	"../config"
	"sync"
)

//struct representing the one task
type Task struct {
	first, second int
	op            string
}

//structure containing all products in store as channel and mutex using to lock access to this channel
type Store struct {
	results chan int
	mutex   *sync.Mutex
}
/*
	structures and methods responsible for store write and read operations

 */
type ReadStoreOp struct {
	resp chan int
}

type WriteStoreOp struct {
	newResult int
	resp    chan bool
}

type GetAllStoreOp struct {
	respStore chan [config.ProductsMaxNo]int
	respCheck chan [config.ProductsMaxNo]bool
}

type StoreServer struct {
	reads chan *ReadStoreOp
	writes chan *WriteStoreOp
	getAll chan *GetAllStoreOp
}















type ReadTaskOp struct {
	resp chan Task
}

type WriteTaskOp struct {
	newTask Task
	resp    chan bool
}

type GetAllTasksOp struct {
	respTask chan [config.TasksMaxNo]Task
	respCheck chan [config.TasksMaxNo]bool
}

type TasksServer struct {
	reads  chan *ReadTaskOp
	writes chan *WriteTaskOp
	getAll chan *GetAllTasksOp
}

func (ts TasksServer) run() {
	var tasks [config.TasksMaxNo]Task
	var tasksCheck [config.TasksMaxNo]bool
	var readIterator = 0
	var writeIterator = 0
	for {
		select {
		case read := <- ts.guardReadTasks(tasksCheck[readIterator]):
			read.resp <- tasks[readIterator]
			tasksCheck[readIterator] = false
			readIterator++
			if readIterator == config.TasksMaxNo {
				readIterator = 0
			}
		case write := <- ts.guardWriteTasks(!tasksCheck[writeIterator]):
			tasks[writeIterator] = write.newTask
			write.resp <- true
			tasksCheck[writeIterator] = true
			writeIterator++
			if writeIterator == config.TasksMaxNo {
				writeIterator = 0
			}
		case get := <-ts.getAll:
			get.respTask <- tasks
			get.respCheck <- tasksCheck
		}
	}
}

func (ts TasksServer) guardReadTasks(cond bool) chan *ReadTaskOp{
	if cond {
		return ts.reads
	}
	return nil
}

func (ts TasksServer) guardWriteTasks(cond bool) chan *WriteTaskOp{
	if cond {
		return ts.writes
	}
	return nil
}



