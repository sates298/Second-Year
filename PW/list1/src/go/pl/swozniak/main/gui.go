package main

import (
	"../config"
	"fmt"
)

/*
	function to communicate with user and to change modes of program
	infinite loop is used to choose in which mode is program
	peaceful mode contains menu and options to show store or actual list of tasks
	loud mode is implemented in another goroutines(getAndExecute(), getProduct(), createTask())
	parameters are structures of taskList and store using to show actual store or list of tasks
 */
func guiRun(tasks tasksList, store store) {
	var choose int
	for {
		if PeacefulMode {
			fmt.Println("Hello in our company!")
			fmt.Println("Choose number which you want to see: ")
			fmt.Println("1. See list of tasks")
			fmt.Println("2. see store status")
			fmt.Println("others: change mode to loud mode")
			fmt.Scanf("%d", &choose)
			switch choose {
			case 1:
				tasks.mutex.Lock()
				size := len(tasks.tasksChan)
				var table [config.TasksMaxNo]task
				for i := 0; i < size; i++ {
					table[i] = <-tasks.tasksChan
					tasks.tasksChan <- table[i]
				}
				fmt.Println("All Tasks: ", table[:size])
				tasks.mutex.Unlock()
			case 2:
				store.mutex.Lock()
				size := len(store.results)
				var table [config.ProductsMaxNo]int
				for i := 0; i < size; i++ {
					table[i] = <-store.results
					store.results <- table[i]
				}
				fmt.Println("Store: ", table[:size])
				store.mutex.Unlock()

			default:
				PeacefulMode = false

			}
		} else {
			fmt.Scanf("%d", &choose)
			PeacefulMode = true
		}

	}
}
