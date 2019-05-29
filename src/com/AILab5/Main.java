package com.AILab5;

import com.AILab5.Entity.ColorGraph;

import static com.AILab5.CspAlgo.Utility.parseProblem;
import static com.AILab5.Entity.Solutions.backJumping;

public class Main
{
    public static void main (String[] args)
    {
        // Read and Parse problem
        boolean[][] _gr = parseProblem(10);
        if (_gr == null) return;
        ColorGraph graph = new ColorGraph(_gr, 10);
        // Run algo to find answer
        boolean solved = backJumping(graph);

        // Output
        if (solved)
        {
            System.out.println("Solved");
            for (int i = 0; i < graph.getNumberOfNodes(); i++)
            {
                System.out.println("Color of node " + (i + 1) + ": " + graph.getColor(i));
            }
        } else System.out.println("Not solved");

    }

}