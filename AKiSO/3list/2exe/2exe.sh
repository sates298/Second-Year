#!/bin/bash

cd /proc/

find -maxdepth 1 -type d | for p in [0-9]*
do printf "pid:"
   printf $p"\t"
   grep 'Name' $p/status

done
