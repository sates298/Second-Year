#include <stdio.h>
#include <unistd.h>
#include <signal.h>

int main(int argc, char **argv){


  int pid = 1465;

  for(int i=1; i<=64; i++){
    int response = kill(pid,i);
    if(response == 0){
      printf("Signal %d comes\n", i);
    }else{
      printf("Signal %d missed\n", i);
    }
  }
  return 0;
}
