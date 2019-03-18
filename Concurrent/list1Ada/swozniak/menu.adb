with Ada.Text_IO;
with Ada.Integer_Text_IO;
with Servers;
with Constants;

package body Menu is
   use Ada.Text_IO;
   use Ada.Integer_Text_IO;
   use Servers;
   use Constants;
   
   task body Gui is
      choose: Integer;
      iterator: Integer := 1;
      
      actualProduct: Integer;
      store: StoreArray;
      storeCheck: StoreChecksArray;
      
      actualJob: Job;
      jobs: JobsArray;
      jobsCheck: JobsChecksArray;
      
   begin
      loop
         if isPeacfull then
            Put_Line("1. Show list of tasks.");
            Put_Line("2. Show store.");
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
