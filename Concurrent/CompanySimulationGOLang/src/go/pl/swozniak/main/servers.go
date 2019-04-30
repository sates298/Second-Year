package main

import (
	"../config"
)

//struct representing one task
type Task struct {
	first, second, result int
	op                    string
}

/*
	structures and methods responsible for write and read operations on store
 */
type ReadStoreOp struct {
	resp chan int
}

type WriteStoreOp struct {
	newResult int
	resp      chan bool
}

type GetAllStoreOp struct {
	respStore chan [config.ProductsMaxNo]int
	respCheck chan [config.ProductsMaxNo]bool
}

type StoreServer struct {
	reads  chan *ReadStoreOp
	writes chan *WriteStoreOp
	getAll chan *GetAllStoreOp

	products      [config.ProductsMaxNo]int
	productsCheck [config.ProductsMaxNo]bool
}

func (ss StoreServer) run() {
	var readIterator = 0
	var writeIterator = 0
	for {
		select {
		case read := <-ss.guardReadStore(ss.productsCheck[readIterator]):
			read.resp <- ss.products[readIterator]
			ss.productsCheck[readIterator] = false
			readIterator++
			if readIterator == config.ProductsMaxNo {
				readIterator = 0
			}
		case write := <-ss.guardWriteStore(!ss.productsCheck[writeIterator]):
			ss.products[writeIterator] = write.newResult
			write.resp <- true
			ss.productsCheck[writeIterator] = true
			writeIterator++
			if writeIterator == config.ProductsMaxNo {
				writeIterator = 0
			}
		case get := <-ss.getAll:
			get.respStore <- ss.products
			get.respCheck <- ss.productsCheck
		}
	}
}

func (ss StoreServer) guardReadStore(cond bool) chan *ReadStoreOp {
	if cond {
		return ss.reads
	}
	return nil
}

func (ss StoreServer) guardWriteStore(cond bool) chan *WriteStoreOp {
	if cond {
		return ss.writes
	}
	return nil
}

/*
	structures and methods responsible for write and read operations on list of tasks
 */
type ReadTaskOp struct {
	resp chan *Task
}

type WriteTaskOp struct {
	newTask Task
	resp    chan bool
}

type GetAllTasksOp struct {
	respTask  chan [config.TasksMaxNo]Task
	respCheck chan [config.TasksMaxNo]bool
}

type TasksServer struct {
	reads  chan *ReadTaskOp
	writes chan *WriteTaskOp
	getAll chan *GetAllTasksOp

	tasks      [config.TasksMaxNo]Task
	tasksCheck [config.TasksMaxNo]bool
}

func (ts *TasksServer) run() {
	var readIterator = 0
	var writeIterator = 0
	for {
		select {
		case read := <-ts.guardReadTasks(ts.tasksCheck[readIterator]):
			read.resp <- &ts.tasks[readIterator]
			ts.tasksCheck[readIterator] = false
			readIterator++
			if readIterator == config.TasksMaxNo {
				readIterator = 0
			}
		case write := <-ts.guardWriteTasks(!ts.tasksCheck[writeIterator]):
			ts.tasks[writeIterator] = write.newTask
			write.resp <- true
			ts.tasksCheck[writeIterator] = true
			writeIterator++
			if writeIterator == config.TasksMaxNo {
				writeIterator = 0
			}
		case get := <-ts.getAll:
			get.respTask <- ts.tasks
			get.respCheck <- ts.tasksCheck
		}
	}
}

func (ts TasksServer) guardReadTasks(cond bool) chan *ReadTaskOp {
	if cond {
		return ts.reads
	}
	return nil
}

func (ts TasksServer) guardWriteTasks(cond bool) chan *WriteTaskOp {
	if cond {
		return ts.writes
	}
	return nil
}
