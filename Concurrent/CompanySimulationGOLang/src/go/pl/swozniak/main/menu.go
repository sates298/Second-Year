package main

import (
	"../config"
	"fmt"
)

var PeacefulMode = true

/*
	function to communicate with user and to change modes of program
	infinite loop is used to choose in which mode is program
	peaceful mode contains menu and options to show store or actual list of tasks
	loud mode is implemented in another goroutines(getAndExecute(), getProduct(), createTask())
	parameters are structures of taskList and store using to show actual store or list of tasks
 */
func guiRun(tasks chan *GetAllTasksOp, store chan *GetAllStoreOp) {
	var choose int
	var showingTasks [config.TasksMaxNo]Task
	var showingStore [config.ProductsMaxNo]int
	getAllTasks := &GetAllTasksOp{
		respTask: make(chan [config.TasksMaxNo]Task),
		respCheck: make(chan [config.TasksMaxNo]bool)}
	getAllProducts := &GetAllStoreOp{
		respStore: make(chan [config.ProductsMaxNo]int),
		respCheck: make(chan [config.ProductsMaxNo]bool)}
	for {
		if PeacefulMode {
			fmt.Println("Hello in our company!")
			fmt.Println("Choose number which you want to see: ")
			fmt.Println("1. See list of tasks")
			fmt.Println("2. see store status")
			fmt.Println("others + 2*Enter: change mode to loud mode")
			fmt.Scanf("%d", &choose)
			switch choose {
			case 1:
				tasks <- getAllTasks
				tasksArray := <- getAllTasks.respTask
				checks := <- getAllTasks.respCheck
				iterator := 0
				for i:=0; i<config.TasksMaxNo; i++{
					if checks[i] {
						showingTasks[iterator] = tasksArray[i]
						iterator++
					}
				}
				fmt.Println("All Tasks: ", showingTasks[:iterator])

			case 2:
				store <- getAllProducts
				productsArray := <-getAllProducts.respStore
				checks := <-getAllProducts.respCheck
				iterator :=0
				for i:=0; i<config.ProductsMaxNo; i++{
					if checks[i] {
						showingStore[iterator] = productsArray[i]
						iterator++
					}
				}
				fmt.Println("Store: ", showingStore[:iterator])

			default:
				PeacefulMode = false

			}
		} else {
			fmt.Scanf("%d", &choose)
			PeacefulMode = true
		}

	}
}