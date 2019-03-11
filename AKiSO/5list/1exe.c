#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>
#include <stdio.h>

int myprintf(char* string, ...);
int myscanf(char* format, ...);


int power(int base, int index){

  int result = 1;
  for(int i=0; i<index; i++){
    result = base*result;
  }
  return result;
}


int count_bits(int number, int system){

  int i=0;

  while(power(system,i) < number){
    i++;
  }

  return i;
}

int string_len(char* string){

	int i=0;
	while(string[i] != '\0'){
		i++;
	}
	return i;
}
/////////////////////////////////////////////////////////////////////start printf///////////////////////////////////////////
char* convert(int number, int system){

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

  //printf("coverted : %s\n", converted);
  if(number <0){
    myprintf("errrrorrrrr!!\n");
  }
  return converted;
}

char* push(char* dest, char* what, int index){

  //printf("%s : string\n %d : index \n", dest, index);
	int dest_size = string_len(dest);
  
	int right_size = dest_size - index - 2;
	char* left = malloc(sizeof(char)*(index + 1 + string_len(what)));
	char* right = malloc(sizeof(char)*(right_size + 1));
//printf("%d : dest_size\n %d : right size\n", dest_size, right_size);
	for(int i=0; i<index; i++){
		left[i] = dest[i];
	}
	left[index] = '\0';


	for(int i=0;i<right_size; i++){
		right[i] = dest[index + 2 + i];
   // printf("right[%d] : %c\n", i, right[i]);
	}
	right[right_size] = '\0';

//printf("czy to stracty?\n");
	strcat(left, what);
	strcat(left, right);
  //printf("a moze fri?\n");
  //printf("right : %p\n", right);
  //free(right);
  free(what);
  //printf("jednak nie fri\n");
	return left;
}

char* put_in(char* dest, char* what, char type){

	int i=0;
	while(dest[i] != '%' || dest[i+1] != type){
		if(dest[i+1] == '\0'){
			myprintf("ERROR!!!");
			return NULL;
		}
		i++;
	}

	dest = push(dest, what, i);

  return dest;
}


//unused function
char* replace_string(char* string, char* characters){

	string = put_in(string, characters, 's');

  return string;
}

char* replace(char* string, void* what, char type){

  char* converted;
  switch(type){
    case 's':
      converted = ((char *)what);
      break;
    case 'x':
      converted = convert(*((int *)what), 16);
      break;
    case 'd':
      converted = convert(*((int *)what), 10);
      break;
    case 'b':
      converted = convert(*((int *)what), 2);
      break;
    default:
      break;
  }

  return put_in(string, converted, type);
}

char* check_string(char* string, char* arguments){

  char* s;
  
  int i=0, d, x, b;
  while(string[i+1] != '\0'){

    //if(string[i+1] != '\0'){
      if(string[i] == '%'){
	      switch(string[i+1]){
	        case 's':
            //s = va_arg(arguments, char*);
						
            s = *((char **) arguments);
            arguments += sizeof(char *);
 	          string = replace(string, s, 's');
	          break;
          case 'd':
            //d = va_arg(arguments, int);
            d = *((int *) arguments);
            arguments += sizeof(int);
            string = replace(string, &d, 'd');
            break;
          case 'x':
             //x = va_arg(arguments, int);
            x = *((int *) arguments);
            arguments += sizeof(int);
            string = replace(string, &x, 'x');
            break;
          case 'b':
            //b = va_arg(arguments, int);
            b = *((int *) arguments);
            arguments += sizeof(int);
            string = replace(string, &b, 'b');
            break;
          default:
            break;
	      }
      }
    //}
    i++;
  }
  return string;
}


///////////////////////////////////////////////////////////////////endprintf////////////////////
///////////////////////////////////////////////////////////////////startscanf/////////////////////////////////

size_t allocated_size(void * pointer){
  return ((size_t*)pointer)[-1];

}

int deconvert(char *string, int system){
  int number = 0;

  switch(system){
    case 2:
      break;
    case 10:
      break;
    case 16:
      break;
    default:
      break;
  }
  return number;
}

int check_format(char *format, va_list arguments){


  char **s, **temp_x = malloc(sizeof(char *)), **temp_b = malloc(sizeof(char *)), **temp_d = malloc(sizeof(char *));
  int i=0, *d,*x, *b, result = 0, chars_size;
  while(format[i+1] != '\0'){
    if(format[i] == '%'){
      switch(format[i+1]){
        case 's':
          s = va_arg(arguments, char**);
          chars_size = allocated_size(s);
          result = read(0, s, chars_size);
          break;
        case 'x':
          x = va_arg(arguments, int*);
          chars_size = allocated_size(temp_x);
          result = read(0, temp_x, chars_size);
          *x = deconvert(*temp_x, 16);
          break;
        case 'b':
          b = va_arg(arguments, int*);
          chars_size = allocated_size(temp_b);
          result = read(0, temp_b, chars_size);
           *b = deconvert(*temp_b, 2);
          break;
        case 'd':
          d = va_arg(arguments, int*);
          chars_size = allocated_size(temp_d);
          result = read(0, temp_d, chars_size);
          *d = deconvert(*temp_d, 10);
          break;
        default:
          break;
      }
    }
    i++;
  }


  return (result > 0) ? 1 : 0;
}

////////////////////////////////////////////////////////////////endscanf//////////////////////////////////////////////////////////////

int myprintf(char* string, ...){

   // va_list arguments;
  

  //va_start(arguments, string);
  char *arguments = (char *) &string + sizeof string;
  string = check_string(string, arguments);

  int string_size = string_len(string) + 1;

  int status = write(1, string, string_size);

  //va_end(arguments);
  arguments = NULL;
  return (status > 0) ? 1 : 0;
}

int myscanf(char* format, ...){

  va_list arguments;
  va_start(arguments, format);

  int status = check_format(format, arguments);

  va_end(arguments);
  myprintf("%d : status\n", status);
  return (status > 0) ? 1 : 0;
}


int main(int argc, char **argv){
  int test = 12, test1 = 127, test2 = 5 ;
	char string[4] = "hey";
	char string1[13] = "my string :P";

 // myprintf("a = %d, ab = %s, c = %d", 1, "lol",2);
  myprintf("test(%d, d) = %d, test1(%d, x) = %x, test2(%d, b) = %b\n", test, test, test1, test1, test2 , test2);
  myprintf("wielkosc hey : %d, wielkosc my string :P : %d \n", string_len(string), string_len(string1));
	myprintf("here is: hey and another %s , %s\n", string1, "hey2");
  //myscanf("%d", &test);
  //myprintf("%d : test \n", test);
  //myscanf("%s %s", &string, &string1);
  //myprintf("%s + %s \n", string, string1);

  return 0;
}







