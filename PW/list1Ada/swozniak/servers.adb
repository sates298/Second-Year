with Constants;

package body Servers is
   use Constants;
   
   ---------------
   --StoreServer--
   ---------------
   
   task body StoreServer is
      products : StoreArray := (1..ProductsMaxNo => 0);
      checkProducts: StoreChecksArray := (1..ProductsMaxNo => False);
      writeIterator : Integer := 1;
      readIterator: Integer := 1;
   begin
      loop
         select
            when checkProducts(readIterator) =>
               accept StoreReadOp (result : out Integer) do
                  result := products(readIterator);
                  checkProducts(readIterator) := False;
                  readIterator := readIterator + 1;
                  if readIterator > ProductsMaxNo then
                     readIterator := 1;
                  end if;
                  
               end StoreReadOp; 
         or
            when not checkProducts(writeIterator) =>
               accept StoreWriteOp (result : in Integer) do
                  products(writeIterator) := result;
                  checkProducts(writeIterator) := True;
                  writeIterator := writeIterator + 1;
                  if writeIterator > ProductsMaxNo then
                     writeIterator := 1;
                  end if;
               end StoreWriteOp;
         or
            accept StoreGetAllOp (results : out StoreArray; checks: out StoreChecksArray) do
               results := products;
               checks := checkProducts;
            end StoreGetAllOp;
            
         end select;
      end loop;
   end StoreServer;  

   --------------
   --JobsServer--
   --------------  
   
   task body JobsServer is
      jobs : JobsArray := (1..JobsMaxNo => (first => 0, second => 0, operation => '0'));
      checkJobs: JobsChecksArray := (1..JobsMaxNo => False);
      writeIterator : Integer := 1;
      readIterator: Integer := 1;
   begin
      loop
         select
            when checkJobs(readIterator) =>
               accept JobsReadOp (nextJob : out Job) do
                  nextJob := jobs(readIterator);
                  checkJobs(readIterator) := False;
                  readIterator := readIterator + 1;
                  if readIterator > JobsMaxNo then
                     readIterator := 1;
                  end if;
               end JobsReadOp; 
         or
            when not checkJobs(writeIterator) =>
               accept JobsWriteOp (newJob : in Job) do
                  jobs(writeIterator) := newJob;
                  checkJobs(writeIterator) := True;
                  writeIterator := writeIterator + 1;
                  if writeIterator > JobsMaxNo then
                     writeIterator := 1;
                  end if;
                  
               end JobsWriteOp;
         or
               accept JobsGetAllOp (response : out JobsArray; checks: out JobsChecksArray) do
               response := jobs;
               checks := checkJobs;
               end JobsGetAllOp;
         end select;  
      end loop;
         
   end JobsServer;
      
      
      
      
end Servers;
