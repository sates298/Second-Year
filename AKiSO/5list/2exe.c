#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>
#include <stdbool.h>

int a=0;
pthread_mutex_t lock;

struct argument {
  bool** first_matrix;
  bool** second_matrix;
  bool** result_matrix;
};

int matrix_size(bool** matrix){

  int size = 0;

  while(matrix[size] != NULL){
    size++;
  }
  return size;
}

void fill_matrix(bool** matrix){
  
  int random_number, size = matrix_size(matrix);
  for(int i=0; i<size; i++){
    for(int j=0; j<size; j++){
      random_number = rand() % 2;      
      matrix[i][j] = random_number;
    }
  }
  matrix[size]=NULL;
}

void free_matrix(bool** pointer){

  int size = matrix_size(pointer);
  for(int i=0; i<size; i++){
    free(pointer[i]);
  }

  free(pointer);

}

bool** declare_matrix(int size){

  bool** matrix = malloc(sizeof(bool *)*(size+1));
  for(int i=0; i<size; i++){
    matrix[i]=malloc(sizeof(bool)*(size + 1));
  }
   
  return matrix;
}

void draw(bool** matrix, char* name){

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
    struct argument *parameters = arg;
		//printf("%p : wskaźnik parameters\n", parameters);
    int size, temp_a;
    bool **matrix, **one_row, **result;
    bool val = false;
		one_row = parameters->first_matrix;
    matrix = parameters->second_matrix;
		//printf("%p : wskaźnik na second w watku\n", matrix);
    result = parameters->result_matrix; 
		//printf("%p : wskaxnik na result w wątku\n", result);  
    size = matrix_size(matrix) ; 
    //printf("%d : size\n" , size);
    //draw( matrix, "w wątku");
    while(a < size){
			pthread_mutex_lock(&lock);
      a++;
      temp_a = a;
			pthread_mutex_unlock(&lock);
			if(temp_a > size) break;
      for(int i=0; i<size; i++){

        for(int j=0; j<size; j++){
          val = (one_row[temp_a-1][j] && matrix[j][i]);		
					if(val) break;
        }      
        result[temp_a-1][i] = val;

        val = false;
      }
			//pthread_mutex_unlock(&lock);
    }
  }
  return NULL;
  pthread_exit(NULL);
}



int main(int argc, char **argv){

  srand(time(NULL));
  bool **first, **second;
  int size, thread_no;
  bool **result;

  size = atoi(argv[1]);
  thread_no = atoi(argv[2]);

  pthread_t tids[thread_no];
  struct argument **rows = malloc(sizeof(struct argument *)*(thread_no));
  result = declare_matrix(size);

  first = declare_matrix(size);
  second = declare_matrix(size);

  fill_matrix(first);
  fill_matrix(second);
  if(size < 50){
    draw(first, "matrix 1");
    draw(second, "matrix 2");
  }
  for(int i=0; i< thread_no; i++){
    struct argument* arg = malloc(sizeof(struct argument));
    arg->first_matrix = first;
    arg->second_matrix = second;
    arg->result_matrix = result;
    rows[i] = arg;
  }
  //rows[thread_no] = NULL;

	pthread_mutex_init(&lock, NULL);

  int i=0;
  while(i < thread_no){
    pthread_create(&tids[i], NULL, count_one_row, rows[i]); 
    i++;
  }

  
 
	
  for(int i=0; i<thread_no; i++){
		pthread_join(tids[i], NULL);
	}
	pthread_mutex_destroy(&lock);
	draw(result, "result");

	for(int i=0; i < thread_no; i++){
    free(rows[i]);
  }
  free(rows);
  free_matrix(first);
  free_matrix(second);
  free_matrix(result);
  return 0;
}


