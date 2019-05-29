package com.AILab5.Entity;

import static com.AILab5.CspAlgo.Utility.isSafe;

public class Solutions
{
    public static boolean backJumping(ColorGraph graph)
    {
        return backJumping(graph, 0) == -1;
    }
    private static int backJumping(ColorGraph graph, int node)
    {
        boolean foundSolution = false;
        int errNode = node;
        for (int c = 0; c < graph.getNumberOfColors(); c++)
        {
            if(isSafe(graph, node, c))
            {
                graph.setColor(node, c);
                if (node + 1 == graph.getNumberOfNodes())
                {
                    foundSolution = true;
                    break;
                }
                int err = backJumping(graph, node + 1);
                if (err == -1)
                {
                    foundSolution = true;
                    break;
                }
                graph.setColor(node, -1);
                if(!graph.areConnected(node, err))
                {
                    errNode = err;
                    break;
                }
            }
        }
        return foundSolution ? -1 : errNode;
    }
}
