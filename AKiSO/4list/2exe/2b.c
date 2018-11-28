#include <unistd.h>
#include <stdio.h>
#include <signal.h>
int main(int argc, char **argv){


  int status = kill(1, SIGKILL);
  if(status == 0){
    printf("SIGKILL came\n");
  }else{
    printf("SIGKILL didn't come\n");
  }
/*
  for(int i=1; i<65; i++){
    status = kill(1, i);
    if(status == 0){
      printf("Signal %d came\n", i);
    }else{
      printf("Signal %d didn't come\n", i);
    }
  }

*/


  return 0;
}
