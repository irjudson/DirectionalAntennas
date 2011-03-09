package simulation;

import linear.MaxTotalWeightLP;
import linear.BrendansAlg;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;


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

        CmdLineOptions options = new CmdLineOptions();
        CmdLineParser parser = new CmdLineParser(options);
        parser.setUsageWidth(80);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        System.out.println("The number of nodes = " + options.nodeNumber);
        System.out.println("The square side = " + options.squareSide);
        System.out.println("The seed = " + options.seed);
        System.out.println("The beams = " + options.beams);
        System.out.println("The number of neighbors = " + options.neighborsNumber);
        
        MaxTotalWeightLP optimal = new MaxTotalWeightLP(options.nodeNumber,
                options.seed, options.squareSide, options.beams);
        optimal.run();
        Vertex[] optimalGraph = optimal.getGraph();
        System.out.println("\n\nThe optimal graph is connected = " +
                Utilities.checkForConnectivity(optimalGraph));

        BrendansAlg brendan = new BrendansAlg(options.nodeNumber, options.seed,
                options.squareSide, options.beams);
        brendan.run();
        Vertex[] brendanGraph = brendan.getGraph();
        System.out.println("\n\nThe Brendan graph is connected = " +
                Utilities.checkForConnectivity(brendanGraph));

        MSTAlgorithm mst = new MSTAlgorithm(options.nodeNumber, options.seed,
                options.squareSide, options.beams);
        mst.buildMinimumSpanningTree();
        mst.constructNewGraph();
        Vertex[] mstGraph = mst.getNewGraph();
        System.out.println("The MST-based graph is connected = " +
                Utilities.checkForConnectivity(mstGraph));        

        int neighborsNumber = 3;
        KNearestNeighborsAlgorithm knn =
                new KNearestNeighborsAlgorithm(options.nodeNumber, options.seed,
                options.squareSide, options.neighborsNumber, options.beams);
        knn.findKNearestNeighbors();
        knn.constructNewGraph();
        Vertex[] knnGraph = knn.getNewGraph();
        System.out.println("The k nearest neighbors-based graph is connected = " +
                Utilities.checkForConnectivity(knnGraph));

        PrimsBasedAlgorithm prims = new PrimsBasedAlgorithm(options.nodeNumber,
                options.seed, options.squareSide, options.beams);
        prims.run();
        Vertex[] primsGraph = prims.getGraph();
        System.out.println("The Prim's-based graph is connected = " +
                Utilities.checkForConnectivity(primsGraph));

        if (options.graphs) {
            DrawRegion draw = new DrawRegion(optimalGraph,
                    options.squareSide, "The optimal graph topology");
            DrawRegion draw1 = new DrawRegion(brendanGraph,
                    options.squareSide, "The Brendan graph topology");
            DrawRegion draw2 = new DrawRegion(mstGraph,
                    options.squareSide, "The MST-based graph topology");
            DrawRegion draw3 = new DrawRegion(knnGraph,
                    options.squareSide, "The k nearest neighbors-based graph topology");
            DrawRegion draw4 = new DrawRegion(primsGraph,
                    options.squareSide, "The Prim's-based graph topology");
        }
        System.out.println("\n\nOptimal total = " + optimal.getTotalWeight());
        System.out.println("Brendan total = " + brendan.getTotalWeight());
        System.out.println("MST total = " + mst.getTotalWeight());
        System.out.println("k nearest neighbors total = " + knn.getTotalWeight());
        System.out.println("Prim's-based total = " + prims.getTotalWeight());
    }

}
