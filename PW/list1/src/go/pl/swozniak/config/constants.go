package config

import "time"

const WorkersNo int = 1
const ClientsNo int = 2

const TasksMaxNo int = 2
const ProductsMaxNo int = 2

const ClientsProductsMaxNo int = 4

const BossSpeed = 800*time.Millisecond
const BossSensitive int = 70 //%

const WorkerSpeed = 100*time.Millisecond
const WorkerSensitive int = 30 //%

const ClientSpeed = time.Second
const ClientSensitive int = 10 //%