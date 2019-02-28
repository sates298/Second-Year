package main

import "time"

const workersNo int = 2
const clientsNo int = 3
const tasksMaxNo int = 10
const productsMaxNo int = 40
const bossSpeed time.Duration = 1000*time.Millisecond
const bossSensitive int = 70
const workerSpeed time.Duration = 1000*time.Millisecond
const workerSensitive int = 30
const clientSpeed time.Duration = time.Second
const clientSensitive int = 30