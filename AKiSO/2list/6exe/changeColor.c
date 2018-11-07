#include<stdio.h>

int main(int argc, char const *argv[]){

  for(int i=0; i<256; i++){
    printf("\x1b[38;5;%dm%d.Hello, World!\n", i,i);
  }

  return 0;
}
