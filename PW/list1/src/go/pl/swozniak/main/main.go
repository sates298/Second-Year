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

	tasks := make(chan task, tasksMaxNo)
	store := make(chan int, productsMaxNo)




	fmt.Println("something")
}
