#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>
#include <stdbool.h>

typedef struct {
  int* row;
  int** matrix;
} argument;

int matrix_size(int** matrix){

  int size = 0;

  while(matrix[size] != NULL){
    size++;
  }
  return size;
}

void fill_matrix(int** matrix){
  
  int random_number, size = matrix_size(matrix);
  for(int i=0; i<size; i++){
    for(int j=0; j<size; j++){
      random_number = rand() % 2;      
      matrix[i][j] = random_number;
    }
  }
  matrix[size]=NULL;
}

void free_matrix(int** pointer){

  int size = matrix_size(pointer);
  for(int i=0; i<size; i++){
    free(pointer[i]);
  }

  free(pointer);

}

int** declare_matrix(int size){

  int** matrix = malloc(sizeof(int*)*(size+1));
  for(int i=0; i<size; i++){
    matrix[i]=malloc(sizeof(int)*(size + 1));
  }
   
  return matrix;
}

void draw(int** matrix, char* name){

  printf("%s :\n", name);
  int size = matrix_size(matrix);
  for(int i=0; i<size+2; i++){
    for(int j=0; j<size+2; j++){
      if(j==size+1 && (i==0 || i==size+1)){
        printf("-+");
      }else if(j==0 && (i==0 || i==size+1)){
        printf("+");
      } else if(j == 0){
        printf("|");
      }else if(j==size+1){
        printf(" |");
      } else if(i == 0 || i==size+1){
        printf("--");
      }else {
        printf(" %d" , matrix[i-1][j-1]);
      }
    }
    printf("\n");
  }

  printf("\n");
}

void *count_one_row(void *arg){

  if(arg != NULL){
    argument *parameters = (argument *) arg;
    int **matrix, *one_row,  size , *result;
    bool val = false;
    matrix = parameters->matrix;   
    size = matrix_size(matrix) ; 
    draw(matrix, "w wątku");
    one_row = parameters->row;
    result = malloc(sizeof(int)*(size+1));
    for(int i=0; i<size; i++){
      printf("%d\n",i);
      for(int j=0; j<size; j++){
        val = (one_row[j] && matrix[i][j]);
        if(val) break;
      }      
      result[i] = (int)val;
      printf("%d : result\n", result[i]);
      val = false;
    }
    
    
    return (void *)result;
  }
  //return NULL;
  pthread_exit(NULL);
}



int main(int argc, char **argv){

  srand(time(NULL));
  int **first, **second, size, thread_no;
  void *result;

  size = atoi(argv[1]);
  thread_no = atoi(argv[2]);

  pthread_t tids[thread_no];
  argument **rows = malloc(sizeof(argument *)*(size + 1));
  result = (void *)malloc(sizeof(int *)*(size+1));  

  first = declare_matrix(size);
  second = declare_matrix(size);
  fill_matrix(first);
  fill_matrix(second);
  if(size < 50){
    draw(first, "matrix 1");
    draw(second, "matrix 2");
  }

  for(int i=0; i<size; i++){
    argument* arg = malloc(sizeof(argument));
    arg->row = first[i];
    arg->matrix = second;
    rows[i] = arg;
  }
  rows[size] = NULL;

  int i=0;
  while(i < size || i < thread_no){
    if(i < size && i < thread_no){
      pthread_create(tids+i, NULL, count_one_row, (void *)(rows+i)); 
    } else if (i < thread_no) {
      pthread_create(tids+i, NULL, count_one_row, NULL);
    }
    i++;
  }


  for(int i=0; i<thread_no; i++){
    pthread_join(tids[i], result+i);
    printf("udalo sie watek %d\n", i);
  }

  draw(result, "result");
 
  for(int i=0; i<size; i++){
    free(rows[i]);
  }
  free(rows);
  free_matrix(first);
  free_matrix(second);
  free_matrix((int **)result);

  pthread_exit(NULL);
  return 0;
}


