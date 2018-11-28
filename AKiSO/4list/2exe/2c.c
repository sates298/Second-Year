#include <stdio.h>
#include <unistd.h>
#include <signal.h>

int main(int argc, char **argv){

  int pid = 1466;
  int status;
  for(int i=0; i<20; i++){
    status = kill(pid,SIGUSR1);
    if(status == 0){
      printf("%d : SIGUSR1 came\n", i);
    } else {
      printf("%d : SIGUSR1 didnt't come\n", i);
    }
  }

  return 0;
}
