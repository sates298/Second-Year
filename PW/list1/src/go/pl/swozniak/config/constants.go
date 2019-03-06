package config

import "time"

/*
	constants to configure program
 */
const WorkersNo int = 3
const ClientsNo int = 2

const TasksMaxNo int = 30
const ProductsMaxNo int = 40

const ClientsProductsMaxNo int = 2

const BossSpeed = 500*time.Millisecond
const BossSensitive int = 90 //%

const WorkerSpeed = 800*time.Millisecond
const WorkerSensitive int = 40 //%

const ClientSpeed = time.Second
const ClientSensitive int = 50 //%