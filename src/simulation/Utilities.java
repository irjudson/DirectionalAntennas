package simulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.io.*;
/**
 *
 * @author Kairat Zhubayev
 */
public class Utilities {
    public Utilities(){}

    /**
     *
     * @param nodeNumber is a number of nodes in the graph
     * @param squareSide is a length of the square side
     * @param seed
     * @return a random graph in the square
     */
    public static Vertex[] buildRandomGraph(int nodeNumber, double squareSide, int seed){
        Vertex[] array = new Vertex[nodeNumber];
        Random generator = generator = new Random(seed);

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

        return array;
    }


    /**
     *
     * @param graph
     * @return whether graph is connected
     */
    public static boolean checkForConnectivity(Vertex[] graph){
        // clean up
        for(int i = 0; i < graph.length; i++){
            graph[i].known = false;
        }

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
        for(int i = 0; i < graph.length; i++){
//            if(!graph[i].known)
//                System.out.println("The vertex " + i + " is not reachable.");
            graph[i].known = false;
        }

        if(graph.length == counter)
            return true;

        return false;
    }

    /**
     * log2(x) = log(x) / log(2)
     * @param x is real number
     * @return log2(x)
     */
    public static double log2(double x){
        return Math.log(x) / Math.log(2);
    }
/*    public static void main(String[] args){
        int seed = 1532;
        double squareSide = 30000;
    Random generator = generator = new Random(seed);
 double x = generator.nextDouble() ;
            double y = generator.nextDouble() * squareSide;
            System.out.println(x);
            System.out.println(y);


            
}*/
}


class FileWrite
{
   public static void main(String args[])
  {
      try{
    // Create file
    FileWriter fstream = new FileWriter("out.txt");
        BufferedWriter out = new BufferedWriter(fstream);

      int seed = 1532;
       double squareSide = 30000;
    Random generator = generator = new Random(seed);
            double x = generator.nextDouble()* squareSide ;
            double y = generator.nextDouble() * squareSide;
            System.out.println(x);
            System.out.println(y);



    out.write(""+x+" "+y+"\n");
   // out.write((int)y);


    //Close the output stream
    out.close();
    }catch (Exception e){//Catch exception if any
      System.err.println("Error: " + e.getMessage());
    }
  }
}
