#!/usr/bin/env bash
#
#$ -N DA
#$ -S /bin/bash
#$ -j y
#$ -cwd 
#$ -r y
#$ -shell yes
#$ -V
#
nodes="$1"
seed="$2"
sectors="$3"

# Test comment
CPLEXPATH="/opt/ibm/ILOG/CPLEX_Studio_Academic123"
CPLEXPATH="/opt/cplex"
CPLEXARCH="x86_sles10_4.1"
CPLEXARCH="x86-64_sles10_4.1"
CPLEXARCH="x86-64_darwin9_gcc4.0"
JLPATH="${CPLEXPATH}/cplex/bin/${CPLEXARCH}:${CPLEXPATH}/lib:${CPLEXPATH}/lib/${CPLEXARCH}"

JAVA_OPTIONS="-Djava.library.path=${JLPATH} -Xmx4096m"
JAVA_OPTIONS="-Djava.library.path=${JLPATH} -Xmx2048m"

CMD="java $JAVA_OPTIONS -jar target/DirectionalAntennas-1.0-jar-with-dependencies.jar"

# Runs excluding the optimal solution
echo "Running with $sectors sectors, $nodes nodes, $seed."
$CMD -b $sectors -n $nodes -s $seed -O
