#!/usr/bin/env bash
#$ -N DirectionalAntennas_with_optimal
#$ -S /bin/bash
#$ -o job_two.log
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

S="4 6 8 10 12"
N="10 15 20 25 30"
C="1 2 3 4 5 6 7 8 9 10"

# Default 10 nodes, 8 sectors, so we don't run it

# Runs excluding the optimal solution
for sectors in $S; do
    for nodes in $N; do
        if (( ! (("$sectors" == "8") && ("$nodes" == "10")) )); then
#	        for count in $C; do
                echo "Running with $sectors sectors and $nodes nodes."
                $CMD -b $sectors -n $nodes -r
#	        done
        fi
    done
done
