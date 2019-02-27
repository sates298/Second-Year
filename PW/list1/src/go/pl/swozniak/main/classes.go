package main

import (
	"math/rand"
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

func (w worker) getAndExecute(tasks chan task, results chan int) {
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
	results <- result
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

	tasks <- task{op:operator, first: rand.Int(), second:rand.Int()}
}

