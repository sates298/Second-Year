#!/bin/bash

data_to_speed=0
round=0

while true
do
  round=$(($round + 1))
  data1=`grep "enp0s3" /proc/net/dev`
  first_transmitted=`echo $data1 | awk '{print $"10"}'`
  first_received=`echo $data1 | awk '{print $2}'`

  sleep 1

  data2=`grep "enp0s3" /proc/net/dev`
  second_transmitted=`echo $data2 | awk '{print $"10"}'`
  second_received=`echo $data2 | awk '{print $2}'`

  speed=$((($second_transmitted + $second_received ) - ( $first_received + $first_transmitted)))
  unit=B/s
  average_unit=B/s
  ten=1024
  data_to_speed=$((($speed + $data_to_speed)/$round))
  temp_speed=$data_to_speed
  if [ $ten -lt  $speed ]
    then
    speed=$(($speed/$ten))
    unit=kB/s
    if [ $ten -lt $speed ]
      then
      speed=$(($speed/$ten))
      unit=MB/s
    fi
  fi

  if [ $ten -lt $temp_speed ]
    then
    temp_speed=$(($temp_speed/$ten))
    average_unit=kB/s
    if [ $ten -lt $temp_speed ]
      then
      temp_speed=$(($temp_speed/$ten))
      average_unit=MB/s
    fi
  fi

  echo "actual network speed = $speed $unit"
  echo "average network speed = $temp_speed $average_unit"
  #############################################################################

  seconds=`echo $(cat /proc/uptime) |  awk '{print $1}'`
  seconds=$(bc<<<$seconds/1)

  d=$(($seconds/86400))
  seconds=$(($seconds - ($d*86400)))
  h=$(($seconds/3600))
  seconds=$(($seconds - ($h*3600)))
  m=$(($seconds/60))
  seconds=$(($seconds - ($m*60)))

  echo -e "uptime: $d days, $h hours, $m minutes, $seconds seconds "
  ###############################################################################

  percent=`grep "POWER_SUPPLY_CAPACITY=" /sys/class/power_supply/BAT0/uevent | cut -c 23- `
  echo -e "battery: "$percent"%"

  ###############################################################################

  loadavg=`echo $(cat /proc/loadavg) | awk '{print $1}'`
  loadavg=$(bc<<<$loadavg*100)
  echo -e "loadavg: "$loadavg "%\n"



done

