package com.AILab5.CspAlgo.LocalSearch;

import com.AILab5.Entity.ColorGraph;
import com.AILab5.Entity.LabAnswer;

import static com.AILab5.CspAlgo.Utility.countColorsUsed;

public class KempeChainsLocalSearch

{
    public static LabAnswer kempeChainsLocalSearch (ColorGraph graph)
    {
        final long t0 = System.nanoTime();
        LabAnswer ans = new LabAnswer();

        ans.foundSolution = startSearch(graph);

        ans.executionTime = System.nanoTime() - t0;
        ans.coloursUsed = countColorsUsed(graph);
        return ans;
    }

    private static boolean startSearch (ColorGraph graph)
    {


        return false;
    }

    long fitness (ColorGraph graph)
    {
        long ans = 0;
        final int l = graph.getNumberOfColors();
        for (int i = 0; i < l; i++)
            ans += (Math.pow(graph.colorCount(i), 2));
        return ans;
    }
}
