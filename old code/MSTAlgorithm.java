package simulation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author Kairat Zhubayev
 */
public class MSTAlgorithm {
    
    private Vertex array [];        // The array of vertices
    private Vertex newGraph [];     // The array of vertices
    HashSet<Edge> MST = new HashSet<Edge>();
    // A heap for the Prim's algorithm
    private PriorityQueue<Vertex> unknownNodes;    
    private int nodeNumber;
    private double squareSide;       // 1000 meters
    private double threshold = Math.pow(10, 6);     // 1 Mbps
    private int beams = 4;    
    private double frequency = 2.4 * Math.pow(10, 9);   // 2.4 Ghz
    private double gainReceiver = Math.pow(10, 2.0 / 10);       // 2 dB
    private double gainTransmitter = Math.pow(10, 15.0 / 10);   // 15 dB
    private double transmitPower = 1;   // 1 W
    private double subchannelBandwidth = 0.59 * Math.pow(10, 6);
    private double noisePower = Math.pow(10, -17.4) / 1000;     // -174 dBm/Hz
    private double c = 3.0 * Math.pow(10, 8);
    // N is in dB
    private double N = 10 * Math.log10(noisePower * subchannelBandwidth);
    private Random generator;

    // Constructor
    public MSTAlgorithm(int nodeNumber, int seed, double squareSide){
        this.nodeNumber = nodeNumber;        
        this.squareSide = squareSide;
        generator = new Random(seed);
        unknownNodes = new PriorityQueue<Vertex>(nodeNumber, shortestDistanceComparator);
        loadGraph();
    }
    
    /**
     * Randomly distribute points in the square. Calculate all edge weights.
     * If an edge weight >= threshold, add an edge to the actual graph.
     */
    public void loadGraph(){        
        //initialize an array
        array = new Vertex[nodeNumber];
        HashSet<Point> points = new HashSet<Point>();
        int counter = 0;
        while(counter < nodeNumber){
            // the square is 1km x 1km, with left bottom corner (0, 0)
            double x = generator.nextDouble() * squareSide;
            double y = generator.nextDouble() * squareSide;
            x = Point.roundTwoDecimals(x);
            y = Point.roundTwoDecimals(y);

            Point e = new Point(x, y);
            if(points.add(e)){
                array[counter] = new Vertex(counter, e);
                counter++;
                //System.out.println(e);
            }
        }                    

        // add edges
        //int edgeCount = 0;
        for(int i = 0; i < nodeNumber - 1; i++){
            for(int j = i + 1; j < nodeNumber; j++){              
                double dist = array[i].point.distance(array[j].point);
                addEdge(i, j, dist, array);
//                double weight = calculateWeight(transmitPower, dist);
//                if(weight >= threshold){
//                    addEdge(i, j, weight, array);
//                    edgeCount++;
//                    System.out.println("Edge {" + i + ", " + j + "} weight = " + weight);
//                }
            }
        }

//        System.out.println("The initial graph has " + nodeNumber +
//                " nodes and " + edgeCount + " edges.");
//        System.out.println("|V| * (|V| - 1) / 2 = " +
//                nodeNumber * (nodeNumber - 1) / 2);
//        System.out.println("The square side is " + squareSide);
    }

    /**
     *
     * @param i is a vertex index
     * @param j is a vertex index
     * @param power is a transmit power
     * @param dist is the distance b/n points i and j
     * @return the weight of the edge from i to j
     */
    private double calculateWeight(double power, double dist){
        double lambdaSq = Math.pow(c / frequency, 2);
        double largeScaleFading = gainReceiver * gainTransmitter * lambdaSq;
        largeScaleFading /= Math.pow(4 * Math.PI * dist, 2);
//        // in dB
//        largeScaleFading = 10 * Math.log10(largeScaleFading);
//        // all in dB
//        double powerReceiver = 10 * Math.log10(power) + largeScaleFading;
//        double SNR = powerReceiver - N;
//        // in real numbers
//        SNR = Math.pow(10, SNR / 10);
//        double weight = subchannelBandwidth * log2(1 + SNR);

        double SNR = power * largeScaleFading / (noisePower * subchannelBandwidth);
        double weight = subchannelBandwidth * log2(1 + SNR);
        
        return weight;
    }

    /**
     * log2(x) = log(x) / log(2)
     * @param x is real number
     * @return log2(x)
     */
    public static double log2(double x){
        return Math.log(x) / Math.log(2);
    }
        
