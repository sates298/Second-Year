package Constants is
   WorkersNo: constant Integer:= 1;
   ClientsNo: constant Integer := 1;
   
   JobsMaxNo: constant Integer := 20;
   ProductsMaxNo: constant Integer := 15;
   
   ClientsProductsMaxNo: constant Integer := 5;
   
   BossSpeed: Duration := 0.5; --seconds
   BossSensitive: constant Integer := 80; --%
   
   WorkerSpeed: Duration := 0.8; --seconds
   WorkerSensitive: constant Integer := 40; --%
   
   ClientSpeed: Duration := 1.0; --seconds
   ClientSensitive: constant Integer := 40; --%
end Constants;
