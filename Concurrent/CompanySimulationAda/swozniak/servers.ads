with Constants; 
with Ada.Numerics.Discrete_Random;

package Servers is
   use Constants;
   
   type Job is record
      first: Integer;
      second: Integer;
      result: Integer;
      operation: Character;
   end record;
   
   type Job_Access is access Job;
   
   type StoreArray is array (1..ProductsMaxNo) of Integer;
   type StoreChecksArray is array (1..ProductsMaxNo) of Boolean;
   
   type JobsChecksArray is array (1..JobsMaxNo) of Boolean;
   type JobsArray is array (1..JobsMaxNo) of Job_Access;

   task StoreServer is
      entry StoreReadOp(result : out Integer);
      entry StoreWriteOp(result: in Integer);
      entry StoreGetAllOp(results: out StoreArray;
                          checks: out StoreChecksArray);
   end StoreServer;
     
   task JobsServer is
      entry JobsReadOp(nextJob: out Job_Access);
      entry JobsWriteOp(newJob: in Job_Access);
      entry JobsGetAllOp(response: out JobsArray;
                         checks: out JobsChecksArray);
   end JobsServer;
        
    subtype range100 is Integer range 0 .. 100;
    package Rand_Int is new Ada.Numerics.Discrete_Random(range100);
     
end Servers;
