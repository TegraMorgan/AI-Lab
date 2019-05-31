package com.AILab5.Entity;

import static com.AILab5.CspAlgo.Utility.isDeadEnd;

public class Solutions
{

    public static LabAnswer advancedBackJumping (ColorGraph graph)
    {
        final long t0 = System.nanoTime();
        LabAnswer ans = new LabAnswer();
        final int COLORS = graph.getNumberOfColors();
        int currNode = 0;
        while (true)
        {
            if (graph.getColor(currNode) == -1 && vertexNotColored(graph, currNode, COLORS))
            {
                int err2;
                do
                {
                    err2 = 0;
                    int[] errorNeigh = graph.getNeighbors(currNode);
                    for (int n : errorNeigh)
                        if (graph.getColor(n) != -1 && n > err2) err2 = n;
                    graph.setColor(err2, -1);
                    ans.statesScanned++;
                } while (vertexNotColored(graph, currNode, COLORS));
                if (err2 < currNode) currNode = err2;
            } else
            {
                currNode++;
                ans.statesScanned++;
            }
            if (currNode == graph.getNumberOfNodes())
            {
                ans.foundSolution = true;
                ans.executionTime = System.nanoTime() - t0;
                return ans;
            }
        }
    }

    private static boolean vertexNotColored (ColorGraph graph, int currNode, int COLORS)
    {
        boolean[] availableColours = graph.getAvailableColours(currNode);
        for (int color = 0; color < COLORS; color++)
            if (availableColours[color])
            {
                graph.setColor(currNode, color);
                return false;
            }
        return true;
    }

    public static LabAnswer straightforwardBackJumping(ColorGraph graph)
    {
        final long t0 = System.nanoTime();
        LabAnswer ans = new LabAnswer();
        ans.foundSolution = straightforwardBackJumping(graph, 0, ans) == -1;
        ans.executionTime = System.nanoTime() - t0;
        return ans;
    }
    private static int straightforwardBackJumping(ColorGraph graph, int node, LabAnswer ans)
    {
        ans.statesScanned++;
        boolean foundSolution = false;
        int errNode = node;
        boolean[] nc = new boolean[graph.getNumberOfColors() + 1];
        for (int i = 0; i < graph.getNeighborsCount(node); i++)
        {
            nc[1 + graph.getColor(graph.getNeighbor(node, i))] = true;
        }
        final int nextNode = node + 1;
        for (int c = 0; c < graph.getNumberOfColors(); c++)
        {
            if (!nc[1 + c])
            {
                graph.setColor(node, c);
                if (nextNode == graph.getNumberOfNodes())
                {
                    foundSolution = true;
                    break;
                }
                int err = straightforwardBackJumping(graph, nextNode, ans);
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
    public static LabAnswer forwardChecking(ColorGraph graph)
    {
        final long t0 = System.nanoTime();
        LabAnswer ans = new LabAnswer();
        ans.foundSolution = forwardChecking(graph, 0, ans);
        ans.executionTime = System.nanoTime() - t0;
        return ans;
    }
    private static boolean forwardChecking(ColorGraph graph, int node, LabAnswer ans)
    {
        ans.statesScanned++;
        boolean foundSolution = false;
        boolean[] nc = new boolean[graph.getNumberOfColors() + 1];
        for (int i = 0; i < graph.getNeighborsCount(node); i++)
        {
            nc[1 + graph.getColor(graph.getNeighbor(node, i))] = true;
        }
        final int nextNode = node + 1;
        for (int c = 0; c < graph.getNumberOfColors(); c++)
        {
            if (!nc[1 + c] && forwardCheck(graph, node, c))
            {
                if (nextNode == graph.getNumberOfNodes() ||
                        forwardChecking(graph, nextNode, ans))
                {
                    foundSolution = true;
                    break;
                }
                graph.setColor(node, -1);
            }
        }
        return foundSolution;
    }
    private static boolean forwardCheck(ColorGraph graph, int node, int color)
    {
        graph.setColor(node, color);
        for (int i = 0; i < graph.getNeighborsCount(node); i++)
        {
            int neighbor = graph.getNeighbor(node, i);
            if (graph.getColor(neighbor) == -1 && isDeadEnd(graph, neighbor))
            {
                graph.setColor(node, -1);
                return false;
            }
        }
        return true;
    }
}
