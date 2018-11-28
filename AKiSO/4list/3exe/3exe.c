#include <signal.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>


//count legth of strngs array
int array_len(char **array){

  int size = 0;
  while(array[size] != NULL){
    size++;
  }
                                                                                                                 printf("%d : podaj size, %s : podaj drugi element\n",size, array[1]);
  return size;
}

//free memory used by current arguments
void memclear(char **pointers){

  int a_size = array_len(pointers);                                                                         printf("%d : size w free\n", a_size);
  for(int i=0; i<a_size; i++){
    free(pointers[i]);
                                                                                                                          printf("%d : iterator w free\n", i);
  }

  free(pointers);
                                                                                                      printf("przesżło całe free\n");
}


char** split_arguments(char *arguments, char **line){
     
                                                                                                    printf("wgl tu wchodzi do splita?\n");
   //declare delimiter
  char delimiter = ' ';  

  //count size of the table and size of the biggest cell
  int table_size = 0;
  int i=0;
  int max_cell = 0;
  int curr_cell = 0;
  while(arguments[i] != '\0'){
    if(arguments[i] == '\n'){
       i++;
      continue;
    }
    if(arguments[i] == delimiter || (arguments[i+1] == '\0' && curr_cell > 0)){
      if(arguments[i+1] == delimiter){
        i++; 
        continue;
      }
      if(arguments[i+1] == '\0'){
        break;
      }
      if(curr_cell >= max_cell){
        max_cell = curr_cell;
      }
      curr_cell = 0;
      i++;
    }else {
      curr_cell++;
      i++;
    }
    if(curr_cell == 1){
      table_size++;
    }
  }
  //increase max_cell for '\0' char
  max_cell++;
  //increase table_size for NULL value
  //table_size++;
                                                                                                       printf("%d : table size\n", table_size);
  //allocate table
  line = (char **)malloc(sizeof(char*)*table_size);

  for(int j=0; j<table_size; j++){
    line[j] = (char *)malloc(sizeof(char)*max_cell);
  }

  //split char* to char** using delimiter 
  int cell_iterator = 0;
  int line_iterator = 0;

  for(int k=0; arguments[k] != '\0'; k++){
    
    if(arguments[k] != delimiter || arguments[k] != '\n'){
      line[line_iterator][cell_iterator] = arguments[k];
      cell_iterator++;
    } else {
      line[line_iterator][cell_iterator] = '\0';

      while(arguments[k] == delimiter && arguments[k] != '\0'){
        k++;
      }
      k--;
      if(arguments[k+1] != '\0'){
        line_iterator++;                                                                                  rypie sie iterator
      }
      cell_iterator = 0;
    }
  } 

  line[line_iterator + 1] = NULL;
                                                                 printf("%s : ostatnie w returnie; %d : iterator\n", line[line_iterator + 1], line_iterator);
  //return table of char*
  return line;
}

char** read_line(char **args){

  //declare buffer
  char *buffer = NULL;
  ssize_t buf_size = 0;

  //get commands
  getline(&buffer, &buf_size, stdin);
                                                                                                      printf("tutaj przeszło to:%s\n", buffer);
  //return splited commands
  return split_arguments(buffer, args);
}

//function to exit 
void my_exit(){
  printf("Bye Bye!\n");
  exit(0);
}

//execute commands
void execute(char **arguments){
  char ex[4] = {'e', 'x', 'i', 't'};
                                                                                                  printf("może tutuaj?\n");
  int isExit = strcmp(arguments[0], ex);
  if(isExit == 0){ 
    my_exit();
  }else if(0){
    //here cd implementation
  }else{

    //printf("%s", arguments[0]);
  }
}

int main(int argc, char **argv){

  char **arguments;
  while(true){

    arguments = read_line(arguments);
    execute(arguments);
    memclear(arguments);
    printf("\n");
  }

}
