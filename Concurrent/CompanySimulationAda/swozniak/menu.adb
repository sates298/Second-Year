with Ada.Text_IO;
with Ada.Integer_Text_IO;
with Servers;
with Employees;
with Constants;
with Machines;
with Service;

package body Menu is
   use Ada.Text_IO;
   use Ada.Integer_Text_IO;
   use Servers;
   use Constants;
   use Machines;
   use Employees;
   use Service;
   
   task body Gui is
      choose: Integer;
      iterator: Integer := 1;
      
      actualProduct: Integer;
      store: StoreArray;
      storeCheck: StoreChecksArray;
      
      actualJob: Job_Access;
      jobs: JobsArray;
      jobsCheck: JobsChecksArray;
            
   begin
      loop
         if isPeacfull then
            Put_Line("1. Show list of tasks.");
            Put_Line("2. Show store.");
            Put_Line("3. Show workers.");
            Put_Line("4. Show machines.");
            Put_Line("5. Show service workers");
            Put_Line("others. Change mode on loud.");
            Get(choose);
            case choose is
               when 1 =>
                  Servers.JobsServer.JobsGetAllOp(response => jobs,
                                                  checks   => jobsCheck);
                  
                  Put("List of Tasks: [");
                  while iterator <= JobsMaxNo loop
                     actualJob := jobs(iterator);
                     if jobsCheck(iterator) then
                        Put("{" & Integer'Image(actualJob.first) & "," 
                            & Integer'Image(actualJob.second) & ", " 
                            & actualJob.operation & "}");
                     end if;
                     iterator := iterator + 1;
                  end loop;
                  Put_Line("]");
                  iterator := 1;
               when 2 =>
                  Servers.StoreServer.StoreGetAllOp(results => store,
                                                    checks  => storeCheck);
                  Put("Store : [");
                  while iterator <= ProductsMaxNo loop
                     actualProduct := store(iterator);
                     if storeCheck(iterator) then
                        Put(Integer'Image(actualProduct) & " ");
                     end if;
                     iterator := iterator + 1;
                  end loop;
                  Put_Line("]");
                  iterator := 1;
               when 3 =>
                  Put("Workers :[");
                  for I in 1..WorkersNo loop
                     Put("{ id:" & Integer'Image(w_array(I).id) &
                           ", Completed: " &
                           Integer'Image(w_array(I).completed) &
                           ", isPatient: " &
                           Boolean'Image(w_array(I).isPatient) & "}");
                  end loop;
                  Put_Line("]");
               when 4 =>
                  Put("Add Machines : [");
                  for I in 1..AddMachinesNo loop
                     Put("{ id:" & Integer'Image(I) &
                           ", isBroken: " &
                             Boolean'Image(AddMachinesArray(I).GetBroken) &
                           ", collisions: " &
                           Integer'Image(AddMachinesArray(I).GetCollisions) & "}");
                  end loop;
                  Put_Line("]");
                           
                  Put("Mul Machines : [");
                  for I in 1..MulMachinesNo loop
                     Put("{ id:" & Integer'Image(I) &
                           ", isBroken: " &
                             Boolean'Image(MulMachinesArray(I).GetBroken) &
                           ", collisions: " &
                           Integer'Image(MulMachinesArray(I).GetCollisions) & "}");
                  end loop;
                  Put_Line("]");
               when 5 =>
                  Put("Service workers :[");
                  for I in 1..ServiceWorkersNo loop
                     Put("{ id:" & Integer'Image(sw_array(I).id) &
                           ", isBusy: " &
                           Boolean'Image(sw_array(I).isBusy) & "}");
                  end loop;
                  Put_Line("]");
               when others =>
                  isPeacfull := False;
            end case;
         else
            Get(choose);
            isPeacfull := True;
         end if;
      end loop;
   end Gui;
   

end Menu;
