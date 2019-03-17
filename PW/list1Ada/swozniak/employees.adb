with Ada.Text_IO;
with Ada.Numerics.Discrete_Random;
with Constants;
with Servers;

package body Employees is
   use Constants;
   use Servers;

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
      created: Servers.Job;
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

         created := (first => firstInt, second => secondInt, operation => oper);

         Servers.JobsServer.JobsWriteOp(newJob => created);
            Ada.Text_IO.Put_Line("Task: {" & Integer'Image(firstInt) & ", "
                                 & Integer'Image(secondInt) & ", " & oper & " }");

         end if;
      end loop;
   end Boss;

   ---------------
   -- WorkerRun --
   ---------------

   task body WorkerRun is
      subtype range100 is Integer range 0 .. 100;
      package Rand_Int is new Ada.Numerics.Discrete_Random(range100);
      use Rand_Int;
      gen : Rand_Int.Generator;
      sensitive: range100;

      executed: Job;
      resultOp: Integer;
      id: Integer := 0;
   begin
       loop
         delay WorkerSpeed;
         Rand_Int.Reset(gen);
         sensitive := Random(gen);

         if sensitive < WorkerSensitive then

            Servers.JobsServer.JobsReadOp(nextJob => executed);

            case executed.operation is
               when '+' =>
                  resultOp := executed.first + executed.second;
               when '-' =>
                  resultOp := executed.first - executed.second;
               when others =>
                  resultOp := executed.first * executed.second;
            end case;

            Ada.Text_IO.Put_Line("Worker(id:" & Integer'Image(id) & "): Task{" & Integer'Image(executed.first) & ", "
                                 & Integer'Image(executed.second) & ", " & executed.operation
                                 & "},  Result{" & Integer'Image(resultOp) & " }");
            Servers.StoreServer.StoreWriteOp(result => resultOp);
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
      products: ClientArray := (1.. ClientsProductsMaxNo => 0);
      iterator: Integer := 1;
      id: Integer := 0;
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
                  Servers.StoreServer.StoreReadOp(result => products(iterator));

                  Ada.Text_IO.Put("Client(id:" & Integer'Image(id) & "): products: {");
                  for i in 1..iterator loop
                     Ada.Text_IO.Put(Integer'Image(products(i)) & " ");
                  end loop;
                  Ada.Text_IO.Put_Line("}");

                  iterator := iterator + 1;
               end if;
            end loop;
         end if;
      end loop;

   end ClientRun;

end Employees;
