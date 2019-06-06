package com.AILab5;

import com.AILab5.Entity.ColorGraph;

import static com.AILab5.CspAlgo.LocalSearch.HybridSearch.hybridSearch;
import static com.AILab5.CspAlgo.LocalSearch.ObjectiveFirst.objectiveFunctionFirstSearch;
import static com.AILab5.CspAlgo.Utility.parseProblem;
import static com.AILab5.CspAlgo.Utility.printResults;

public class Main
{
    public static void main (String[] args)
    {
        final int file_number = 29;
        final int numberOfColors = 17;

        // Read and Parse problem
        boolean[][] _gr = parseProblem(file_number);
        if (_gr == null) return;

        System.out.println("File " + file_number + " - number of nodes: " + _gr.length + "\n");

        ColorGraph graph1 = new ColorGraph(_gr);

        System.out.println("File " + file_number + " - Kempe Chains Local Search test:");
        printResults(hybridSearch(graph1), graph1);

    }
}