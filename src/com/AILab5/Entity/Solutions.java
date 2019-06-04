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

    private static int straightforwardBackJumping (ColorGraph graph,
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
                if (graph.areDisconnected(node, err))
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

    private static ColorGraph graph = null;
    private static int statesScanned = 0;

    public static LabAnswer arcConsistencyForwardChecking (ColorGraph g)
    {
        graph = g;
        statesScanned = 0;
        RangeSet[] domains = new RangeSet[graph.getNumberOfNodes()];
        Arrays.setAll(domains, i -> new RangeSet(graph.getNumberOfColors(), true));
        LabAnswer ans = new LabAnswer();
        final long t0 = System.nanoTime();
        ans.foundSolution = arcConsistencyForwardChecking(0, domains) == -1;
        ans.executionTime = System.nanoTime() - t0;
        ans.statesScanned = statesScanned;
        if (ans.foundSolution)
            ans.coloursUsed = countColorsUsed(graph);
        return ans;
    }
    private static int arcConsistencyForwardChecking (int node,
                                                      RangeSet[] domains)
    {
        statesScanned++;
        final int NODES = graph.getNumberOfNodes();
        int next = node + 1;
        while (next < NODES && graph.getColor(next) != -1)
        {
            next++;
        }
        final int nextNode = next;
        int errNode = node;
        var uncolored = graph.getUnColoredNeighbors(node);
        for (Integer color : domains[node])
        {
            var changes = domainSetColor(node, color, uncolored, domains);
            if (graph.getColor(node) != -1)
            {
                next = nextNode;
                while (next < NODES && graph.getColor(next) != -1)
                {
                    next++;
                }
                if(next == NODES)
                {
                    errNode = -1;
                    break;
                }
                int err = arcConsistencyForwardChecking(next, domains);
                if (err == -1)
                {
                    errNode = -1;
                    break;
                }
                domainUnSetColor(domains, changes);
                if (graph.areDisconnected(err, node))
                {
                    errNode = err;
                    break;
                }
            }
        }
        return errNode;
    }

    private static Pair<ArrayList<Integer>, ArrayList<Pair<Integer, Integer>>>
    domainSetColor (int node,
                    int color,
                    Integer[] uncolored,
                    RangeSet[] domains)
    {
        var nodesChanged = new ArrayList<Integer>();
        var domainsChanged = new ArrayList<Pair<Integer, Integer>>(graph.getNeighborsCount(node));
        var changes = new Pair<>(nodesChanged, domainsChanged);

        if (domainSetFailed(node,
                            color,
                            uncolored,
                            nodesChanged,
                            domainsChanged,
                            domains))
        {
            domainUnSetColor(domains, changes);
        }

        return changes;
    }

    private static boolean domainSetFailed (int node,
                                            int color,
                                            Integer[] uncolored,
                                            ArrayList<Integer> nodesChanged,
                                            ArrayList<Pair<Integer, Integer>> domainsChanged,
                                            RangeSet[] domains)
    {
        boolean failed = false;

        nodesChanged.add(node);
        graph.setColor(node, color);

        for (int neighbor : uncolored)
        {
            if (domains[neighbor].contains(color))
            {
                domainsChanged.add(new Pair<>(neighbor, color));
                domains[neighbor].remove(color);
            }
        }

        for (int neighbor : uncolored)
        {
            if (domains[neighbor].size() == 1)
            {
                statesScanned++;
                if (domainSetFailed(neighbor,
                                    domains[neighbor].iterator().next(),
                                    graph.getUnColoredNeighbors(neighbor),
                                    nodesChanged,
                                    domainsChanged,
                                    domains))
                {
                    failed = true;
                    break;
                }
            }
        }

        return failed;
    }
    private static void domainUnSetColor
            (RangeSet[] domains,
             Pair<ArrayList<Integer>, ArrayList<Pair<Integer, Integer>>> changes)
    {
        var nodesChanged = changes.getFirst();
        var domainsChanged = changes.getSecond();

        for (var p : domainsChanged)
        {
            domains[p.getFirst()].add(p.getSecond());
        }
        domainsChanged.clear();
        for (var n : nodesChanged)
        {
            graph.setColor(n, -1);
        }
    }
}