    /**
     * add edge between vertices i and j
     * assume graph is not directed
     * @param i is the first vertex
     * @param j is the second vertex
     */
    private void addEdge(int i, int j, double cost, Vertex[] graph){
        if(i < graph.length && j < graph.length && i != j){
            //create a new list element and add it to vertix i's list
            ListElement elem1 = new ListElement(j, cost);
            graph[i].vertices.add(elem1);
            //create a new list element and add it to vertix j's list
            ListElement elem2 = new ListElement(i, cost);
            graph[j].vertices.add(elem2);
        }
    }
    
    
    /**
     * find minimum spanning tree
     * use the Prims's algorithm to solve this problem
     */
    public void buildMinimumSpanningTree(){
        int startVertex = 0;
        System.out.println("The start vertex for MST is " + array[startVertex].point);
        // begin Prim's algorithm
        array[startVertex].distance = 0;
        unknownNodes.add(array[startVertex]);
        
        // while there are some unknown nodes, keep going
        while (!unknownNodes.isEmpty()){
            // get the node with the shortest distance
            Vertex u = extractMin();
            // mark as known
            u.known = true;
            // relax neighbors
            for(ListElement v: u.vertices){
                if(!array[v.vertexNumber].known){
                    // if a shorter distance exists
                    if ((array[v.vertexNumber].distance == -1) || (array[v.vertexNumber].distance > v.cost)){
                        // update a distance (using Prim's algorithm!)
                        array[v.vertexNumber].distance = v.cost;
                        // update a path
                        array[v.vertexNumber].path = u;
                        // add a new vertex to the queue
                        unknownNodes.add(array[v.vertexNumber]);
                    }
                }   
            }
        }
    }
    
    
    /**
     * Iterate through the found MST and "activate" necessary beams. Then
     * calculate new link weights.
     */
    public void constructNewGraph(){
        newGraph = new Vertex[nodeNumber];
        for(int i = 0; i < nodeNumber; i++){
            newGraph[i] = new Vertex(i, array[i].point);
            newGraph[i].activeBeams = new boolean[beams + 1];
        }

        // find all edges of the MST
        HashSet<Edge> edges = new HashSet<Edge>();
        for(int i = 0; i < nodeNumber; i++){
            if(array[i].path != null){
                Edge e = new Edge(i, array[i].path.vertexNumber);
                edges.add(e);
                //System.out.println("MST edge (" + e.endPoint1 + ", " + e.endPoint2 + ")");
            }
        }

        if(edges.size() != nodeNumber - 1){
            System.out.println("Number of nodes = " + nodeNumber +
                    ", number of edges in the MST = " + edges.size());
            throw new RuntimeException("MST must have |V| - 1 edges!");
        }
        MST = edges;

        // iterate through all edges and activate appropriate beams
        Iterator iter = edges.iterator();
        while(iter.hasNext()){
            Edge e = (Edge) iter.next();
            int index1 = array[e.endPoint1].point.beamIndex(beams, array[e.endPoint2].point);
            newGraph[e.endPoint1].activeBeams[index1] = true;

            int index2 = array[e.endPoint2].point.beamIndex(beams, array[e.endPoint1].point);
            newGraph[e.endPoint2].activeBeams[index2] = true;
        }

        // calculate the number of beams used by each vertex
        for(int i = 0; i < nodeNumber; i++){
            int beamsUsed = 0;
            for(int j = 0; j < beams + 1; j++)
                if(newGraph[i].activeBeams[j])
                    beamsUsed++;

            newGraph[i].beamsUsedNumber = beamsUsed;
        }

        // add edges in the new graph
        int edgeCount = 0;
        double minimumWeight = Double.MAX_VALUE;
        for(int i = 0; i < nodeNumber - 1; i++){
            for(int j = i + 1; j < nodeNumber; j++){
                int index1 = newGraph[i].point.beamIndex(beams, newGraph[j].point);
                if(!newGraph[i].activeBeams[index1])
                    continue;
                int index2 = newGraph[j].point.beamIndex(beams, newGraph[i].point);
                if(!newGraph[j].activeBeams[index2])
                    continue;

                double dist = newGraph[i].point.distance(newGraph[j].point);
                double weightItoJ = calculateWeight(transmitPower /
                        newGraph[i].beamsUsedNumber, dist);
                if(weightItoJ < threshold)
                    continue;
                double weightJtoI = calculateWeight(transmitPower /
                        newGraph[j].beamsUsedNumber, dist);
                if(weightJtoI < threshold)
                    continue;

                double weight = Math.min(weightItoJ, weightJtoI);
                addEdge(i, j, weight, newGraph);
                edgeCount++;
                //System.out.println("Edge {" + i + ", " + j + "} weight = " + weight);

                if(minimumWeight > weight)
                    minimumWeight = weight;
            }
        }

        System.out.println("The new graph has " + nodeNumber +
                " nodes and " + edgeCount + " edges.");
        System.out.println("The minimum link weight is " + minimumWeight);
    }

    /**
     *
     * @param graph
     * @return whether graph is connected
     */
    public boolean checkForConnectivity(Vertex[] graph){
        ArrayList<Vertex> vert = new ArrayList<Vertex>();
        vert.add(graph[0]);
        graph[0].known = true;
        int counter = 1;

        // use BFS to find all vertices achievable from vertex 0
        while(!vert.isEmpty()){
            Vertex v = vert.remove(0);
            for(ListElement elem: v.vertices){
                if(!graph[elem.vertexNumber].known){
                    graph[elem.vertexNumber].known = true;
                    counter++;
                    vert.add(graph[elem.vertexNumber]);
                }
            }
        }

        // clean up
        for(int i = 0; i < graph.length; i++)
            graph[i].known = false;

        if(graph.length == counter)
            return true;

        return false;
    }

    public Vertex[] getNewGraph(){
        return newGraph;
    }

    public HashSet<Edge> getMST(){
        return MST;
    }

    /**
     * Find the vertex with minimum distance from unknown vertices
     * @return the head of the queue
     */
    private Vertex extractMin(){
        return unknownNodes.poll();
    }
    
    
    /**
     * The comparator is used by the PriorityQueue to determine both object
     * ordering and identity. If the comparator returns that two elements are
     * equal, the queue infers they are the same, and it stores only one
     * instance of the element. To prevent losing nodes with equal shortest
     * distances, we must compare the elements themselves
     * (third block in the if statement above).
     */
    private final Comparator<Vertex> shortestDistanceComparator = new Comparator<Vertex>(){
        public int compare(Vertex left, Vertex right)
        {
            double shortestDistanceLeft = left.distance;
            double shortestDistanceRight = right.distance;

            if (shortestDistanceLeft > shortestDistanceRight)
            {
                return +1;
            }
            else if (shortestDistanceLeft < shortestDistanceRight)
            {
                return -1;
            }
            else // equal
            {
                return (right.vertexNumber - left.vertexNumber);
            }
        }
    };
                   
}
