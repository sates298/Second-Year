package main

type task struct {
	first, second int
	op            string
}

type prezes struct {

}

type worker struct {
	id int
	executed task
}

func (w worker) getAndExecute(tasks []task) int{
	w.executed = tasks[0]
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
	return result
}
