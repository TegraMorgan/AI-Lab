package com.AILab5.CspAlgo.LocalSearch;

import com.AILab5.Entity.ColorGraph;
import com.AILab5.Entity.LabAnswer;

import java.util.HashSet;
import java.util.LinkedList;

import static com.AILab5.CspAlgo.Utility.countColorsUsed;

public class KempeChainsLocalSearch

{
    public static LabAnswer kempeChainsLocalSearch (ColorGraph graph)
    {
        final long t0 = System.nanoTime();
        LabAnswer ans = new LabAnswer();

        ans.foundSolution = startSearch(graph, ans);

        ans.executionTime = System.nanoTime() - t0;
        ans.coloursUsed = countColorsUsed(graph);
        return ans;
    }

    private static boolean startSearch (ColorGraph graph, LabAnswer ans)
    {
        // Setup
        //GreedySearch.greedyFeasibleSearch(graph,ans);
        long bestScore;
        int bestColour;
        final int MAX_COLOURS = graph.getNumberOfColors(), MAX_ITERATIONS = graph.getNumberOfNodes() * graph.getNumberOfColors();
        LinkedList<Integer> neighboursQueue = new LinkedList<>();
        HashSet<Integer> neighboursSet = new HashSet<>();
        int iteration = 0;
        int node = 0;

        // Outer loop
        while (graph.countAllViolations() > 0 && iteration < MAX_ITERATIONS)
        {
            bestScore = Integer.MIN_VALUE;
            bestColour = -1;
            long score;

            // Find best colour
            for (int c = 0; c < MAX_COLOURS; c++)
            {
                ans.statesScanned++;
                graph.setColor(node, c);
                score = fitness(graph);
                if (score > bestScore && graph.countNodeViolations(node) == 0)
                {
                    bestScore = score;
                    bestColour = c;
                }
            }
            // If there were no legal colours to use
            if (bestColour == -1)
            {
                // Get all the neighbours
                int[] nodeNeighbours = graph.getNeighbors(node);
                for (int neighbourIterator : nodeNeighbours)
                {
                    ans.statesScanned++;
                    // If they are not in queue already, remove colour and put back into queue
                    if (!neighboursSet.contains(neighbourIterator))
                    {
                        graph.setColor(neighbourIterator, -1);
                        neighboursSet.add(neighbourIterator);
                        neighboursQueue.add(neighbourIterator);
                    }
                }
                // Now find the best colour to assign (will be the biggest colour so far)
                for (int c = 0; c < MAX_COLOURS; c++)
                {
                    ans.statesScanned++;
                    graph.setColor(node, c);
                    score = fitness(graph);
                    if (score > bestScore)
                    {
                        bestScore = score;
                        bestColour = c;
                    }
                }
            }

            // Finally assign the best colour
            graph.setColor(node, bestColour);

            // Insert the neighbours into queue
            int[] nodeNeighbours = graph.getNeighbors(node);
            for (int neighbourIterator : nodeNeighbours)
                // If they are not in queue already and don't have a colour, add them to queue
                if (!neighboursSet.contains(neighbourIterator) && graph.getColor(neighbourIterator) == -1)
                {
                    neighboursSet.add(neighbourIterator);
                    neighboursQueue.add(neighbourIterator);
                }

            // Advance in queue
            if (neighboursQueue.peek() != null)
            {
                node = neighboursQueue.removeFirst();
                neighboursSet.remove(node);
                iteration++;
            } else
            {
                final int l = graph.getNumberOfNodes();
                for (int i = 0; i < l; i++)
                {
                    if (graph.getColor(i) == -1)
                    {
                        node = i;
                        break;
                    }
                }
            }
        }

        return true;
    }

    private static long fitness (ColorGraph graph)
    {
        long ans = 0;
        final int l = graph.getNumberOfColors();
        for (int i = 0; i < l; i++)
            ans += (Math.pow(graph.colorCount(i), 2));
        return ans;
    }
}
