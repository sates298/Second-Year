package main

import (
	"../config"
	"math/rand"
	"time"
)

type SendToMachineOp struct {
	current *Task
	resp    chan bool
}

type MultiplyingMachine struct {
	id       int
	requests chan SendToMachineOp
}

type AddingMachine struct {
	id       int
	requests chan SendToMachineOp
}

func (m AddingMachine) run() {
	for {
		curr := <-m.requests
		sleep := config.AddMachineSpeed * rand.Intn(100)
		time.Sleep(time.Duration(sleep))
		task := curr.current
		switch task.op {
		case "-":
			task.result = task.first - task.second
		default:
			task.result = task.first + task.second
		}
		curr.resp <- true
	}
}

func (m MultiplyingMachine) run() {
	for {
		curr := <-m.requests
		sleep := config.MulMachineSpeed * rand.Intn(100)
		time.Sleep(time.Duration(sleep))
		task := curr.current
		task.result = task.first * task.second
		curr.resp <- true

	}
}
