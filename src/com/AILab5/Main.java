package com.AILab5;

import com.AILab5.Entity.ColorGraph;
import com.AILab5.Entity.Solutions;

public class Main
{
    public static void main (String[] args)
    {

        // Read and Parse problem
        boolean[][] _gr = com.AILab5.CspAlgo.Utility.parseProblem(10);

        // Run algo to find answer
        boolean solved = Solutions.backJumping(new ColorGraph(_gr, 10));

        // Output
        if (solved) System.out.println("Solved");
        else System.out.println("Not solved");

    }

}