#!/bin/bash
cd patches-mcg1 || exit
for file in *; do
  :
  if [ -e "../patches/$file" ]; then
    echo "deleting $file"
    rm "$file"
  fi
done
