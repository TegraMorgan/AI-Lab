package com.AILab5;

import com.AILab5.Entity.ColorGraph;

import static com.AILab5.CspAlgo.LocalSearch.KempeChainsLocalSearch.kempeChainsLocalSearch;
import static com.AILab5.CspAlgo.Utility.parseProblem;
import static com.AILab5.CspAlgo.Utility.printResults;
import static com.AILab5.Entity.Solutions.*;
import static com.AILab5.CspAlgo.LocalSearch.LocalFeasibleDecrementalSearch.*;

public class Main
{
    public static void main (String[] args)
    {
        final int file_number = 22;
        final int numberOfColors = 17;

        // Read and Parse problem
        boolean[][] _gr = parseProblem(file_number);
        if (_gr == null) return;

        System.out.println("File " + file_number + " - number of nodes: " + _gr.length + "\n");

        ColorGraph graph1 = new ColorGraph(_gr, numberOfColors);
        ColorGraph graph2 = new ColorGraph(_gr);
        ColorGraph graph3 = new ColorGraph(_gr);

        //System.out.println();
        System.out.println("File " + file_number + " - Local Feasible Decremental Search test:");
        printResults(localFeasibleDecrementalSearch(graph2), graph2);

        //System.out.println();

        System.out.println("File " + file_number + " - Kempe Chains Local Search test:");
        printResults(kempeChainsLocalSearch(graph3), graph3);

    }
}