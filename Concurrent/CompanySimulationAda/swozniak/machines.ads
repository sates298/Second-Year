with Constants; use Constants;
with Servers; use Servers;


package Machines is

   protected type AddingMachine is
      entry Compute(job : in Job_Access);
   end AddingMachine;
   
   protected type MultiplyingMachine is
      entry Compute(job : in Job_Access);
   end MultiplyingMachine;
   
   type AM_Access is access AddingMachine;
   type MM_Access is access MultiplyingMachine;
   
   type AddMachines is array (1..AddMachinesNo) of AM_Access;
   type AddMachines_Acc is access all AddMachines;
   type MulMachines is array (1..MulMachinesNo) of MM_Access;
   type MulMachines_Acc is access all MulMachines;
   
   AddMachinesArray: aliased AddMachines := (others => new AddingMachine);
   MulMachinesArray: aliased MulMachines := (others => new MultiplyingMachine);
   
end Machines;
