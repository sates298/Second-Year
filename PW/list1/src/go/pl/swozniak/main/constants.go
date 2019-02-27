package main

import "time"

const workersNo int = 5
const clientsNo int = 3
const tasksMaxNo int = 10
const productsMaxNo int = 40
const bossSpeed time.Duration = 500*time.Millisecond
const workerSpeed time.Duration = 100*time.Millisecond
const clientSpeed time.Duration = time.Second