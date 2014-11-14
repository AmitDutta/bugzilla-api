#!/bin/sh

cd $1
for file in `ls`
do
 suffix='_tmp'
 tmp=$file$suffix
 sort $file | uniq > $tmp
 rm $file
 mv $tmp $file 
done
