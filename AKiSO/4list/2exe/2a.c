#include <stdio.h>
#include <unistd.h>
#include <signal.h>

void signal_handler(int i){

   printf("Signal %d is catched\n", i);
}


int main(int argc, char **argv){


  for(int i=1; i<=64; i++){
    signal(i, signal_handler);
  }

  for(int i=1; i<=64; i++){
   if(i == 0 || i == 9 || i == 19 || i == 32 || i == 33) continue;
    int response = kill(getpid(),i);
    if(response == 0){
      printf("Signal %d comes\n", i);
    }else{
      printf("Signal %d missed\n", i);
    }
  }
  return 0;
}
