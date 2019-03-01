#!/bin/bash

#--------create FAT--------------------
cd /tmp

dd if=/dev/zero of=fat.img bs=1024 count=100
mkfs.msdos fat.img
mkdir -p /tmp/fs
mount -t msdos fat.img /tmp/fs -o umask=000,loop


#------create VFAT-------------------

#mount -t vfat fat.img /tmp/fs -o umask=000,loop


#-------tasks--------------
echo "Tasks For FAT"

echo "create few files and show it as hexdump -C"
cd /tmp/fs
echo "hello, world" > hello 
echo "something else" > something
echo "hey" > hey
exit
cd /tmp
hexdump -C fat.img | less
echo "now we delete one file, and show it again"
rm something
hexdump -C fat.img |less

