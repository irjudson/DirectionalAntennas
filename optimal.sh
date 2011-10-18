#!/usr/bin/env bash
#$ -N DirectionalAntennas_with_optimal
#$ -S /bin/bash
#$ -o job_one.log
#$ -j y
#$ -cwd

# Test comment
CPLEXPATH="/opt/cplex"
CPLEXARCH="x86_sles10_4.1"
CPLEXARCH="x86-64_sles10_4.1"
CPLEXARCH="x86-64_darwin9_gcc4.0"
JLPATH="./dist/lib:${CPLEXPATH}/cplex/bin/${CPLEXARCH}:${CPLEXPATH}/lib:${CPLEXPATH}/lib/${CPLEXARCH}"

JAVA_OPTIONS="-Djava.library.path=${JLPATH} -Xmx4096m"

CMD="java $JAVA_OPTIONS -jar target/DirectionalAntennas-1.0-jar-with-dependencies.jar"

# Runs including the optimal solution
for nodes in 8 10 12 14 16; do
    for count in 1 2 3 4 5 6 7 8 9 10; do
      echo "Running $count with 8 sectors and $nodes nodes, including optimal."
      $CMD -n $nodes -o -r
    done
done
