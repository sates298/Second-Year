#include <unistd.h>
#include <stdlib.h> //only for malloc, realloc and free
//#include <stdio.h> //only for checking 

int myscanf(char *format, ...);
int myprintf(char *string, ...);
//printf functions
int write_char(char c);
int write_string(char *string);
int write_all(char *string, char *arguments);
//scanf functions
int read_char(char *dest);
int read_all(char *format, char *arguments);
int put_to_string(char *source, char *dest, int index);
void put_characters(char *line, char *format, char *arguments);
//algorithm function to printf
int power(int base, int index);
int count_bits(int number, int system);
char *convert(int number, int system);
//algorithm function to scanf
void deconvert(int *array, int system, int size, int *dest);
int string_to_ints(char *source, int *dest, int index, int system);


int main(int argc, char *argv[]){
  int test = 12, test1 = 127, test2 = 5 ;
	char string[20] = "hey";
	char string1[13] = "my string :P";

  myprintf("a = %d, ab = %s, c = %d\n", 1, "lol",2);
  myprintf("test(%d, d) = %d, test1(%d, x) = %x, test2(%d, b) = %b\n", test, test, test1, test1, test2 , test2);
	myprintf("here is: hey and another %s , %s\n", string1, "hey2");
  myscanf("%d %b %s", &test, &test1, &string);
  myprintf("%d : test,  %d : test1 b, %s : string x\n", test, test1, string);
  //myscanf("%s %s", &string, &string1);
 // myprintf("%s + %s + %% \n", string, string1);
  return 0;
}

int myscanf(char *format, ...){

  //va_list arguments;

  //va_start(arguments, format);
  char *arguments = (char *) &format + sizeof format;

  int status = read_all(format, arguments);

  //va_end(arguments);
  arguments = NULL;
  return status;
}

int myprintf(char *string, ...){

  // va_list arguments;

  //va_start(arguments, string);
  char *arguments = (char *) &string + sizeof string;

  int status = write_all(string, arguments);

  //va_end(arguments);
  arguments = NULL;
  return status;
}

int write_char(char c){
  return write(1, &c, 1);
}

int write_string(char *string){

  int status=0, i=0, temp=0;
  while(string[i] != '\0'){
    temp = write_char(string[i]);
    if(temp == -1) return temp;
    status += temp;
    i++;
  }

  return status;
}

int write_all(char *string, char *arguments){

  if(string[0] == '\0') return 0;
  int status=0, temp=0, i=0, d, b, x;
  char type, *s;
  while(string[i] != '\0'){
    
    if(string[i] == '%' && string[i+1] != '\0'){
      type = string[i+1];
      switch(type){
        case 's': 
          //s = va_arg(arguments, char *);
          s = *((char **) arguments);
          arguments += sizeof(char *);
          temp = write_string(s);
          if(temp == -1){
            return temp;
          }
          status += temp;
          i += 2;
          break;
        case 'd':
          //d = va_arg(arguments, int);
          d = *((int *) arguments);
          arguments += sizeof(int);
          s = convert(d, 10);
          temp = write_string(s);
          free(s);
          if(temp == -1){
            return temp;
          }
          status += temp;
          i+=2;
          break;
        case 'b':
          //b = va_arg(arguments, int);
          b = *((int *) arguments);
          arguments += sizeof(int);
          s = convert(b,2);
          temp = write_string(s);
          free(s);
          if(temp == -1){
            return temp;
          }
          status += temp;
          i+=2;
          break;
        case 'x':
          //x = va_arg(arguments, int);
          x = *((int *) arguments);
          arguments += sizeof(int);
          s = convert(x,16);
          temp = write_string(s);
          free(s);
          if(temp == -1){
            return temp;
          }
          status += temp;
          i+=2;
          break;
        default :
          break;
      } 
      
    }
    if(string[i] == '\0') break;
    temp = write_char(string[i]);
    if(temp == -1){
      return temp;
    }
    status += temp;
    i++;
  }

 
  return status;
}

int read_char(char *dest){
  return read(0, dest, 1); 
}

