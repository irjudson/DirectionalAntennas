#!/usr/bin/env bash
#

CPLEXPATH="/Users/judson/Projects/phd/Tools/cplex"

JLPATH="./dist/lib:${CPLEXPATH}/cplex/bin/x86-64_darwin9_gcc4.0:${CPLEXPATH}/lib:${CPLEXPATH}/lib/x86-64_darwin9_gcc4.0"

JAVA_OPTIONS="-Djava.library.path=${JLPATH}"

java $JAVA_OPTIONS -jar dist/DirectionalAntennas.jar
