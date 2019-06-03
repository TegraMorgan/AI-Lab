package com.AILab5;

import com.AILab5.Entity.ColorGraph;

import static com.AILab5.CspAlgo.Utility.parseProblem;
import static com.AILab5.CspAlgo.Utility.printResults;
import static com.AILab5.Entity.Solutions.*;
import static com.AILab5.Entity.Feasibility.*;

public class Main
{
    public static void main (String[] args)
    {
        final int file_number = 30;
        final int numberOfColors = 38;

        // Read and Parse problem
        boolean[][] _gr = parseProblem(file_number);
        if (_gr == null) return;

        System.out.println("File " + file_number + " - number of nodes: " + _gr.length + "\n");

        ColorGraph graph1 = new ColorGraph(_gr, numberOfColors);
        ColorGraph graph2 = new ColorGraph(_gr);
        ColorGraph graph3 = new ColorGraph(_gr, numberOfColors);

        //System.out.println("File " + file_number + " - Straightforward Back-Jumping test:");
        printResults(arcConsistencyForwardChecking(graph1), graph1);

        System.out.println();
        System.out.println("File " + file_number + " - Greedy Feasibility test:");
        printResults(feasibilityMain(graph2), graph2);

        System.out.println();

        //System.out.println("File " + file_number + " - forwardChecking test:");
        //printResults(forwardChecking(graph3), graph3);
    }
}