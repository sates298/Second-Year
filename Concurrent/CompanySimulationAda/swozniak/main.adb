with Ada.Text_IO; use Ada.Text_IO;
with Ada.Numerics.Discrete_Random;
with Constants; use Constants;
with Machines; use Machines;
with Servers; use Servers;
with Employees; use Employees;

procedure Main is
   subtype range2 is Integer range 1..2;
   package Rand_Int is new Ada.Numerics.Discrete_Random(range2);
   use Rand_Int;
   gen : Rand_Int.Generator;
   bool : range2;

   am_array : AddMachines_Acc := AddMachinesArray'Access;
   mm_array : MulMachines_Acc := MulMachinesArray'Access;
   patience : Boolean;
begin

   for I in 1..WorkersNo loop
      Rand_Int.Reset(gen);
      bool := Random(gen);

      if (bool = 1) then
         patience := True;
      else
         patience := False;
      end if;

      w_array(I) := new Worker'(id => I-1,
                                completed => 0,
                                executed => new Job'(first => 0,
                                             second => 0,
                                             result=>0,
                                             operation => '0'),
                                addMachines => am_array,
                                mulMachines => mm_array,
                                isPatient => patience);
   end loop;


   for J in 1..ClientsNo loop
      c_array(J) := new Client'(J-1,(others => 0));
   end loop;

   for K in 1..WorkersNo loop
      w_Tasks(K) := new WorkerRun(actual => w_array(K));
   end loop;

   for L in 1..ClientsNo loop
      c_Tasks(L) := new ClientRun(actual => c_array(L));
   end loop;
end Main;
