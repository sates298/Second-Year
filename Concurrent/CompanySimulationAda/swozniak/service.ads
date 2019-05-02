with Constants; use Constants;
with Servers; use Servers;
with Machines; use Machines;

package Service is

   pragma Elaborate_Body (Service);
   
   type Complain is record
      machineType: Integer;
      machineIndex: Integer;
      collision: Integer;
   end record;
      
   type Cmp_Access is access Complain;
   
   type ServiceWorker is record
      id: Integer;
      isBusy : Boolean;
      
      addMachines : AddMachines_Acc;
      mulMachines : MulMachines_Acc;
      
   end record;    
   
   type SW_Access is access ServiceWorker;
   type service_workers is array (1..ServiceWorkersNo) of SW_Access;
  
   task type ServiceWorkerRun(actual: SW_Access) is
      entry Fix(complain: in Cmp_Access);
   end ServiceWorkerRun;
   
   type T_ServiceWorker is access ServiceWorkerRun;
   type ServicesTask is array (1..ServiceWorkersNo) of T_ServiceWorker;
   
      
   task ServiceTask is
      entry Response(complain: in Cmp_Access);
      entry Complains(complain: in Cmp_Access);
   end ServiceTask;
   
   sw_array: service_workers;
   sworkers: ServicesTask;
   
   
   
end Service;
