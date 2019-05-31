package com.AILab5;

import com.AILab5.Entity.ColorGraph;

import static com.AILab5.Entity.Solutions.*;
import static com.AILab5.CspAlgo.Utility.parseProblem;
import static com.AILab5.CspAlgo.Utility.printResults;

public class Main
{
    public static void main (String[] args)
    {
        final int file_number = 10;
        final int numberOfColors = 5;

        // Read and Parse problem
        boolean[][] _gr = parseProblem(file_number);
        if (_gr == null) return;

        System.out.println("File " + file_number + " - number of nodes: " + _gr.length + "\n");

        ColorGraph graph1 = new ColorGraph(_gr, numberOfColors);
        ColorGraph graph2 = new ColorGraph(_gr, numberOfColors);

        System.out.println("File " + file_number + " - backJumping test:");
        printResults(backJumping(graph1), graph1);

        System.out.println();

        System.out.println("File " + file_number + " - forwardChecking test:");
        printResults(backJumping(graph2), graph2);
    }
}