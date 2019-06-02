package com.AILab5;

import com.AILab5.Entity.ColorGraph;

import static com.AILab5.CspAlgo.Utility.parseProblem;
import static com.AILab5.CspAlgo.Utility.printResults;
import static com.AILab5.Entity.Solutions.forwardChecking;
import static com.AILab5.Entity.Solutions.straightforwardBackJumping;

public class Main
{
    public static void main (String[] args)
    {
        final int file_number = 29;
        final int numberOfColors = 19;

        // Read and Parse problem
        boolean[][] _gr = parseProblem(file_number);
        if (_gr == null) return;

        System.out.println("File " + file_number + " - number of nodes: " + _gr.length + "\n");

        ColorGraph graph1 = new ColorGraph(_gr, numberOfColors);
        ColorGraph graph3 = new ColorGraph(_gr, numberOfColors);

        System.out.println("File " + file_number + " - Straightforward Back-Jumping test:");
        printResults(straightforwardBackJumping(graph1), graph1);

        System.out.println();

        System.out.println("File " + file_number + " - forwardChecking test:");
        printResults(forwardChecking(graph3), graph3);
    }
}