package config

import "time"

/*
	constants to configure program
 */
const WorkersNo int = 3
const ClientsNo int = 2

const TasksMaxNo int = 20
const ProductsMaxNo int = 15

const ClientsProductsMaxNo int = 3

const BossSpeed = 500*time.Millisecond
const BossSensitive int = 80 //%

const WorkerSpeed = 800*time.Millisecond
const WorkerSensitive int = 40 //%

const ClientSpeed = time.Second
const ClientSensitive int = 40 //%