with Constants; use Constants;
with Machines; use Machines;
with Servers; use Servers;
with Ada.Text_IO;

package body Service is

   task body ServiceWorkerRun is
      current : Cmp_Access;
   begin
      loop
         select
            accept Fix(complain : in Cmp_Access) do
               current := complain;
            end Fix;
            
            delay ServiceWorkerSpeed;
         
              case current.machineType is
              when ADDMACHINEIDENTIFIER =>
                  actual.addMachines(current.machineIndex).Backdoor;
                  if not isPeacfull then
                     Ada.Text_IO.Put_Line("Service Worker: {" &
                                           Integer'Image(actual.id) & 
                                         " } fix {" &
                                         Integer'Image(current.machineIndex) &
                                         "} add machine");
                  end if;
              when MULMACHINEIDENTIFIER =>
                 actual.mulMachines(current.machineIndex).Backdoor;
                  if not isPeacfull then
                     Ada.Text_IO.Put_Line("Service Worker: {" &
                                           Integer'Image(actual.id) & 
                                         " } fix {" &
                                         Integer'Image(current.machineIndex) &
                                         "} mul machine");
                  end if;
               when others => 
                  if not isPeacfull then
                     Ada.Text_IO.Put_Line("mistake");
                  end if;
                  
              end case;  


            ServiceTask.Response(complain => current);
            actual.isBusy := False;
            
            
         end select;
      end loop;
   end ServiceWorkerRun;
   
   task body ServiceTask is
      type checksAddMachineArray is array (1..AddMachinesNo) of Boolean;
      type logsAddMachineArray is array (1..AddMachinesNo) of Integer;
      type checksMulMachineArray is array (1..MulMachinesNo) of Boolean;
      type logsMulMachineArray is array (1..MulMachinesNo) of Integer;
      
      addStatus: checksAddMachineArray := (others => False);
      addIsWorking: checksAddMachineArray := (others => True);
      addLogs: logsAddMachineArray := (others => 0);
      
      mulStatus: checksMulMachineArray := (others => False);
      mulIsWorking: checksMulMachineArray := (others => True);
      mulLogs: logsMulMachineArray := (others => 0);
      
      
      currentWorker : SW_Access;
      current: Cmp_Access;
      found: Boolean;
   begin
      
      loop
         select
            accept Response (complain : in Cmp_Access) do
               current := complain;
            end Response;
            
              case current.machineType is
               when ADDMACHINEIDENTIFIER =>
                  addStatus(current.machineIndex) := False;
                  addIsWorking(current.machineIndex) := True;
               when MULMACHINEIDENTIFIER =>
                  mulStatus(current.machineIndex) := False;
                  mulIsWorking(current.machineIndex) := True;
               when others => 
                  if not isPeacfull then
                     Ada.Text_IO.Put_Line("mistake");
                  end if;
                  
              end case;
                        
         or
            accept Complains (complain : in Cmp_Access) do
               current := complain;
            end Complains;
              case current.machineType is
                 when ADDMACHINEIDENTIFIER =>
                    if addLogs(current.machineIndex) < current.collision and
                      addIsWorking(current.machineIndex) then
                       addLogs(current.machineIndex) := current.collision;
                     addIsWorking(current.machineIndex) := False;
                  end if;
                  
                  when MULMACHINEIDENTIFIER =>
                    if mulLogs(current.machineIndex) < current.collision and
                      mulIsWorking(current.machineIndex) then
                       mulLogs(current.machineIndex) := current.collision;
                     mulIsWorking(current.machineIndex) := False;
                  end if;  
                  
                  when others => 
                  if not isPeacfull then
                     Ada.Text_IO.Put_Line("mistake");
                  end if;
            end case;
            
         end select;
         
         
         found := False;
         currentWorker := null;
         for I in 1..ServiceWorkersNo loop
            if not sw_array(I).isBusy then
               currentWorker := sw_array(I);
            end if;
            exit when currentWorker /= null ;            
         end loop;
         
         if  currentWorker /= null then
            for I in 1..AddMachinesNo loop
               if not addStatus(I) and not addIsWorking(I) then
                  found := True;
                  sw_array(currentWorker.id).isBusy := True;
                  sworkers(currentWorker.id).
                    Fix(complain => new Complain'(machineType  => ADDMACHINEIDENTIFIER,
                                                  machineIndex => I,
                                                  collision    => currentWorker.
                                                    addMachines(I).
                                                    GetCollisions));
               end if;
               exit when found;
            end loop;
         end if;
         
         found := False;
         currentWorker := null;
         for I in 1..ServiceWorkersNo loop
            if not sw_array(I).isBusy then
               currentWorker := sw_array(I);
            end if;
            exit when currentWorker /= null;        
         end loop;
         
         if currentWorker /= null then
            for I in 1..MulMachinesNo loop
               if not mulStatus(I) and not mulIsWorking(I) then
                  found := True;
                  sw_array(currentWorker.id).isBusy := True;
                  sworkers(currentWorker.id).
                    Fix(complain => new Complain'(machineType  => MULMACHINEIDENTIFIER,
                                                  machineIndex => I,
                                                  collision    => currentWorker.
                                                    mulMachines(I).
                                                    GetCollisions));
               end if;
               exit when found;
            end loop;
         end if;
         
      end loop;
      
      
   end ServiceTask; 
   
end Service;
