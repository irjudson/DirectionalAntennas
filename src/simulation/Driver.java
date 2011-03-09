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

        Vertex[] optimalGraph = null;
        MaxTotalWeightLP optimal = null;

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

//        System.out.println("The number of nodes = " + options.nodeNumber);
//        System.out.println("The square side = " + options.squareSide);
//        System.out.println("The seed = " + options.seed);
//        System.out.println("The beams = " + options.beams);
//        System.out.println("The number of neighbors = " + options.neighborsNumber);

        if (options.optimum) {
            optimal = new MaxTotalWeightLP(options.nodeNumber,
                    options.seed, options.squareSide, options.beams);
            optimal.run();
            optimalGraph = optimal.getGraph();
        }

        BrendansAlg brendan = new BrendansAlg(options.nodeNumber, options.seed,
                options.squareSide, options.beams);
        brendan.run();
        Vertex[] brendanGraph = brendan.getGraph();

        MSTAlgorithm mst = new MSTAlgorithm(options.nodeNumber, options.seed,
                options.squareSide, options.beams);
        mst.buildMinimumSpanningTree();
        mst.constructNewGraph();
        Vertex[] mstGraph = mst.getNewGraph();

        int neighborsNumber = 3;
        KNearestNeighborsAlgorithm knn =
                new KNearestNeighborsAlgorithm(options.nodeNumber, options.seed,
                options.squareSide, options.neighborsNumber, options.beams);
        knn.findKNearestNeighbors();
        knn.constructNewGraph();
        Vertex[] knnGraph = knn.getNewGraph();

        PrimsBasedAlgorithm prims = new PrimsBasedAlgorithm(options.nodeNumber,
                options.seed, options.squareSide, options.beams);
        prims.run();
        Vertex[] primsGraph = prims.getGraph();

        if (options.graphs) {
            if (options.optimum) {
                DrawRegion draw = new DrawRegion(optimalGraph,
                        options.squareSide, "The optimal graph topology");
            }
            DrawRegion draw1 = new DrawRegion(brendanGraph,
                    options.squareSide, "The Brendan graph topology");
            DrawRegion draw2 = new DrawRegion(mstGraph,
                    options.squareSide, "The MST-based graph topology");
            DrawRegion draw3 = new DrawRegion(knnGraph,
                    options.squareSide, "The k nearest neighbors-based graph topology");
            DrawRegion draw4 = new DrawRegion(primsGraph,
                    options.squareSide, "The Prim's-based graph topology");
        }

//        if (options.optimum) {
//            System.out.println("Optimal Total: " + optimal.getTotalWeight()
//                    + "\tConnected: "
//                    + Utilities.checkForConnectivity(optimalGraph)
//                    + "\tFairness (in out): "
//                    + Utilities.inFairness(optimalGraph)
//                    + " " + Utilities.outFairness(optimalGraph));
//        }
//        System.out.println("Brendan total:   " + brendan.getTotalWeight()
//                + "\tConnected: "
//                + Utilities.checkForConnectivity(brendanGraph)
//                + "\tFairness (in out): "
//                + Utilities.inFairness(brendanGraph)
//                + " " + Utilities.outFairness(brendanGraph));
//
//        System.out.println("MinSpanT total:  " + mst.getTotalWeight()
//                + "\tConnected: "
//                + Utilities.checkForConnectivity(mstGraph)
//                + "\tFairness (in out): "
//                + Utilities.inFairness(mstGraph)
//                + " " + Utilities.outFairness(mstGraph));
//
//        System.out.println("K-nearest total: " + knn.getTotalWeight()
//                + "\tConnected: "
//                + Utilities.checkForConnectivity(knnGraph)
//                + "\tFairness (in out): "
//                + Utilities.inFairness(knnGraph)
//                + " " + Utilities.outFairness(knnGraph));
//
//        System.out.println("Prim's total:    " + prims.getTotalWeight()
//                + "\tConnected: "
//                + Utilities.checkForConnectivity(primsGraph)
//                + "\tFairness (in out): "
//                + Utilities.inFairness(primsGraph)
//                + " " + Utilities.outFairness(primsGraph));

	// Output in one line
	// Headers first
	String headers = "n\tside\tseed\tsectors\tneighbors";
	headers += "\tOptimal Total\tOptimal Connected\tOptimal Fairness (in)\tOptimal Fairness (out)\t";
	headers += "\tBrendan Total\tBrendan Connected\tBrendan Fairness (in)\tBrendan Fairness (out)\t";
	headers += "\tMinSpanTree Total\tMinSpanTree Connected\tMinSpanTree Fairness (in)\tMinSpanTree Fairness (out)\t";
	headers += "\tK-nearest Total\tK-nearest Connected\tK-nearest Fairness (in)\tK-nearest Fairness (out)\t";
	headers += "\tPrim's Total\tPrim's Connected\tPrim's Fairness (in)\tPrim's Fairness (out)\t";
	System.out.println(headers);
	System.out.println(options.nodeNumber + "\t" 
		         + options.squareSide + "\t" 
			 + options.seed + "\t" 
			 + options.beams + "\t" 
			 + options.neighborsNumber + "\t" 
			 + optimal.getTotalWeight() + "\t" 
			 + Utilities.checkForConnectivity(optimalGraph) + "\t" 
			 + Utilities.inFairness(optimalGraph) + "\t" 
			 + Utilities.outFairness(optimalGraph) + "\t"
			 + brendan.getTotalWeight() + "\t"
			 + Utilities.checkForConnectivity(brendanGraph) + "\t"
			 + Utilities.inFairness(brendanGraph) + "\t"
			 + Utilities.outFairness(brendanGraph) + "\t"
			 + mst.getTotalWeight() + "\t"
			 + Utilities.checkForConnectivity(mstGraph) + "\t"
			 + Utilities.inFairness(mstGraph) + "\t"
			 + Utilities.outFairness(mstGraph) + "\t"
			 + knn.getTotalWeight() + "\t"
			 + Utilities.checkForConnectivity(knnGraph) + "\t"
			 + Utilities.inFairness(knnGraph) + "\t"
			 + Utilities.outFairness(knnGraph) + "\t"
			 + prims.getTotalWeight() + "\t"
			 + Utilities.checkForConnectivity(primsGraph) + "\t"
			 + Utilities.inFairness(primsGraph) + "\t"
			 + Utilities.outFairness(primsGraph));
    }
}
