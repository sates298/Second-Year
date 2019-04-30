package main

import (
	"../config"
	"fmt"
)

var PeacefulMode = false

/*
	struct to storage things used in menu

	@tasks - channel to send request and get all tasks from list of tasks and have capabilities to show them
	@store - channel to send request and get all products from store and have capabilities to show them
	@workers - array of pointers on workers to show them
	@machines - pointers to machines structure contains all machines
	@services - array of pointers on service workers to show them
 */
type Menu struct {
	tasks    chan *GetAllTasksOp
	store    chan *GetAllStoreOp
	workers  [config.WorkersNo]*Worker
	machines *Machines
	services [config.ServiceWorkersNo]*ServiceWorker
}

/*
	function to communicate with user and to change modes of program
	infinite loop is used to choose in which mode is program
	peaceful mode contains menu and options as:
		see list of tasks - showing current list of tasks in out of order
		see store status - showing current store in out of order
		see workers - showing each worker with his status
		see machines - showing each machine and its status
		see service workers - showing each service worker with his status
	loud mode is implemented in another goroutines(getAndExecute(), getProduct(), createTask(), ...)
 */
func (m *Menu) guiRun() {
	var choose int
	var showingTasks [config.TasksMaxNo]Task
	var showingStore [config.ProductsMaxNo]int
	getAllTasks := &GetAllTasksOp{
		respTask:  make(chan [config.TasksMaxNo]Task),
		respCheck: make(chan [config.TasksMaxNo]bool)}
	getAllProducts := &GetAllStoreOp{
		respStore: make(chan [config.ProductsMaxNo]int),
		respCheck: make(chan [config.ProductsMaxNo]bool)}
	for {
		if PeacefulMode {
			fmt.Println("Hello in our company!")
			fmt.Println("Choose number which you want to see: ")
			fmt.Println("1. See list of tasks")
			fmt.Println("2. See store status")
			fmt.Println("3. See workers")
			fmt.Println("4. See machines")
			fmt.Println("5. See service workers")
			fmt.Println("others + 2*Enter: change mode to loud mode")
			fmt.Scanf("%d", &choose)
			switch choose {
			case 1:
				m.tasks <- getAllTasks
				tasksArray := <-getAllTasks.respTask
				checks := <-getAllTasks.respCheck
				iterator := 0
				for i := 0; i < config.TasksMaxNo; i++ {
					if checks[i] {
						showingTasks[iterator] = tasksArray[i]
						iterator++
					}
				}
				fmt.Println("All Tasks: ", showingTasks[:iterator])

			case 2:
				m.store <- getAllProducts
				productsArray := <-getAllProducts.respStore
				checks := <-getAllProducts.respCheck
				iterator := 0
				for i := 0; i < config.ProductsMaxNo; i++ {
					if checks[i] {
						showingStore[iterator] = productsArray[i]
						iterator++
					}
				}
				fmt.Println("Store: ", showingStore[:iterator])
			case 3:
				fmt.Print("[")
				for _, w := range m.workers {
					fmt.Print("{id:", w.id, " completed:", w.completed, " isPatient: ", w.isPatient, "} ")
				}
				fmt.Println("]")
			case 4:
				fmt.Print("Add Machines: [")
				for _, am := range m.machines.addMachines {
					fmt.Print("{id:", am.id, " is broken:", am.isBroken, " collisions: ", am.collisionNo, "} ")
				}
				fmt.Println("]")
				fmt.Print("Mul Machines: [")
				for _, mm := range m.machines.mulMachines {
					fmt.Print("{id:", mm.id, " is broken:", mm.isBroken, " collisions: ", mm.collisionNo, "} ")
				}
				fmt.Println("]")
			case 5:
				fmt.Print("[")
				for _, s := range m.services {
					fmt.Print("{id:", s.id, " is busy:", s.isBusy, "} ")
				}
				fmt.Println("]")
			default:
				PeacefulMode = false
			}
		} else {
			fmt.Scanf("%d", &choose)
			PeacefulMode = true
		}

	}
}
