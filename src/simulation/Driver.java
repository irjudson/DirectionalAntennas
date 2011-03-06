package simulation;

import linear.MaxTotalWeightLP;
import linear.BrendansAlg;

/**
 *
 * @author Kairat Zhubayev
 */
public class Driver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int nodeNumber = 10;
        int seed = 1532;
        double squareSide = 30000;
        int beams = 8;
        
        System.out.println("The number of nodes = " + nodeNumber);
        System.out.println("The square side = " + squareSide);
        System.out.println("The seed = " + seed);

        MaxTotalWeightLP optimal = new MaxTotalWeightLP(nodeNumber, seed, squareSide, beams);
        optimal.run();
        Vertex[] optimalGraph = optimal.getGraph();
        System.out.println("\n\nThe optimal graph is connected = " +
                Utilities.checkForConnectivity(optimalGraph));

        BrendansAlg brendan = new BrendansAlg(nodeNumber, seed, squareSide, beams);
        brendan.run();
        Vertex[] brendanGraph = brendan.getGraph();
        System.out.println("\n\nThe Brendan graph is connected = " +
                Utilities.checkForConnectivity(brendanGraph));

        MSTAlgorithm mst = new MSTAlgorithm(nodeNumber, seed, squareSide, beams);
        mst.buildMinimumSpanningTree();
        mst.constructNewGraph();
        Vertex[] mstGraph = mst.getNewGraph();
        System.out.println("The MST-based graph is connected = " +
                Utilities.checkForConnectivity(mstGraph));        

        int neighborsNumber = 3;
        KNearestNeighborsAlgorithm knn =
                new KNearestNeighborsAlgorithm(nodeNumber, seed, squareSide, neighborsNumber, beams);
        knn.findKNearestNeighbors();
        knn.constructNewGraph();
        Vertex[] knnGraph = knn.getNewGraph();
        System.out.println("The k nearest neighbors-based graph is connected = " +
                Utilities.checkForConnectivity(knnGraph));

        PrimsBasedAlgorithm prims = new PrimsBasedAlgorithm(nodeNumber, seed, squareSide, beams);
        prims.run();
        Vertex[] primsGraph = prims.getGraph();
        System.out.println("The Prim's-based graph is connected = " +
                Utilities.checkForConnectivity(primsGraph));

        DrawRegion draw = new DrawRegion(optimalGraph, squareSide, "The optimal graph topology");
        DrawRegion draw1 = new DrawRegion(brendanGraph, squareSide, "The Brendan graph topology");
        //DrawRegion draw2 = new DrawRegion(mstGraph, squareSide, "The MST-based graph topology");
        //DrawRegion draw3 = new DrawRegion(knnGraph, squareSide, "The k nearest neighbors-based graph topology");
        //DrawRegion draw4 = new DrawRegion(primsGraph, squareSide, "The Prim's-based graph topology");

        System.out.println("\n\nOptimal total = " + optimal.getTotalWeight());
        System.out.println("Brendan total = " + brendan.getTotalWeight());
        System.out.println("MST total = " + mst.getTotalWeight());
        System.out.println("k nearest neighbors total = " + knn.getTotalWeight());
        System.out.println("Prim's-based total = " + prims.getTotalWeight());
    }

}
