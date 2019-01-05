#!/bin/bash

nasm -f elf32 3ex.asm -o 3ex.o
gcc -m32 3ex.o -o 3ex.exe
