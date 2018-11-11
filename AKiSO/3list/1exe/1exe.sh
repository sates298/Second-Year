#!/bin/bash

data_to_speed=0
round=0

raw1=("" "" "" "" "" "" "" "" "" )
raw2=("" "" "" "" "" "" "" "" "" )
raw3=("" "" "" "" "" "" "" "" "" )
raw4=("" "" "" "" "" "" "" "" "" )
raw5=("" "" "" "" "" "" "" "" "" )
raw6=("" "" "" "" "" "" "" "" "" )
raw7=("" "" "" "" "" "" "" "" "" )
raw8=("" "" "" "" "" "" "" "" "" )
raw9=("" "" "" "" "" "" "" "" "" )
raw10=("" "" "" "" "" "" "" "" "" )

point="\e[42;32m .\e[0m"

function push_forward
{
  raw1[9]=${raw1[8]}; raw1[8]=${raw1[7]}; raw1[7]=${raw1[6]}; raw1[6]=${raw1[5]}; raw1[5]=${raw1[4]}; raw1[4]=${raw1[3]}; raw1[3]=${raw1[2]}; raw1[2]=${raw1[1]}; raw1[1]=${raw1[0]}
  #raw4[0]=`echo -e ${point}`
  raw2[9]=${raw2[8]}; raw2[8]=${raw2[7]}; raw2[7]=${raw2[6]}; raw2[6]=${raw2[5]}; raw2[5]=${raw2[4]}; raw2[4]=${raw2[3]}; raw2[3]=${raw2[2]}; raw2[2]=${raw2[1]}; raw2[1]=${raw2[0]}
  raw3[9]=${raw3[8]}; raw3[8]=${raw3[7]}; raw3[7]=${raw3[6]}; raw3[6]=${raw3[5]}; raw3[5]=${raw3[4]}; raw3[4]=${raw3[3]}; raw3[3]=${raw3[2]}; raw3[2]=${raw3[1]}; raw3[1]=${raw3[0]}
  raw4[9]=${raw4[8]}; raw4[8]=${raw4[7]}; raw4[7]=${raw4[6]}; raw4[6]=${raw4[5]}; raw4[5]=${raw4[4]}; raw4[4]=${raw4[3]}; raw4[3]=${raw4[2]}; raw4[2]=${raw4[1]}; raw4[1]=${raw4[0]}
  raw5[9]=${raw5[8]}; raw5[8]=${raw5[7]}; raw5[7]=${raw5[6]}; raw5[6]=${raw5[5]}; raw5[5]=${raw5[4]}; raw5[4]=${raw5[3]}; raw5[3]=${raw5[2]}; raw5[2]=${raw5[1]}; raw5[1]=${raw5[0]}
  raw6[9]=${raw6[8]}; raw6[8]=${raw6[7]}; raw6[7]=${raw6[6]}; raw6[6]=${raw6[5]}; raw6[5]=${raw6[4]}; raw6[4]=${raw6[3]}; raw6[3]=${raw6[2]}; raw6[2]=${raw6[1]}; raw6[1]=${raw6[0]}
  raw7[9]=${raw7[8]}; raw7[8]=${raw7[7]}; raw7[7]=${raw7[6]}; raw7[6]=${raw7[5]}; raw7[5]=${raw7[4]}; raw7[4]=${raw7[3]}; raw7[3]=${raw7[2]}; raw7[2]=${raw7[1]}; raw7[1]=${raw7[0]}
  raw8[9]=${raw8[8]}; raw8[8]=${raw8[7]}; raw8[7]=${raw8[6]}; raw8[6]=${raw8[5]}; raw8[5]=${raw8[4]}; raw8[4]=${raw8[3]}; raw8[3]=${raw8[2]}; raw8[2]=${raw8[1]}; raw8[1]=${raw8[0]}
  raw9[9]=${raw9[8]}; raw9[8]=${raw9[7]}; raw9[7]=${raw9[6]}; raw9[6]=${raw9[5]}; raw9[5]=${raw9[4]}; raw9[4]=${raw9[3]}; raw9[3]=${raw9[2]}; raw9[2]=${raw9[1]}; raw9[1]=${raw9[0]}
  raw10[9]=${raw10[8]}; raw10[8]=${raw10[7]}; raw10[7]=${raw10[6]}; raw10[6]=${raw10[5]}; raw10[5]=${raw10[4]}; raw10[4]=${raw10[3]}; raw10[3]=${raw10[2]}; raw10[2]=${raw10[1]}; raw10[1]=${raw10[0]}
}


function generate_pole()
{
  percent=$(bc <<< $1*100/1)
  if [ $percent -gt 0 ] ; then
      raw1[0]=`echo -e ${point}`
  else
      raw1[0]=`echo "  "` 
  fi
  if [ $percent -gt 10 ] ; then
      raw2[0]=`echo -e ${point}`
  else
      raw2[0]=`echo "  "` 
  fi
  if [ $percent -gt 20 ] ; then
      raw3[0]=`echo -e ${point}`
  else
      raw3[0]=`echo "  "` 
  fi
  if [ $percent -gt 30 ] ; then
      raw4[0]=`echo -e ${point}`
  else
      raw4[0]=`echo "  "`  
  fi
  if [ $percent -gt 40 ] ; then
      raw5[0]=`echo -e ${point}`
  else
      raw5[0]=`echo "  "` 
  fi
  if [ $percent -gt 50 ] ; then
      raw6[0]=`echo -e ${point}`
  else
      raw6[0]=`echo "  "` 
  fi
  if [ $percent -gt 60 ] ; then
      raw7[0]=`echo -e ${point}`
  else
      raw7[0]=`echo "  "` 
  fi
  if [ $percent -gt 70 ] ; then
      raw8[0]=`echo -e ${point}`
  else
      raw8[0]=`echo "  "` 
  fi
  if [ $percent -gt 80 ] ; then
      raw9[0]=`echo -e ${point}`
  else
      raw9[0]=`echo "  "`
  fi
  if [ $percent -gt 90 ] ; then
      raw10[0]=`echo -e ${point}`
  else
      raw10[0]=`echo "  "`
  fi
}
function print_plot
{
  echo "0.9-1.0: ${raw10[*]}"
  echo "0.8-0.9: ${raw9[*]}"
  echo "0.7-0.8: ${raw8[*]}"
  echo "0.6-0.7: ${raw7[*]}"
  echo "0.5-0.6: ${raw6[*]}"
  echo "0.4-0.5: ${raw5[*]}"
  echo "0.3-0.4: ${raw4[*]}"
  echo "0.2-0.3: ${raw3[*]}"
  echo "0.1-0.2: ${raw2[*]}"
  echo "0.0-0.1: ${raw1[*]}"
}


while true
do
  
  round=$(($round + 1))
  data1=`grep "enp0s3" /proc/net/dev`
  first_transmitted=`echo $data1 | awk '{print $"10"}'`
  first_received=`echo $data1 | awk '{print $2}'`

  sleep 1
  clear
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
  echo -e "loadavg: "$loadavg "\n"

  ###############################################################################3
  echo "loadavg plot:"

  
  print_plot
  push_forward
  generate_pole $loadavg


  


done

