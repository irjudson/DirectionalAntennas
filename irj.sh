#!/usr/bin/env bash

exec 3< p3.csv

while read -u 3 nodes seed ; do
  ./run.sh $nodes $seed 8
done
