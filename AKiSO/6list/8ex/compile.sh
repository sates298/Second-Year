#!/bin/bash

nasm -f elf32 8ex.asm -o 8ex.o
gcc -m32 8ex.o -o 8ex.exe
