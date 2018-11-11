#!/bin/bash

a_cat=`curl -s --request GET \
 --url 'https://api.thecatapi.com/v1/images/search?foramt=json' \
 --header 'Content-Type: application/json' \
 --header 'x-api-key: d8c21e26-bed0-4d19-be40-2a643c0f3b30' | jq -r '.[0].url' `

wget -q $a_cat -O cat_img

img2txt -f utf8 -W 100 cat_img

chuck=`curl -s https://api.icndb.com/jokes/random | jq '.value.joke'`
echo  $chuck


