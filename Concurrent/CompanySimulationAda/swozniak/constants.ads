package Constants is
   WorkersNo: constant Integer:= 4;
   ClientsNo: constant Integer := 5;
   ServiceWorkersNo: constant Integer := 3;
   
   AddMachinesNo: constant Integer := 5;
   MulMachinesNo: constant Integer := 3;
   
   JobsMaxNo: constant Integer := 20;
   ProductsMaxNo: constant Integer := 15;
   
   ClientsProductsMaxNo: constant Integer := 5;
   
   BossSpeed: Duration := 1.0; --seconds
   BossSensitive: constant Integer := 80; --%
   
   WorkerSpeed: Duration := 1.9; --seconds
   WorkerSensitive: constant Integer := 40; --%
   
   ClientSpeed: Duration := 4.0; --seconds
   ClientSensitive: constant Integer := 40; --%
   
   ServiceWorkerSpeed: Duration := 1.2; --seconds 
   
   AddMachineSpeed: Duration := 0.7; --seconds
   AddMachineReliability: constant Integer := 60; --%
   MulMachineSpeed: Duration := 0.9; --seconds
   MulMachineReliability: constant Integer := 65; --%
   
   isPeacfull: Boolean := True;
end Constants;
