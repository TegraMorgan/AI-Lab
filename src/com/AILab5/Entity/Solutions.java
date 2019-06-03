package com.AILab5.Entity;


import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static com.AILab5.CspAlgo.Utility.countColorsUsed;
import static com.AILab5.CspAlgo.Utility.isDeadEnd;

public class Solutions
{


    public static LabAnswer straightforwardBackJumping (ColorGraph graph, boolean forwardChecking)
    {
        final long t0 = System.nanoTime();
        LabAnswer ans = new LabAnswer();
        ans.foundSolution = straightforwardBackJumping(graph, 0, ans, forwardChecking) == -1;
        ans.executionTime = System.nanoTime() - t0;
        if (ans.foundSolution)
            ans.coloursUsed = countColorsUsed(graph);
        return ans;
    }
    private static int straightforwardBackJumping(ColorGraph graph,
                                                  int node,
                                                  LabAnswer ans,
                                                  boolean forwardChecking)
    {
        ans.statesScanned++;
        boolean foundSolution = false;
        int errNode = node;
        boolean[] ac = graph.getAvailableColours(node);
        final int nextNode = node + 1;
        for (int c = 0; c < graph.getNumberOfColors(); c++)
        {
            if (isSafeToColor(graph, node, c, ac, forwardChecking))
            {
                graph.setColor(node, c);
                if (nextNode == graph.getNumberOfNodes())
                {
                    foundSolution = true;
                    break;
                }
                int err = straightforwardBackJumping(graph, nextNode, ans, forwardChecking);
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
    private static boolean isSafeToColor (ColorGraph graph,
                                          int node,
                                          int color,
                                          boolean[] ac,
                                          boolean forwardChecking)
    {
        boolean isSafe;
        if (ac[color] && forwardChecking)
        {
            isSafe = true;
            graph.setColor(node, color);
            for (int i = 0; i < graph.getNeighborsCount(node); i++)
            {
                int neighbor = graph.getNeighbor(node, i);
                if (graph.getColor(neighbor) == -1 && isDeadEnd(graph, neighbor))
                {
                    isSafe = false;
                    break;
                }
            }
            graph.setColor(node, -1);
        } else
        {
            isSafe = ac[color];
        }
        return isSafe;
    }

    public static LabAnswer arcConsistencyForwardChecking (ColorGraph graph)
    {
        RangeSet[] domains = new RangeSet[graph.getNumberOfNodes()];
        Arrays.setAll(domains, i -> new RangeSet(graph.getNumberOfColors(), true));
        LabAnswer ans = new LabAnswer();
        final long t0 = System.nanoTime();
        ans.foundSolution = arcConsistencyForwardChecking(graph, 0, ans, domains) == -1;
        ans.executionTime = System.nanoTime() - t0;
        if (ans.foundSolution)
            ans.coloursUsed = countColorsUsed(graph);
        return ans;
    }

    private static int arcConsistencyForwardChecking (ColorGraph graph,
                                                      int node,
                                                      LabAnswer ans,
                                                      RangeSet[] domains)
    {
        if (node == graph.getNumberOfNodes())
        {
            return -1;
        }

        final int nextNode = node + 1;
        if (graph.getColor(node)!= -1)
        {
            return arcConsistencyForwardChecking(graph, nextNode, ans, domains);
        }

        ans.statesScanned++;
        var uncolored = graph.getUnColoredNeighbors(node);
        for (Integer color: domains[node])
        {
            var nodesChanged = new ArrayList<Pair<Integer, Integer>>(graph.getNeighborsCount(node));
            if (domainCheckAndSetColor(graph,
                                       node,
                                       color,
                                       uncolored,
                                       nodesChanged,
                                       domains))
            {
                int err = arcConsistencyForwardChecking(graph, nextNode, ans, domains);
                if (err == -1)
                {
                    return -1;
                }
                for (var p: nodesChanged)
                {
                    domains[p.getFirst()].add(p.getSecond());
                }
                if (!graph.areConnected(err, node))
                {
                    return err;
                }
            }
        }
        return node;
    }

    private static boolean domainCheckAndSetColor (ColorGraph graph,
                                                   int node,
                                                   int color,
                                                   Integer[] uncolored,
                                                   ArrayList<Pair<Integer, Integer>> nodesChanged,
                                                   RangeSet[] domains)
    {
        graph.setColor(node, color);
        for (int neighbor: uncolored)
        {
            if (domains[neighbor].contains(color))
            {
                if (domains[neighbor].size() == 1)
                {
                    graph.setColor(node, -1);
                    for (var p: nodesChanged)
                    {
                        domains[p.getFirst()].add(p.getSecond());
                    }
                    nodesChanged.clear();
                    return false;
                }
                nodesChanged.add(new Pair<>(neighbor, color));
                domains[neighbor].remove(color);
            }
        }
        for (int neighbor: uncolored)
        {
            if (domains[neighbor].size() == 1)
            {
                if (!domainCheckAndSetColor(graph,
                                            neighbor,
                                            domains[neighbor].iterator().next(),
                                            graph.getUnColoredNeighbors(neighbor),
                                            nodesChanged,
                                            domains))
                {
                    graph.setColor(node, -1);
                    return false;
                }
            }
        }

        return true;
    }

    //#region Feasability first

    public static LabAnswer greedyFeasibility (ColorGraph graph)
    {
        final long t0 = System.nanoTime();
        final int gl = graph.getNumberOfNodes();
        LabAnswer ans = new LabAnswer();
        boolean[] ac;
        for (int _nodeI = 0; _nodeI < gl; _nodeI++)
        {
            ans.statesScanned++;
            ac = graph.getAvailableColours(_nodeI);
            for (int _colorI = 0; _colorI < graph.getNumberOfColors() && graph.getColor(_nodeI) == -1; _colorI++)
                if (ac[_colorI])
                    graph.setColor(_nodeI, _colorI);
        }
        ans.executionTime = System.nanoTime() - t0;
        ans.coloursUsed = countColorsUsed(graph);
        ans.foundSolution = true;
        return ans;
    }

    //#endregion
}
