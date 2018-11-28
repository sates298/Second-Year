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
                                                                                                                 
  return size;
}

//free memory used by current arguments
void memclear(char **pointers){

  int i = 0;
  while(pointers[i] != NULL){                                            
    free(pointers[i]);
    i++;                                                                                                      
  }

  free(pointers);
                                                                                                     
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
char** split_arguments(char *arguments, char **line){
     
                                                                                                   
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
                                                                                                    
  //allocate table   (char **) and (char *)
  if(table_size == 0 || max_cell == 0)
    return NULL;
  line = malloc(sizeof(char*)*(table_size+1));

  for(int j=0; j<table_size; j++){
    line[j] = malloc(sizeof(char)*max_cell);
  }
  
  
  //split char* to char** using delimiter 
  int cell_iterator = 0;
  int line_iterator = 0;

  for(int k=0; arguments[k] != '\0'; k++){

    if(arguments[k] != delimiter){
      if(arguments[k] == '\n'){
        continue;
      }
      line[line_iterator][cell_iterator] = arguments[k];
      cell_iterator++;
      if(arguments[k+1] == '\n' || arguments[k+1] == delimiter || arguments[k+1] == '\0'){         
        line[line_iterator][cell_iterator] = '\0';                                                               
        line_iterator++;
        cell_iterator = 0;
      }
    } else {
      continue;                                                                               
    }
  } 
  //set NULL on the end of table
  line[table_size] = NULL;
                                                                 
  //free buffer
  free(arguments);
  //return table of char*
  return line;
}

char** read_line(char **args){

  //declare buffer
  char *buffer = NULL;
  size_t buf_size = 0;

  //get commands
  getline(&buffer, &buf_size, stdin);
                                                                                                    
  //return splited commands
  return split_arguments(buffer, args);
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//function to exit 
void my_exit(){
  printf("Bye Bye!\n");
  exit(0);
}

void my_cd(char **command){

  
  printf("change directory\n");
}

//execute commands
void execute(char **arguments){
                                                                                           
  if(strcmp( arguments[0] , "exit") == 0){ 
    my_exit();
  }else if( strcmp(arguments[0], "cd") == 0){
    my_cd(arguments);
  }else{

     printf("something else\n");
  }
}

int main(int argc, char **argv){

  char **arguments = NULL;
  while(true){
    printf(">");
    arguments = read_line(arguments);
    if(arguments != NULL){

      execute(arguments);
      memclear(arguments);
      //printf("\n");
    }

  }

}
