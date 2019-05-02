with Constants; use Constants;
with Servers; use Servers;
with Ada; use Ada;
with Ada.Numerics;
with Ada.Numerics.Discrete_Random;

package Machines is

   subtype range100Machines is Integer range 0 .. 100;
   package Rand_Int_M is new Ada.Numerics.Discrete_Random(range100Machines);
   use Rand_Int_M;
   
   protected type AddingMachine is
      procedure SetId(identity : Integer);
      entry Backdoor;
      entry Compute(job : in Job_Access);
      function GetCollisions return Integer;
      function GetBroken return Boolean;
   private
      
      id : Integer;
      isBroken : Boolean := False;
      collisionsNo: Integer := 0;

      gen : Rand_Int_M.Generator;
      sensitive: range100Machines;

      synchronize : Boolean := True;
      
      r : Integer;
   end AddingMachine;
   
   protected type MultiplyingMachine is
      procedure SetId( identity : Integer);
      entry Backdoor;
      entry Compute(job : in Job_Access);
      function GetCollisions return Integer;
      function GetBroken return Boolean;
   private
      id : Integer;
      isBroken : Boolean := False;
      collisionsNo: Integer := 0;

      gen : Rand_Int_M.Generator;
      sensitive: range100Machines;


      r : Integer;
   end MultiplyingMachine;
   
   type AM_Access is access AddingMachine;
   type MM_Access is access MultiplyingMachine;
   
   type AddMachines is array (1..AddMachinesNo) of AM_Access;
   type AddMachines_Acc is access all AddMachines;
   type MulMachines is array (1..MulMachinesNo) of MM_Access;
   type MulMachines_Acc is access all MulMachines;
   
   AddMachinesArray: aliased AddMachines; 
   MulMachinesArray: aliased MulMachines;
     
   ADDMACHINEIDENTIFIER: constant Integer := 0;
   MULMACHINEIDENTIFIER: constant Integer := 1;
   
   
end Machines;
