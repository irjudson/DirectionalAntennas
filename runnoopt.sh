#$ -N DA
#$ -S /bin/bash
#$ -j y
#$ -cwd

# Test comment
CPLEXPATH="/opt/cplex"
CPLEXARCH="x86-64_darwin9_gcc4.0"
CPLEXARCH="x86_sles10_4.1"
CPLEXARCH="x86-64_sles10_4.1"
JLPATH="./dist/lib:${CPLEXPATH}/cplex/bin/${CPLEXARCH}:${CPLEXPATH}/lib:${CPLEXPATH}/lib/${CPLEXARCH}"

JAVA_OPTIONS="-Djava.library.path=${JLPATH} -Xmx3072m"

CMD="java $JAVA_OPTIONS -jar dist/DirectionalAntennas.jar"

# nodes in 8 10 12 14 16
#for count in 1 2 3 4 5 6 7 8 9 10; do
#    echo "Running $count with 8 sectors and $1 nodes, including optimal."
#    $CMD -n $1 -o -r
#done

# sectors in 4 6 8 10 12
# nodes in 10 15 20 25 30
for count in 1 2 3 4 5 6 7 8 9 10; do
    echo "Running $count with $sectors sectors and $nodes nodes."
    $CMD -b $1 -n $2 -r
done

