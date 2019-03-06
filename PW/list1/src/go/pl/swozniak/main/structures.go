package main

import (
	"sync"
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
