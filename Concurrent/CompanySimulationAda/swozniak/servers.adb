with Constants;

package body Servers is
   use Constants;
   
   -----------------
   -- StoreServer --
   -----------------
   
   task body StoreServer is
      products : StoreArray := (others => 0);
      checkProducts: StoreChecksArray := (others => False);
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

   ----------------
   -- JobsServer --
   ----------------  
   
   task body JobsServer is
      jobs : JobsArray := (others => new Job'(first => 0, second =>0 ,result => 0, operation => '0'));
      checkJobs: JobsChecksArray := (others => False);
      writeIterator : Integer := 1;
      readIterator: Integer := 1;
   begin
      loop
         select
            when checkJobs(readIterator) =>
               accept JobsReadOp (nextJob : out Job_Access) do
                  nextJob := jobs(readIterator);
                  checkJobs(readIterator) := False;
                  readIterator := readIterator + 1;
                  if readIterator > JobsMaxNo then
                     readIterator := 1;
                  end if;
               end JobsReadOp; 
         or
            when not checkJobs(writeIterator) =>
               accept JobsWriteOp (newJob : in Job_Access) do
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
