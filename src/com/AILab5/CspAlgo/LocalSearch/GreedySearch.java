package com.AILab5.CspAlgo.LocalSearch;

import com.AILab5.Entity.ColorGraph;
import com.AILab5.Entity.LabAnswer;

public class GreedySearch
{
    public static void greedyFeasibleSearch (ColorGraph graph, LabAnswer ans)
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
    }
}
