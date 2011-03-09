#!/usr/bin/env bash
#

CPLEXPATH="/Users/judson/Projects/phd/Tools/cplex"

JLPATH="./dist/lib:${CPLEXPATH}/cplex/bin/x86-64_darwin9_gcc4.0:${CPLEXPATH}/lib:${CPLEXPATH}/lib/x86-64_darwin9_gcc4.0"

JAVA_OPTIONS="-Djava.library.path=${JLPATH}"

CMD="java $JAVA_OPTIONS -jar dist/DirectionalAntennas.jar"

# Runs including the optimal solution
for nodes in 8 10 12 14 16; do
    $CMD -n $nodes
done

# Runs excluding the optimal solution
for sectors in 4 6 8 10 12; do
    for nodes in 10 15 20 25 30; do
        echo "Running with $sectors sectors and $nodes nodes."
        $CMD -b $sectors -n $nodes
    done
done
