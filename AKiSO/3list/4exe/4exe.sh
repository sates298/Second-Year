#!/bin/bash

address=$1
sleep_time=$2

while true
do
  lynx -dump $address > saved_site1
  sleep $sleep_time
  lynx -dump $address > saved_site2

  differences=`diff saved_site1 saved_site2`

  if [[ $differences == "" ]]
    then
      echo no diffrences
    else
      There are differences
      echo $differences
  fi
done
