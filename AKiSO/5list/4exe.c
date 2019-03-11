#include <stdio.h>
#include <stdlib.h>
//#include <time.h>
#include <sys/time.h>

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
      random_number = rand() % 100;      
      matrix[i][j] = random_number;
    }
    //matrix[i][size]='\\0';
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
        printf("---");
      }else if(matrix[i-1][j-1] / 10 == 0) {
        printf("  %d" , matrix[i-1][j-1]);
      }else{
        printf(" %d" , matrix[i-1][j-1]);
      }
    }
    printf("\n");
  }

  printf("\n");
}

int** multiply_matrixes(int** first, int** second){

  int curr_cell = 0, **result, size;
  size = matrix_size(first);
  result = declare_matrix(size);
  
  for(int i=0; i<size; i++){
    for(int j=0; j<size; j++){
      for(int k=0; k<size; k++){
        curr_cell = curr_cell + first[i][k]*second[k][j];
      }
      result[i][j] = curr_cell;
      curr_cell = 0;
    }
  }  
  
  return result;
}

void transpose(int** matrix){

  int size = matrix_size(matrix), value;
  
  for(int i=0; i<size; i++){
    
    for(int j=0; j<i; j++){
      value = matrix[i][j];
      matrix[i][j] = matrix[j][i];
      matrix[j][i] = value;
     
    }
  }

}

int** multiply_transpose(int** first, int** transpose){

  int curr_cell=0, size=matrix_size(first);
  int **result = declare_matrix(size);

  for(int i=0; i<size; i++){
    for(int j=0; j<size; j++){
      for(int k=0; k<size; k++){
        curr_cell = curr_cell + first[i][k]*transpose[j][k];
      }
      result[i][j] = curr_cell;
      curr_cell = 0;
    }
  }
  
  return result;
}



int main(int argc, char** argv){

  //srand(time(NULL));
  struct timeval *current_time = malloc(sizeof(struct timeval));
  time_t sec, first_sec;
  suseconds_t usec, first_usec;
  int size = atoi(argv[1]), **matrix_1, **matrix_2, **result;
  matrix_1 = declare_matrix(size);
  matrix_2 = declare_matrix(size);
  //fill matrixes by random int [0-99]
  fill_matrix(matrix_1);
  fill_matrix(matrix_2);
  //drawing matrixes if they are not too big
  if(size < 50){
    draw(matrix_1, "matrix_1");
    draw(matrix_2, "matrix_2");
  }
  //set current time
  gettimeofday(current_time, NULL);
  sec = current_time->tv_sec;
  usec = current_time->tv_usec;
  //normal multiply
  result = multiply_matrixes(matrix_1, matrix_2);

  //count difference of time in normal multiply
  gettimeofday(current_time, NULL);
  sec = (current_time->tv_sec) - sec;
  if(sec == 0){
    usec = (current_time->tv_usec) - usec;
  }else{
    sec--;
    usec = usec + 1000000;
  }
  first_sec = sec;
  first_usec = usec;
  printf("multiplication time before transpose: \nseconds: %ld, microseconds: %ld \n\n", sec, usec);

  //transpose matrix
  transpose(matrix_2);
  //drawing matrixes if they are not too big
  if(size < 50){
    //draw(matrix_1, "matrix_1");
    draw(matrix_2, "transpose matrix_2");
  }

  //set new current time
  gettimeofday(current_time, NULL);
  sec = current_time->tv_sec;
  usec = current_time->tv_usec;

  free_matrix(result);
  //multiply after transpose
  result = multiply_transpose(matrix_1, matrix_2);
  
  //count difference of time after transpose
  gettimeofday(current_time, NULL);
  sec = (current_time->tv_sec) - sec;
  if(sec == 0){
    usec = (current_time->tv_usec) - usec;
  }else{
    sec--;
    usec = usec + 1000000;
  }

  printf("multiplication time after transpose : \nseconds : %ld, microseconds : %ld \n", sec, usec);


  if(sec > first_sec){
    sec = sec - first_sec;
    if(usec > first_usec){
       usec = usec - first_usec;
    }else if(usec < first_usec){
      sec--;
      usec = usec - first_usec + 1000000;
    }else{
      usec = 0;
    }
    printf("\ndifferences between this two measurements : \nseconds : %ld, microseconds : %ld \nfaster multiplication was %s\n", sec, usec, "before transpose");
  }else if(first_sec > sec){
    first_sec = first_sec - sec;
    if(first_usec > usec){
      first_usec = first_usec - usec;
    }else if(first_usec < usec){
      first_sec--;
      first_usec = first_usec - usec + 1000000;
    }else{
      first_usec = 0;
    }
    printf("\ndifferences between this two measurements : \nseconds : %ld, microseconds : %ld \nfaster multiplication was %s\n", first_sec, first_usec, "after transpose");    
  }else{
    if(usec > first_usec){
      usec = usec - first_usec;
      printf("\ndifferences between this two measurements : \nseconds : 0, microseconds : %ld \nfaster multiplication was %s\n", usec, "before transpose");    
    }else if(first_usec > usec){
      first_usec = first_usec - usec;
      printf("\ndifferences between this two measurements : \nseconds : 0, microseconds : %ld \nfaster multiplication was %s\n", first_usec, "after transpose");    
    }else{
      printf("\ndifferences between this two measurements : \nseconds : 0, microseconds : 0 \nboth were just as fast\n");    
    }
  }


  

  




  free(current_time);
  free_matrix(matrix_1);
  free_matrix(matrix_2);
  free_matrix(result);

  return 0;
}
