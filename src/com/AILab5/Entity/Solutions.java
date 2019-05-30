package com.AILab5.Entity;

public class Solutions
{
    public static LabAnswer backJumping(ColorGraph graph)
    {
        final long t0 = System.nanoTime();
        LabAnswer ans = new LabAnswer();
        ans.foundSolution = backJumping(graph, 0, ans) == -1;
        ans.executionTime = System.nanoTime() - t0;
        return ans;
    }
    private static int backJumping(ColorGraph graph, int node, LabAnswer ans)
    {
        ans.nodesScanned++;
        boolean foundSolution = false;
        int errNode = node;
        boolean[] nc = new boolean[graph.getNumberOfColors() + 1];
        for (int i = 0; i < graph.getNeighborsCount(node); i++)
        {
            nc[1 + graph.getColor(graph.getNeighbor(node, i))] = true;
        }
        for (int c = 0; c < graph.getNumberOfColors(); c++)
        {
            if (!nc[1 + c])
            {
                graph.setColor(node, c);
                if (node + 1 == graph.getNumberOfNodes())
                {
                    foundSolution = true;
                    break;
                }
                int err = backJumping(graph, node + 1, ans);
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
