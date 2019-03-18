with Ada.Text_IO;
with Ada.Numerics.Discrete_Random;
with Constants;
with Servers;
with Menu;

package body Employees is
   use Constants;
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

      resultOp: Integer;
      id: Integer := actual.id;
   begin
       loop
         delay WorkerSpeed;
         Rand_Int.Reset(gen);
         sensitive := Random(gen);

         if sensitive < WorkerSensitive then

            Servers.JobsServer.JobsReadOp(nextJob => actual.executed);

            case actual.executed.operation is
               when '+' =>
                  resultOp := actual.executed.first + actual.executed.second;
               when '-' =>
                  resultOp := actual.executed.first - actual.executed.second;
               when others =>
                  resultOp := actual.executed.first * actual.executed.second;
            end case;

            Servers.StoreServer.StoreWriteOp(result => resultOp);
            if not isPeacfull then
            Ada.Text_IO.Put_Line("Worker(id:" & Integer'Image(id) & "): Task{" & Integer'Image(actual.executed.first) & ", "
                                 & Integer'Image(actual.executed.second) & ", " & actual.executed.operation
                                 & "},  Result{" & Integer'Image(resultOp) & " }");
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

   -----------------------
   -- GenerateEmployees --
   -----------------------

   task body GenerateEmployees is
      type workers is array (1..WorkersNo) of W_Access;
      type clients is array (1..ClientsNo) of C_Access;

      w_array: workers;
      c_array: clients;
      w_Tasks: WorkersTasks;
      c_Tasks: ClientsTasks;
   begin
      for I in 1..WorkersNo loop
         w_array(I) := new Worker'(I-1,(first => 0, second => 0, operation => '0'));
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

   end GenerateEmployees;

end Employees;
