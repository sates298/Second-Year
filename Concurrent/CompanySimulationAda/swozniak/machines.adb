
package body Machines is

   protected body AddingMachine is
      entry Compute(job: in Job_Access)
        when True is
      begin
         delay AddMachineSpeed;
         case job.operation is
            when '-' =>
               job.result := job.first - job.second;
            when others =>
               job.result := job.first + job.second;
         end case;
      end Compute;
   end AddingMachine;
      
   protected body MultiplyingMachine is
      entry Compute(job: in Job_Access)
        when True is
      begin 
         delay MulMachineSpeed;
         job.result := job.first * job.second;
      end Compute;
   end MultiplyingMachine;
end Machines;
