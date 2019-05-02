with Ada.Text_IO; use Ada.Text_IO;
with Ada.Numerics.Discrete_Random;
with Constants; use Constants;
with Machines; use Machines;
with Servers; use Servers;
with Service; use Service;
with Employees; use Employees;
with Menu; use Menu;

procedure Main is

   subtype range100 is Integer range 0 .. 100;
   package Rand_Int is new Ada.Numerics.Discrete_Random(range100);
   use Rand_Int;
   gen : Rand_Int.Generator;
   bool : Integer;

   am_array : AddMachines_Acc := AddMachinesArray'Access;
   mm_array : MulMachines_Acc := MulMachinesArray'Access;
   patience : Boolean;
begin

   for I in 1..AddMachinesNo loop
      am_array(I) := new AddingMachine;
      am_array(I).SetId(identity => I);
   end loop;

   for I in 1..MulMachinesNo loop
      mm_array(I) := new MultiplyingMachine;
      mm_array(I).SetId(identity => I);
   end loop;

   for I in 1..ServiceWorkersNo loop
         sw_array(I) :=
           new ServiceWorker'(id        => I,
                            isBusy      => False,
                            addMachines => am_array,
                            mulMachines => mm_array);

   end loop;



   for I in 1..WorkersNo loop
      Rand_Int.Reset(gen);
      bool := Rand_Int.Random(gen) mod 2;

      if (bool = 1) then
         patience := True;
      else
         patience := False;
      end if;

      w_array(I) := new Worker'(id => I,
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
      c_array(J) := new Client'(J,(others => 0));
   end loop;

   for I in 1..ServiceWorkersNo loop
         sworkers(I) :=  new ServiceWorkerRun(actual => sw_array(I));
   end loop;

   for K in 1..WorkersNo loop
      w_Tasks(K) := new WorkerRun(actual => w_array(K));
   end loop;

   for L in 1..ClientsNo loop
      c_Tasks(L) := new ClientRun(actual => c_array(L));
   end loop;
end Main;
