package simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author Kairat Zhubayev
 */
public class KNearestNeighborsAlgorithm {

    private Vertex array [];        // The array of vertices
    private Vertex newGraph [];     // The array of vertices
    private ArrayList<ListElement> neighbors [];
    // A heap for the Prim's algorithm
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
    private int neighborsNumber;

    // Constructor
    public KNearestNeighborsAlgorithm(int nodeNumber, int seed, double squareSide, int neighborsNumber){
        this.nodeNumber = nodeNumber;        
        this.squareSide = squareSide;
        this.neighborsNumber = neighborsNumber;
        neighbors = new ArrayList[nodeNumber];
        generator = new Random(seed);
        loadGraph();
    }

    /**
     * Randomly distribute points in the square. Calculate all edge weights.
     * If an edge weight >= threshold, add an edge to the actual graph.
     */
    public void loadGraph(){
        // initialize an array
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
                // System.out.println(e);
            }
        }

        // add edges
        for(int i = 0; i < nodeNumber - 1; i++){
            for(int j = i + 1; j < nodeNumber; j++){
                double dist = array[i].point.distance(array[j].point);
                addEdge(i, j, dist, array);
            }
        }
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
        double SNR = power * largeScaleFading / (noisePower * subchannelBandwidth);
        double weight = subchannelBandwidth * MSTAlgorithm.log2(1 + SNR);

        return weight;
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
     * find k nearest neighbors
     */
    public void findKNearestNeighbors(){
        for(int i = 0; i < nodeNumber; i++){
            PriorityQueue<ListElement> curr = new
                    PriorityQueue<ListElement>(array[i].vertices.size(), shortestDistanceComparator);
            // add all neighbors to the priority queue
            for(ListElement e: array[i].vertices)
                curr.add(e);
            // save k nearest neighbors
            neighbors[i] = new ArrayList<ListElement>();
            for(int j = 0; j < neighborsNumber; j++){
                if(!curr.isEmpty())
                    neighbors[i].add(curr.poll());
                else
                    break;
            }
            // check
            if(neighbors[i].size() != neighborsNumber)
                System.out.println("The node " + i + " has only " + 
                        neighbors[i].size() + " nearest neighbors!");

            curr.clear();
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

        // iterate through all k nearest neighbors and activate appropriate beams
        for(int i = 0; i < nodeNumber; i++){
            for(int j = 0; j < neighbors[i].size(); j++){
                int endPoint = neighbors[i].get(j).vertexNumber;
                int index1 = array[i].point.beamIndex(beams, array[endPoint].point);
                newGraph[i].activeBeams[index1] = true;

                int index2 = array[endPoint].point.beamIndex(beams, array[i].point);
                newGraph[endPoint].activeBeams[index2] = true;
            }
        }

        // calculate the number of beams used by each vertex
        for(int i = 0; i < nodeNumber; i++){
            int beamsUsed = 0;
            for(int j = 0; j < beams + 1; j++)
                if(newGraph[i].activeBeams[j])
                    beamsUsed++;

            newGraph[i].beamsUsedNumber = beamsUsed;
            //System.out.println("Node " + i + " uses " + beamsUsed + " beams out of " + beams);
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

    public ArrayList<ListElement> [] getNeighbors(){
        return neighbors;
    }

    /**
     * The comparator is used by the PriorityQueue to determine both object
     * ordering and identity. If the comparator returns that two elements are
     * equal, the queue infers they are the same, and it stores only one
     * instance of the element. To prevent losing nodes with equal shortest
     * distances, we must compare the elements themselves
     * (third block in the if statement above).
     */
    private final Comparator<ListElement> shortestDistanceComparator = new Comparator<ListElement>(){
        public int compare(ListElement left, ListElement right)
        {
            double shortestDistanceLeft = left.cost;
            double shortestDistanceRight = right.cost;

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
