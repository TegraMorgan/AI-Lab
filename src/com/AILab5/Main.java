package com.AILab5;

import com.AILab5.Entity.ColorGraph;
import com.AILab5.Entity.LabAnswer;

import static com.AILab5.CspAlgo.Utility.isSolved;
import static com.AILab5.CspAlgo.Utility.parseProblem;
import static com.AILab5.Entity.Solutions.backJumping;
import static com.AILab5.CspAlgo.Utility.executionTimeString;

public class Main
{
    public static void main (String[] args)
    {
        // Read and Parse problem
        boolean[][] _gr = parseProblem(10);
        if (_gr == null) return;
        ColorGraph graph = new ColorGraph(_gr, 5);
        // Run algo to find answer
        LabAnswer ans = backJumping(graph);

        // Output
        if (ans.foundSolution)
        {
            System.out.println("Solved in: " + executionTimeString(ans.executionTime));
            System.out.println("Nodes scanned: " + ans.nodesScanned);
            System.out.println("Solution check: " + isSolved(graph));
        } else System.out.println("Not solved");
    }
}