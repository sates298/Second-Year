#!/bin/bash

clear
cd /proc/

for p in [0-9]*
do
  process_name=`grep "Name:" $p/status`
  process_ppid=`grep "PPid:" $p/status`
  opened_files=`sudo ls $p/fd | wc -l`
  process_state=`grep "State:" $p/status`
  echo -e "Pid: $p -\t $process_ppid -\t Files Amount: $opened_files -\t $process_state -\t $process_name"


done
