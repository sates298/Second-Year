with Servers; use Servers;
with Constants; use Constants;
with Menu; use Menu;

package Employees is
   
   type Worker is record
      id: Integer;
      executed: Job;
   end record;
   
   type W_Access is access Worker;
   
   type ClientArray is array (1..ClientsProductsMaxNo) of Integer;
   
   type Client is record 
      id: Integer;
      products: ClientArray;
   end record;
   
   type C_Access is access Client;
      
   task Boss;
   
   task type WorkerRun(actual: W_Access);
   type T_Worker is access WorkerRun;
   type WorkersTasks is array (1..WorkersNo) of T_Worker;
 
   task type ClientRun(actual: C_Access);
   type T_Client is access ClientRun;
   type ClientsTasks is array (1..ClientsNo) of T_Client;

   task GenerateEmployees;
end Employees;
