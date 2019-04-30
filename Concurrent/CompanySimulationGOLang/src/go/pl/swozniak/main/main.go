/*	@Author:
 *	@Index:
 */
package main

import (
	"../config"
	"math/rand"
)

/*
	main function with program's bases and run right methods and function in different goroutines
 */
func main() {

	var workers [config.WorkersNo]*Worker
	var clients [config.ClientsNo]*Client
	var taskCreator Boss
	var serviceWorkers [config.ServiceWorkersNo]*ServiceWorker

	var addMachines [config.AddMachineNo]*AddingMachine
	var mulMachines [config.MulMachineNo]*MultiplyingMachine
	addChannel := make(chan *SendToMachineOp)
	mulChannel := make(chan *SendToMachineOp)

	done := make(chan bool)

	readTasks := make(chan *ReadTaskOp)
	writeTasks := make(chan *WriteTaskOp)
	getAllTasks := make(chan *GetAllTasksOp)
	taskServer := &TasksServer{reads: readTasks, writes: writeTasks, getAll: getAllTasks}

	readStore := make(chan *ReadStoreOp)
	writeProduct := make(chan *WriteStoreOp)
	getAllProducts := make(chan *GetAllStoreOp)
	storeServer := &StoreServer{reads: readStore, writes: writeProduct, getAll: getAllProducts}

	go taskServer.run()
	go storeServer.run()

	for i := 0; i < config.AddMachineNo; i++ {
		addMachines[i] = &AddingMachine{
			id:          i,
			requests:    addChannel,
			backdoor:    make(chan bool),
			isBroken:    false,
			collisionNo: 0}
	}

	for i := 0; i < config.MulMachineNo; i++ {
		mulMachines[i] = &MultiplyingMachine{
			id:          i,
			requests:    mulChannel,
			backdoor:    make(chan bool),
			isBroken:    false,
			collisionNo: 0}

	}

	machines := &Machines{mulMachines: mulMachines, addMachines: addMachines}

	for i := 0; i < config.ServiceWorkersNo; i++ {
		serviceWorkers[i] = &ServiceWorker{id: i, isBusy: false, machines: machines}
	}

	complainChannel := make(chan *ComplainOp)
	service := &Service{complains: complainChannel, workers: serviceWorkers}

	go service.run()

	for i := 0; i < config.WorkersNo; i++ {
		r := rand.Int() % 2
		var isPatient bool
		if r == 0 {
			isPatient = false
		} else {
			isPatient = true
		}
		workers[i] = &Worker{
			id:        i,
			machines:  machines,
			completed: 0,
			isPatient: isPatient}
	}

	for i := 0; i < config.ClientsNo; i++ {
		clients[i] = &Client{id: i}
	}

	go taskCreator.run(writeTasks)
	for _, m := range addMachines {
		go m.run()
	}
	for _, m := range mulMachines {
		go m.run()
	}
	for _, w := range workers {
		go w.run(readTasks, writeProduct, complainChannel)
	}
	for _, c := range clients {
		go c.run(readStore)
	}

	menu := &Menu{
		tasks:    getAllTasks,
		store:    getAllProducts,
		workers:  workers,
		services: serviceWorkers,
		machines: machines}

	go menu.guiRun()

	<-done
}
