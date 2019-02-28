package main

import (
	"fmt"
)

func main() {
	var workers [workersNo]worker
	var taskCreator boss
	for i:=0; i< workersNo; i++ {
		workers[i] = worker{id: i}
	}
	done := make(chan bool, 1)
	tasks := make(chan task, tasksMaxNo)
	store := make(chan int, productsMaxNo)

	go taskCreator.run(tasks)
	for i:=0; i<workersNo; i++ {
		go workers[i].run(tasks, store)
	}

	<-done

	fmt.Println("something")
}
