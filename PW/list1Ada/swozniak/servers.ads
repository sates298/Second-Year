with Constants; 

package Servers is
   use Constants;
   
   type Job is record
      first: Integer;
      second: Integer;
      operation: Character;
   end record;
   
   type StoreArray is array (1..ProductsMaxNo) of Integer;
   type StoreChecksArray is array (1..ProductsMaxNo) of Boolean;
   
   type JobsChecksArray is array (1..JobsMaxNo) of Boolean;
   type JobsArray is array (1..JobsMaxNo) of Job;

   task StoreServer is
      entry StoreReadOp(result : out Integer);
      entry StoreWriteOp(result: in Integer);
      entry StoreGetAllOp(results: out StoreArray; checks: out StoreChecksArray);
   end StoreServer;
     
   task JobsServer is
      entry JobsReadOp(nextJob: out Job);
      entry JobsWriteOp(newJob: in Job);
      entry JobsGetAllOp(response: out JobsArray; checks: out JobsChecksArray);
   end JobsServer;
        
     
end Servers;
