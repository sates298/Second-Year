#include <signal.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <sys/wait.h>


//count legth of strings array
int array_len(char **array){

  int size = 0;
  while(array[size] != NULL){
    size++;
  }
                                                                                                                 
  return size;
}

//count length of cell
int cell_len(char *cell){
  int size = 0;
  
  while(cell[size] != '\0'){
    size++;
  }

  return size;
}
//check if is ampersand as last character
bool isAmpers(char **array){

   int table_size = array_len(array);
   int last_cell_size = cell_len(array[table_size - 1]);

  if(array[table_size - 1][last_cell_size - 1] == '&'){
    //delete & from table
    if(last_cell_size == 1){
      array[table_size - 1] = NULL;
    } else if (last_cell_size > 1){
      array[table_size - 1][last_cell_size - 1] = '\0';
    }
    return true;
  }
 
   return false;
}



//free memory used by current arguments
void memclear(char **pointers){

  if(pointers == NULL) return;
  int i = 0;
  while(pointers[i] != NULL){                                            
    free(pointers[i]);
    i++;                                                                                                      
  }

  free(pointers);
                                                                                                     
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
char** split_arguments(char *arguments){
     
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
  //declare table
  char **line;                                                       
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

char** read_line(){

  //declare buffer
  char *buffer = NULL;
  size_t buf_size = 0;

  //get commands
  getline(&buffer, &buf_size, stdin);
                                                         // printf("hej");                                                                            
  //return splited commands
  return split_arguments(buffer);
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//function to exit 
int my_exit(){
  printf("Bye Bye!\n");
  return 0;
}

int my_cd(char **command){

  if(command[1] == NULL){
    printf("need a directory\n");
  } else {
    int state = chdir(command[1]);
    if(state != 0){
      printf("something went wrong!\n");
    } else {
      printf("succes to change directory on %s\n", command[1]);
    }
  }

  return 1;
}

//execute commands
int execute(char **arguments){
                
  if(arguments != NULL){                                                                           
    if(strcmp( arguments[0] , "exit") == 0){ 
      return my_exit();
    }else if( strcmp(arguments[0], "cd") == 0){
      return my_cd(arguments);
    }else{
    //check if parent has to wait for child
      bool isNotWaiting = isAmpers(arguments);

      int status;
    //create child process
      pid_t pid = fork();
      if(pid == 0){
      //execute commands in child process
        status = execvp(arguments[0], arguments);
        if(status == -1){
          printf("something went wrong!!\n");
        }
      }else if (pid < 0){
      //error while forking
        printf("Error\n");
      } else if(!isNotWaiting){
      //waitng for finish 
        int state;
        pid_t wait_pid;
        do{
          wait_pid = waitpid(pid, &state, WUNTRACED);
        }while(!WIFEXITED(state) && !WIFSIGNALED(state));
      } 
      return 1;
    }
  }else{
    printf("no arguments!\n");
  }
  return 1;
}

//main loop;
void run_lsh(){

  char **arguments = NULL;
  int status;
  do{
    printf("> ");
    arguments = read_line();
   
    status = execute(arguments);
    memclear(arguments);
   
  }while(status);

}

int main(int argc, char **argv){

  run_lsh();
  return EXIT_SUCCESS;
}
