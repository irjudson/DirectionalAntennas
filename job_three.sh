#!/usr/bin/env bash
#$ -N DirectionalAntennas_with_optimal
#$ -S /bin/bash
#$ -o job_three.log
#$ -j y
#$ -cwd

# Test comment
CPLEXPATH="/opt/cplex"
CPLEXARCH="x86-64_darwin9_gcc4.0"
CPLEXARCH="x86_sles10_4.1"
CPLEXARCH="x86-64_sles10_4.1"
JLPATH="./dist/lib:${CPLEXPATH}/cplex/bin/${CPLEXARCH}:${CPLEXPATH}/lib:${CPLEXPATH}/lib/${CPLEXARCH}"

JAVA_OPTIONS="-Djava.library.path=${JLPATH} -Xmx4096m"

CMD="java $JAVA_OPTIONS -jar dist/DirectionalAntennas.jar"

# Runs excluding the optimal solution
# Run 1 : 4 6 8 * 10 15 20
# for sectors in 4 6 8; do
#     for nodes in 10 15 20; do
# 	for count in 1 2 3 4 5 6 7 8 9 10; do
#             echo "Running $count with $sectors sectors and $nodes nodes."
#             $CMD -b $sectors -n $nodes -r
# 	done
#     done
# done

# Run 2 : 10 12 * 10 15 20
for sectors in 10 12; do
    for nodes in 10 15 20; do
	for count in 1 2 3 4 5 6 7 8 9 10; do
            echo "Running $count with $sectors sectors and $nodes nodes."
            $CMD -b $sectors -n $nodes -r
	done
    done
done

# Run 3 : 4 6 8 10 12 * 25 30
# for sectors in 4 6 8 10 12; do
#     for nodes in 25 30; do
# 	for count in 1 2 3 4 5 6 7 8 9 10; do
#             echo "Running $count with $sectors sectors and $nodes nodes."
#             $CMD -b $sectors -n $nodes -r
# 	done
#     done
# done
