#!/bin/bash

nasm -f elf32 2ex.asm -o 2ex.o
gcc -m32 2ex.o -o 2ex.exe
