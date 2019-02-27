package main

import (
	"fmt"
)

func main() {
	workersNo := 5
	var workers [workersNo]worker
	for i:=0; i< workersNo; i++ {
		workers[i] = worker{id: i}
	}
	fmt.Println("something")
}
