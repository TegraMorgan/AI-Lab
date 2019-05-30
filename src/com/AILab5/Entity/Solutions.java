package com.AILab5.Entity;

import java.util.HashSet;

public class Solutions
{

    public static boolean BackJumping (ColorGraph graph)
    {
        boolean foundSolution = false;
        final int COLORS = graph.getNumberOfColors();
        int errNode = -1;
        int currNode = 0;
        while (!foundSolution)
        {
            if (!vertexColored(graph, currNode, COLORS))
            {
                errNode = currNode;
                HashSet<Integer> errorNeigh = graph.getNeighbors(errNode);

            } else
            {
                currNode++;
            }
        }
    }

    private static boolean vertexColored (ColorGraph graph, int currNode, int COLORS)
    {
        HashSet<Integer> neighColours = graph.getNeighboursColoursHash(currNode);
        for (int color = 0; color < COLORS; color++)
        {
            if (!neighColours.contains(color))
            {
                graph.setColor(currNode, color);
                return true;
            }
        }
        return false;
    }

    private static int BackJumping (ColorGraph graph, int node)
    {
        int errNode = node;
        boolean[] nc = new boolean[graph.getNumberOfColors() + 1];
        Integer[] NeighboursColours = graph.getNeighboursColoursArray(node);
        for (int neighbourColor : NeighboursColours)
            nc[1 + neighbourColor] = true;
        for (int c = 0; c < graph.getNumberOfColors(); c++)
        {
            if (!nc[1 + c])
            {
                graph.setColor(node, c);
                if (node + 1 == graph.getNumberOfNodes())
                {
                    return -1;
                }
                int err = BackJumping(graph, node + 1);
                if (err == -1)
                    return -1;
                graph.setColor(node, -1);
                if (!graph.areConnected(node, err))
                {
                    errNode = err;
                    break;
                }
            }
        }
        return errNode;
    }

    public static LabAnswer straightforwardBackJumping(ColorGraph graph)
    {
        final long t0 = System.nanoTime();
        LabAnswer ans = new LabAnswer();
        ans.foundSolution = straightforwardBackJumping(graph, 0, ans) == -1;
        ans.executionTime = System.nanoTime() - t0;
        return ans;
    }
    private static int straightforwardBackJumping(ColorGraph graph, int node,LabAnswer ans)
    {
        ans.statesScanned++;
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
                int err = straightforwardBackJumping(graph, node + 1, ans);
                if (err == -1)
                {
                    foundSolution = true;
                    break;
                }
                graph.setColor(node, -1);
                if (!graph.areConnected(node, err))
                {
                    errNode = err;
                    break;
                }
            }
        }
        return foundSolution ? -1 : errNode;
    }
}
