with Ada.Text_IO;
with Ada.Numerics.Discrete_Random;
with Constants;
with Machines;
with Servers;
with Service;

package body Employees is
   use Constants;
   use Machines;
   use Servers;
   use Service;

   ----------
   -- Boss --
   ----------

   task body Boss is
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
         sensitive := Rand_Int.Random(gen);

         if sensitive < BossSensitive then
--              Rand_Int.Reset(gen);
            firstInt := Rand_Int.Random(gen) + 1 ;

--              Rand_Int.Reset(gen);
            secondInt := Rand_Int.Random(gen) + 1;

            while firstInt = secondInt loop
               secondInt := Rand_Int.Random(gen) + 1;
            end loop;

--              Rand_Int.Reset(gen);
            op := Rand_Int.Random(gen) mod 3;
            case op is
            when 0 =>
               oper := '+';
            when 1 =>
               oper := '-';
            when others =>
               oper := '*';
            end case;

            created := new Job'(first => firstInt,
                                second => secondInt,
                                result=>0,
                                operation => oper);


            Servers.JobsServer.JobsWriteOp(newJob => created);
            if not isPeacfull then
               Ada.Text_IO.Put_Line("Task: {" &
                                      Integer'Image(firstInt) &
                                      ", " & Integer'Image(secondInt) &
                                      ", " & oper & " }");
            end if;

         end if;
      end loop;
   end Boss;

   ---------------
   -- WorkerRun --
   ---------------

   task body WorkerRun  is
      gen : Rand_Int.Generator;
      sensitive: range100;


      r : Integer;
      delayedPatience : Duration := 0.3;
      finishedJob : Boolean := False;
      amount : Integer;
      done : Boolean;
      addMachine: AM_Access;
      mulMachine: MM_Access;
   begin
       loop
         delay WorkerSpeed;
         Rand_Int.Reset(gen);
         sensitive := Rand_Int.Random(gen);

         if sensitive < WorkerSensitive then

            Servers.JobsServer.JobsReadOp(nextJob => actual.executed);
            finishedJob := False;
            amount := 0;
            while not finishedJob loop
               done := False;
               case actual.executed.operation is
               when '*' =>

                  r := (Rand_Int.Random(gen) mod MulMachinesNo) + 1;
                  mulMachine := actual.mulMachines(r);
                  if actual.isPatient then
                     mulMachine.Compute(actual.executed);
                  else
                     while not done loop
                        select
                           mulMachine.Compute(actual.executed);
                           done := True;
                        else
                           delay delayedPatience;

                           r := (Rand_Int.Random(gen) mod MulMachinesNo) + 1;
                           mulMachine := actual.mulMachines(r);
                        end select;
                     end loop;
                  end if;

                  if actual.executed.result = 0 then

                       ServiceTask.
                       Complains(complain => new Complain'(
                                 machineType  => MULMACHINEIDENTIFIER,
                                 machineIndex => r,
                                 collision    => mulMachine.GetCollisions));

                     if not isPeacfull  and amount < 5 then
                        Ada.Text_IO.Put_Line("Worker(id:" &
                                      Integer'Image(actual.id) &
                                      ") is complainig on mul machine {" &
                                      Integer'Image(r) & "} ");
                         amount := amount + 1;
                     end if;
                  else
                      finishedJob := True;
                  end if;

               when others =>

                  r := (Rand_Int.Random(gen) mod AddMachinesNo) + 1;
                  addMachine := actual.addMachines(r);
                  if actual.isPatient then
                     addMachine.Compute(actual.executed);
                   else
                     while not done loop
                        select
                           addMachine.Compute(actual.executed);
                           done := True;
                        else
                           delay delayedPatience;

                           r := (Rand_Int.Random(gen) mod AddMachinesNo) + 1;
                           addMachine := actual.addMachines(r);
                        end select;
                     end loop;
                  end if;

                  if actual.executed.result = 0 then

                     ServiceTask.
                       Complains(complain => new Complain'(
                                 machineType  => ADDMACHINEIDENTIFIER,
                                 machineIndex => r,
                                 collision    => addMachine.GetCollisions));

                     if not isPeacfull  and amount < 5 then
                        Ada.Text_IO.Put_Line("Worker(id:" &
                                        Integer'Image(actual.id) &
                                        ") is complainig on add machine {" &
                                        Integer'Image(r) & "} ");
                        amount := amount + 1;
                     end if;
                  else
                      finishedJob := True;
                  end if;

               end case;




            end loop;

            Servers.StoreServer.StoreWriteOp(result => actual.executed.result);
            actual.completed := actual.completed + 1;
            if not isPeacfull then
               Ada.Text_IO.Put_Line("Worker(id:" &
                                      Integer'Image(actual.id) &
                                      "): Task{" &
                                      Integer'Image(actual.executed.first) &
                                      ", " &
                                      Integer'Image(actual.executed.second) &
                                      ", " &
                                      actual.executed.operation &
                                      "},  Result{" &
                                      Integer'Image(actual.executed.result) &
                                      " }");
            end if;

         end if;
      end loop;

   end WorkerRun;

   ---------------
   -- ClientRun --
   ---------------

   task body ClientRun is
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
         sensitive := Rand_Int.Random(gen);
         iterator := 1;

         if sensitive < ClientSensitive then

            Rand_Int.Reset(gen);
            productsNo := Rand_Int.Random(gen) mod ClientsProductsMaxNo;
            while iterator < productsNo loop
               Rand_Int.Reset(gen);
               sensitive := Rand_Int.Random(gen);

               if sensitive < 50 then
                  Servers.StoreServer.
                    StoreReadOp(result => actual.products(iterator));
                  iterator := iterator + 1;
               end if;
            end loop;

            if iterator > 1 and not isPeacfull then
               Ada.Text_IO.Put("Client(id:" &
                                 Integer'Image(id) &
                                 "): products: {");
               for i in 1..iterator-1 loop
                  Ada.Text_IO.Put(Integer'Image(actual.products(i)) & " ");
               end loop;
               Ada.Text_IO.Put_Line("}");
            end if;

         end if;
      end loop;

   end ClientRun;


end Employees;