int read_all(char *format, char *arguments){

  int status=0, temp=0, read_it=0, malloc_size=64;
  char already_read, *line_read = malloc(sizeof(char)*malloc_size), *temp_alloc;
  temp = read_char(&already_read);

  while(already_read != '\n'){
    
    if(read_it > malloc_size){
      malloc_size += 64;
      if((temp_alloc = realloc(line_read, malloc_size*sizeof(char))) == NULL){
        myprintf("realloc error\n");
      }else{
        line_read = temp_alloc;
      } 
    }
    //if(already_read == '\n') break;
    line_read[read_it] = already_read;
    read_it++;
    if(temp == -1){
      return temp;
    }
    status += temp;
    temp = read_char(&already_read);
  } 
  line_read[read_it] = '\0';
 
  put_characters(line_read, format, arguments);
  free(line_read);
  return status;
}

int put_to_string(char *source, char *dest, int index){
  int i=0;
  while(source[index] == ' ') index++;
  while(source[index] != ' ' && source[index] != '\0'){
    dest[i] = source[index];
    i++;
    index++;
  }
  dest[i] = '\0';
  return index;
}

void put_characters(char *line, char *format, char *arguments){

  int i=0, line_it = 0, *d, *b, *x;
  char *s;

  while(format[i] != '\0'){
    if(format[i+1] == '\0') break;
    if(format[i] == '%'){
      switch(format[i+1]){
        case 's':
          s = *((char **)arguments); 
          arguments += sizeof(char *);
          line_it = put_to_string(line, s, line_it);
          break;
        case 'd':
          d = *((int **)arguments);
          arguments += sizeof(int *);
          line_it = string_to_ints(line, d, line_it, 10);
          break;
        case 'b':
          b = *((int **)arguments);
          arguments += sizeof(int *);
          line_it = string_to_ints(line, b, line_it, 2);
          break;
        case 'x':
          x = *((int **)arguments);
          arguments += sizeof(int *);
          line_it = string_to_ints(line, x, line_it, 16);
          break;
        default:
          break;
      }
    }

    i++;
  }
}

int power(int base, int index){

  int result = 1;
  for(int i=0; i<index; i++){
    result = base*result;
  }
  return result;
}

int count_bits(int number, int system){
  int i=1;
  while(power(system,i) <= number){
    i++;
  }
  return i;
}

char *convert(int number, int system){

  int number_size = count_bits(number, system);
  char* converted = malloc(sizeof(char)*(number_size+1));
  int temp, curr;
  int it = 0;
  while(it < number_size){
    temp =  number/power(system, number_size-(it + 1)); 
   
    if(temp > 9){
      curr = temp - 10;
      converted[it] = curr + 'A';
    }else{
      converted[it] = temp + '0';
    }
    number = number - temp * power(system,number_size - (it + 1));
    it++;
  }

  converted[it] = '\0';

  if(number <0){
    myprintf("errrrorrrrr!!\n");
  }
  return converted;
}

void deconvert(int *array, int system, int size, int *dest){

  *dest = 0;
  for(int i=size-1; i >= 0; i--){
    *dest = *dest + power(system, i)*array[size - (i + 1)];
  }

}

int string_to_ints(char *source, int *dest, int index, int system){

  int size=0;
  while(source[index] == ' ') index++;
  while(source[index+size] != ' ' && source[index+size] != '\0') size++;
  int *number = malloc(sizeof(int)*size);
  
  if(system == 2 || system == 10){
    for(int i=0; i<size; i++){
      number[i] = source[index + i] - '0';
    }
  }else if(system == 16){
    for(int i=0; i< size; i++){
      if(source[index + i] == 'a' || source[index + i] == 'A'){
        number[i] = 10;
      } else if(source[index + i] == 'b' || source[index + i] == 'B'){
        number[i] = 11;
      } else if(source[index + i] == 'c' || source[index + i] == 'C'){
        number[i] = 12;
      } else if(source[index + i] == 'd' || source[index + i] == 'D'){
        number[i] = 13;
      } else if(source[index + i] == 'e' || source[index + i] == 'E'){
        number[i] = 14;
      } else if(source[index + i] == 'f' || source[index + i] == 'F'){
        number[i] = 15;
      } else {
        number[i] = source[index + i] - '0';
      }
    }
  }

  deconvert(number, system, size, dest);
  free(number);
  index += size;
  return index;
}
