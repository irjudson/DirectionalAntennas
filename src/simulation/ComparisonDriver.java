package simulation;

import java.util.Random;
import linear.MaxTotalWeightLP;

/**
 *
 * @author Kairat Zhubayev
 */
public class ComparisonDriver {
    public static void main(String[] args){
        int nodeNumber = 10;
        double squareSide = 30000;
        int beams = 8;
        int neighborsNumber = 3;

        int size = 10;
        int[] seeds = new int[size];
        double[] mstTotal = new double[size];
        double[] knnTotal = new double[size];
        double[] optimalTotal = new double[size];

        int counter = 0;
        Random random = new Random();
        while(counter < size){
            int seed = random.nextInt(1000000) + 1;
            
            MaxTotalWeightLP gr = new MaxTotalWeightLP(nodeNumber, seed, squareSide, beams);
            gr.run();
            Vertex[] topologyGraph = gr.getGraph();

            MSTAlgorithm mst = new MSTAlgorithm(nodeNumber, seed, squareSide, beams);
            mst.buildMinimumSpanningTree();
            mst.constructNewGraph();
            Vertex[] mstGraph = mst.getNewGraph();

            KNearestNeighborsAlgorithm knn =
                new KNearestNeighborsAlgorithm(nodeNumber, seed, squareSide, neighborsNumber, beams);
            knn.findKNearestNeighbors();
            knn.constructNewGraph();
            Vertex[] knnGraph = knn.getNewGraph();

            if(Utilities.checkForConnectivity(topologyGraph) && 
                    Utilities.checkForConnectivity(mstGraph) &&
                    Utilities.checkForConnectivity(knnGraph)){
                seeds[counter] = seed;                
                optimalTotal[counter] = gr.getTotalWeight();
                mstTotal[counter] = mst.getTotalWeight();
                knnTotal[counter] = knn.getTotalWeight();

                counter++;
            }            
        }

        System.out.println("\n\nThe number of nodes = " + nodeNumber);
        System.out.println("The square side = " + squareSide);

        System.out.println("\n\nSeed\t  optimal total\tmst total\tknn total");
        for(int i = 0; i < size; i++){
            System.out.println(seeds[i] + "\t" + optimalTotal[i] + "\t\t" +
                    mstTotal[i] + "\t" + knnTotal[i]);
        }

        // get average
        double optimalAverage = 0;
        double mstAverage = 0;
        double knnAverage = 0;
        for(int i = 0; i < size; i++){
            optimalAverage += optimalTotal[i];
            mstAverage += mstTotal[i];
            knnAverage += knnTotal[i];
        }
        optimalAverage /= size;
        mstAverage /= size;
        knnAverage /= size;

        System.out.println("The optimal algorithm average = " + optimalAverage);
        System.out.println("The MST-based algorithm average = " + mstAverage);
        System.out.println("The k nearest neighbors-based algorithm average = " + knnAverage);
    }
}
