with Servers; use Servers;
with Constants; use Constants;

package Employees is
   
   type Worker is record
      id: Integer;
      executed: Job;
   end record;
   
   type ClientArray is array (1..ClientsProductsMaxNo) of Integer;
   
   type Client is record 
      id: Integer;
      products: ClientArray;
   end record;
      
   task Boss;
   task WorkerRun;
   task ClientRun;

end Employees;
