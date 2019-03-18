package Constants is
   WorkersNo: constant Integer:= 3;
   ClientsNo: constant Integer := 2;
   
   JobsMaxNo: constant Integer := 20;
   ProductsMaxNo: constant Integer := 15;
   
   ClientsProductsMaxNo: constant Integer := 5;
   
   BossSpeed: Duration := 1.0; --seconds
   BossSensitive: constant Integer := 80; --%
   
   WorkerSpeed: Duration := 1.6; --seconds
   WorkerSensitive: constant Integer := 40; --%
   
   ClientSpeed: Duration := 2.0; --seconds
   ClientSensitive: constant Integer := 40; --%
end Constants;
