with Ada.Text_IO;
with Ada.Numerics.Discrete_Random;
with Constants;
with Machines;
with Servers;
with Menu;

package body Employees is
   use Constants;
   use Machines;
   use Servers;
   use Menu;

   ----------
   -- Boss --
   ----------

   task body Boss is
      subtype range100 is Integer range 0 .. 100;
      package Rand_Int is new Ada.Numerics.Discrete_Random(range100);
      use Rand_Int;
      gen : Rand_Int.Generator;
      firstInt: Integer;
      secondInt: Integer;
      op: Integer;
      oper: Character;
      sensitive: range100;
      created: Job_Access;
   begin
      loop
         delay BossSpeed;
         Rand_Int.Reset(gen);
         sensitive := Random(gen);

         if sensitive < BossSensitive then
            Rand_Int.Reset(gen);
            firstInt := Random(gen);

            Rand_Int.Reset(gen);
            secondInt := Random(gen);

            Rand_Int.Reset(gen);
            op := Random(gen) mod 3;
            case op is
            when 0 =>
               oper := '+';
            when 1 =>
               oper := '-';
            when others =>
               oper := '*';
            end case;

            created := new Job'(first => firstInt, second => secondInt,result=>0, operation => oper);


            Servers.JobsServer.JobsWriteOp(newJob => created);
            if not isPeacfull then
            Ada.Text_IO.Put_Line("Task: {" & Integer'Image(firstInt) & ", "
                                 & Integer'Image(secondInt) & ", " & oper & " }");
            end if;

         end if;
      end loop;
   end Boss;

   ---------------
   -- WorkerRun --
   ---------------

   task body WorkerRun  is
      subtype range100 is Integer range 0 .. 100;
      package Rand_Int is new Ada.Numerics.Discrete_Random(range100);
      use Rand_Int;
      gen : Rand_Int.Generator;
      sensitive: range100;


      r : Integer;
      delayedPatience : Duration := 0.3;
      done : Boolean;
      addMachine: AM_Access;
      mulMachine: MM_Access;
   begin
       loop
         delay WorkerSpeed;
         Rand_Int.Reset(gen);
         sensitive := Random(gen);

         if sensitive < WorkerSensitive then

            Servers.JobsServer.JobsReadOp(nextJob => actual.executed);
            done := False;
            case actual.executed.operation is
               when '*' =>
                  r := Random(gen) mod MulMachinesNo;
                  mulMachine := actual.mulMachines(r + 1);
                  if actual.isPatient then
                     mulMachine.Compute(actual.executed);
                  else
                     while not done loop
                        select
                           mulMachine.Compute(actual.executed);
                           done := True;
                        else
                           delay delayedPatience;
                           r := Random(gen) mod MulMachinesNo;
                           mulMachine := actual.mulMachines(r + 1);
                        end select;
                     end loop;
                  end if;
               when others =>
                   r := Random(gen) mod AddMachinesNo;
                   addMachine := actual.addMachines(r + 1);
                   if actual.isPatient then
                     addMachine.Compute(actual.executed);
                   else
                     while not done loop
                        select
                           addMachine.Compute(actual.executed);
                           done := True;
                        else
                           delay delayedPatience;
                           r := Random(gen) mod AddMachinesNo;
                           addMachine := actual.addMachines(r + 1);
                        end select;
                     end loop;
                   end if;
            end case;

            Servers.StoreServer.StoreWriteOp(result => actual.executed.result);
            actual.completed := actual.completed + 1;
            if not isPeacfull then
            Ada.Text_IO.Put_Line("Worker(id:" & Integer'Image(actual.id) & "): Task{" & Integer'Image(actual.executed.first) & ", "
                                 & Integer'Image(actual.executed.second) & ", " & actual.executed.operation
                                 & "},  Result{" & Integer'Image(actual.executed.result) & " }");
            end if;

         end if;
      end loop;

   end WorkerRun;

   ---------------
   -- ClientRun --
   ---------------

   task body ClientRun is
      subtype range100 is Integer range 0 .. 100;
      package Rand_Int is new Ada.Numerics.Discrete_Random(range100);
      use Rand_Int;
      gen : Rand_Int.Generator;
      sensitive: range100;
      productsNo: Integer;
      --products: ClientArray := actual.products;
      iterator: Integer := 1;
      id: Integer := actual.id;
   begin
      loop
         delay ClientSpeed;
         Rand_Int.Reset(gen);
         sensitive := Random(gen);
         iterator := 1;

         if sensitive < ClientSensitive then

            Rand_Int.Reset(gen);
            productsNo := Random(gen) mod ClientsProductsMaxNo;
            while iterator < productsNo loop
               Rand_Int.Reset(gen);
               sensitive := Random(gen);

               if sensitive < 50 then
                  Servers.StoreServer.StoreReadOp(result => actual.products(iterator));
                  iterator := iterator + 1;
               end if;
            end loop;

            if iterator > 1 and not isPeacfull then
               Ada.Text_IO.Put("Client(id:" & Integer'Image(id) & "): products: {");
               for i in 1..iterator-1 loop
                  Ada.Text_IO.Put(Integer'Image(actual.products(i)) & " ");
               end loop;
               Ada.Text_IO.Put_Line("}");
            end if;

         end if;
      end loop;

   end ClientRun;


end Employees;
