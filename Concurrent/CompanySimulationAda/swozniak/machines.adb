with Ada.Text_IO;

package body Machines is
 

   protected body AddingMachine is
      procedure SetId(identity : Integer) is
      begin
         id := identity;
      end SetId;
      
      entry Backdoor
        when synchronize is
      begin
         synchronize := False;
         isBroken := False;
         
         Ada.Text_IO.Put_Line("AddMachine: {" &
                                         Integer'Image(id) & 
                                " } is fixed now");
         synchronize := True;
      end Backdoor;
      
      entry Compute(job: in Job_Access)
        when synchronize is
      begin
         synchronize := False;
         delay AddMachineSpeed;
        
         if isBroken then
            job.result := 0;
         else
            case job.operation is
            when '-' =>
               job.result := job.first - job.second;
               if not isPeacfull then
                  Ada.Text_IO.Put_Line("AddMachine: {" &
                                         Integer'Image(id) & 
                                         " } working on : " &
                                         Integer'Image(job.first) &
                                         " - " & Integer'Image(job.second) &
                                       " = " & Integer'Image(job.result));
               end if;
            when others =>
               job.result := job.first + job.second;
               if not isPeacfull then
                  Ada.Text_IO.Put_Line("AddMachine: {" &
                                         Integer'Image(id) & 
                                         " } working on : " &
                                         Integer'Image(job.first) &
                                         " + " & Integer'Image(job.second) &
                                       " = " & Integer'Image(job.result));
               end if;
            end case;
            
            Rand_Int_M.Reset(gen);
            r := Rand_Int_M.Random(gen);
            if r > AddMachineReliability then
               isBroken := True;
               collisionsNo := collisionsNo + 1;
               if not isPeacfull then
                  Ada.Text_IO.Put_Line("AddMachine: {" &
                                         Integer'Image(id) & 
                                         " } is broken now");
               end if;
               
            end if;
            
         end if;
         
         synchronize := True;
         
      end Compute;
      
      function GetCollisions return Integer is
      begin
         return collisionsNo;
      end GetCollisions;
      
      function GetBroken return Boolean is
      begin
         return isBroken;
      end GetBroken;
      
   end AddingMachine;
      
   protected body MultiplyingMachine is
      procedure SetId(identity : Integer) is
      begin
         id := identity;
      end SetId;
      
      entry Backdoor
        when True is
      begin
         isBroken := False;
      end Backdoor;
      
      entry Compute(job: in Job_Access)
        when True is
      begin 
         delay MulMachineSpeed;
        
         if isBroken then
            job.result := 0;
         else
            job.result := job.first * job.second;
            if not isPeacfull then
                  Ada.Text_IO.Put_Line("MulMachine: {" &
                                         Integer'Image(id) & 
                                         " } working on : " &
                                         Integer'Image(job.first) &
                                         " * " & Integer'Image(job.second) &
                                       " = " & Integer'Image(job.result));
            end if;
         
            Rand_Int_M.Reset(gen);
            r := Rand_Int_M.Random(gen);
            if r > MulMachineReliability then
               isBroken := True;
               collisionsNo := collisionsNo + 1;
               if not isPeacfull then
                  Ada.Text_IO.Put_Line("MulMachine: {" &
                                         Integer'Image(id) & 
                                         " } is broken now");
               end if;
            end if;
            
         end if;

      end Compute;
      
      function GetCollisions return Integer is
      begin
         return collisionsNo;
      end GetCollisions;
      
      function GetBroken return Boolean is
      begin
         return isBroken;
      end GetBroken;
      
   end MultiplyingMachine;
end Machines;
