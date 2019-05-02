package main

import (
	"../config"
)

/*
	structure representing task

	@first - first number to calculate
	@second - second number to calculate
	@result - result of calculation
	@op	- operation +, - or *
 */
type Task struct {
	first, second, result int
	op                    string
}

/*
	structure to requests for reading products from server

	@resp - channel to send next product
 */
type ReadStoreOp struct {
	resp chan int
}

/*
	structure to requests for writing new product to server

	@newResult - product to write to server
	@resp - channel of bool to let know new product was saved
 */
type WriteStoreOp struct {
	newResult int
	resp      chan bool
}

/*
	structure to requests for get/show whole store from server

	@respTask - channel of array of products to show
	@respCheck - channel of bool array to check which one is in server yet
 */
type GetAllStoreOp struct {
	respStore chan [config.ProductsMaxNo]int
	respCheck chan [config.ProductsMaxNo]bool
}

/*
	structure to represent store server and its contain products

	@reads - channel to send requests to read next product
	@writes - channel to send request to write new product to store
	@getAll - channel to send request to get whole store to show
	@products - array of products as store
	@productsCheck - array of bool to know if product was read or not
 */
type StoreServer struct {
	reads  chan *ReadStoreOp
	writes chan *WriteStoreOp
	getAll chan *GetAllStoreOp

	products      [config.ProductsMaxNo]int
	productsCheck [config.ProductsMaxNo]bool
}

/*
	method to simulate store server

	@local variables
		readIterator - iterator on products to read next task
		writeIterator - iterator on tasks to write new task

	method contain infinite loop to wait requests
	case read - send to request response correct product and change bool to this product as false (it was read)
	case write - save on the next free field new product got from request and set his bool as true (it wasn't read yet)
	case get - send to request responses both arrays of products
 */
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

/*
	guard to channel of ReadStoreOp
 */
func (ss StoreServer) guardReadStore(cond bool) chan *ReadStoreOp {
	if cond {
		return ss.reads
	}
	return nil
}

/*
	guard to channel of WriteStoreOp
 */
func (ss StoreServer) guardWriteStore(cond bool) chan *WriteStoreOp {
	if cond {
		return ss.writes
	}
	return nil
}

/*
	structure to requests for reading task from server

	@resp - channel to send next task
 */
type ReadTaskOp struct {
	resp chan *Task
}

/*
	structure to requests for writing new tasks to server

	@newTask - task to write to server
	@resp - channel of bool to let know new task was saved
 */
type WriteTaskOp struct {
	newTask Task
	resp    chan bool
}

/*
	structure to requests for get/show all tasks from server

	@respTask - channel of array of tasks to show
	@respCheck - channel of bool array to check which one is in list yet
 */
type GetAllTasksOp struct {
	respTask  chan [config.TasksMaxNo]Task
	respCheck chan [config.TasksMaxNo]bool
}

/*
	structure to represent server of tasks and its contain list of tasks

	@reads - channel to send requests to read next task
	@writes - channel to send request to write new task to list
	@getAll - channel to send request to get all tasks to show
	@tasks - array of tasks to storage all tasks
	@tasksCheck - array of bool to know if task was read or not
 */
type TasksServer struct {
	reads  chan *ReadTaskOp
	writes chan *WriteTaskOp
	getAll chan *GetAllTasksOp

	tasks      [config.TasksMaxNo]Task
	tasksCheck [config.TasksMaxNo]bool
}

/*
	method to simulate server of list of tasks

	@local variables
		readIterator - iterator on tasks to read next task
		writeIterator - iterator on tasks to write new task

	method contain infinite loop to wait requests
	case read - send to request response correct task and change bool to this task as false (it was read)
	case write - save on the next free field new task got from request and set his bool as true (it wasn't read yet)
	case get - send to request responses both arrays of task
 */
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

/*
	guard to channel of ReadTaskOp
 */
func (ts TasksServer) guardReadTasks(cond bool) chan *ReadTaskOp {
	if cond {
		return ts.reads
	}
	return nil
}

/*
	guard to channel of WriteTaskOp
 */
func (ts TasksServer) guardWriteTasks(cond bool) chan *WriteTaskOp {
	if cond {
		return ts.writes
	}
	return nil
}
