package main

import (
	"../config"
	"math/rand"
	"time"
)

type SendToMachineOp struct{
	current *Task
	resp chan bool
}

type MultiplicatingMachine struct{
	id int
	requests chan SendToMachineOp
}

type AddingMachine struct{
	id int
	requests chan SendToMachineOp
}

func (m AddingMachine) compute(task *Task, resp chan bool){
	task.result = task.first + task.second
	resp <- true
}

func (m AddingMachine) run() {
	for{
		select {
		case curr := <-m.requests:
			sleep := config.AddMachineSpeed*rand.Intn(100)
			time.Sleep(time.Duration(sleep))
			m.compute(curr.current, curr.resp)
		}
	}
}
func (m MultiplicatingMachine) compute(task *Task, resp chan bool){
	task.result = task.first * task.second
	resp <- true
}
func (m MultiplicatingMachine) run() {
	for{
		select {
		case curr := <-m.requests:
			sleep := config.MulMachineSpeed*rand.Intn(100)
			time.Sleep(time.Duration(sleep))
			m.compute(curr.current, curr.resp)
		}
	}
}