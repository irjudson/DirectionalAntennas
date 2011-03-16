#!/usr/bin/env bash
#

# Test comment
CPLEXPATH="/opt/cplex"
CPLEXARCH="x86_-64_darwin9_gcc4.0"
CPLEXARCH="x86_sles10_4.1"
JLPATH="./dist/lib:${CPLEXPATH}/cplex/bin/${CPLEXARCH}:${CPLEXPATH}/lib:${CPLEXPATH}/lib/${CPLEXARCH}"

JAVA_OPTIONS="-Djava.library.path=${JLPATH} -Xmx1024m"

CMD="java $JAVA_OPTIONS -jar dist/DirectionalAntennas.jar"

$CMD -n 10 -b 8 -s 1532 -o

# Runs including the optimal solution
#for nodes in 8 10 12 14 16; do
#    for count in 1 2 3 4 5 6 7 8 9 10; do
#      echo "Running $count with 8 sectors and $nodes nodes, including optimal."
#      $CMD -n $nodes -o -r
#    done
#done

# Runs excluding the optimal solution
#for sectors in 4 6 8 10 12; do
#    for nodes in 10 15 20 25 30; do
#	for count in 1 2 3 4 5 6 7 8 9 10; do
#            echo "Running $count with $sectors sectors and $nodes nodes."
#            $CMD -b $sectors -n $nodes -r
#	done
#    done
#done
