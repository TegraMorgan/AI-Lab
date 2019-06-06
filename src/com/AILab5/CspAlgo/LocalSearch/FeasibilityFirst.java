package com.AILab5.CspAlgo.LocalSearch;

import com.AILab5.Entity.ColorGraph;
import com.AILab5.Entity.LabAnswer;

import static com.AILab5.CspAlgo.Utility.countColorsUsed;

public class FeasibilityFirst
{
    public static boolean greedyFeasibleSearch (ColorGraph graph, LabAnswer ans)
    {
        final int gl = graph.getNumberOfNodes();
        boolean[] ac;
        for (int _nodeI = 0; _nodeI < gl; _nodeI++)
        {
            ans.statesScanned++;
            ac = graph.getAvailableColours(_nodeI);
            for (int _colorI = 0; _colorI < graph.getNumberOfColors() && graph.getColor(_nodeI) == -1; _colorI++)
                if (ac[_colorI])
                    graph.setColor(_nodeI, _colorI);
        }
        return true;
    }

    /**
     * This method is a quick greedy search that stops as soon it reaches some coloring
     * @param graph
     * @return
     */
    public static LabAnswer feasibleFirstSearch (ColorGraph graph)
    {
        final long t0 = System.nanoTime();
        LabAnswer ans = new LabAnswer();


        ans.foundSolution = greedyFeasibleSearch(graph, ans);

        ans.executionTime = System.nanoTime() - t0;
        ans.coloursUsed = countColorsUsed(graph);
        return ans;

    }
}
