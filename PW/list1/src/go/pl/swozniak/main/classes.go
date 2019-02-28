package main

import (
	"fmt"
	"math/rand"
	"time"
)

type task struct {
	first, second int
	op            string
}

type boss struct {}

type worker struct {
	id       int
	executed task
}

func (w worker) getAndExecute(tasks chan task, store chan int) {
	w.executed  = <- tasks

	result := 0
	switch w.executed.op {
	case "+":
		result = w.executed.first + w.executed.second
	case "-":
		result = w.executed.first - w.executed.second
	case "*":
		result = w.executed.first * w.executed.second
	default:
		result = -1
	}
	fmt.Println("Worker: ", w,", Result: " , result)
	store <- result

}

func (w worker) run(tasks chan task, store chan int){
	for ;; {
		time.Sleep(workerSpeed)
		number := rand.Int()%100
		if number < workerSensitive {
			w.getAndExecute(tasks, store)
		}
	}
}

func (b boss) createTask(tasks chan task) {
	var operator string
	number := rand.Int()%3
	switch number{
	case 0 :
		operator = "+"
	case 1:
		operator = "-"
	default:
		operator = "*"
	}
	newTask := task{op:operator, first: rand.Int()%100, second:rand.Int()%100}
	fmt.Println("Task: ", newTask)
	tasks <- newTask

}

func (b boss) run(tasks chan task){
	for ;;{
		time.Sleep(bossSpeed)
		number := rand.Int()%100
		if number < bossSensitive {
			b.createTask(tasks)
		}
	}
}
