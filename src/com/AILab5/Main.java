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
        // Run algo to find answer
        boolean solved = backJumping(new ColorGraph(_gr, 5));

        // Output
        if (solved) System.out.println("Solved");
        else System.out.println("Not solved");

    }

}