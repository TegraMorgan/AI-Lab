package com.AILab5.CspAlgo.LocalSearch;

import com.AILab5.Entity.ColorGraph;
import com.AILab5.Entity.LabAnswer;

import java.util.Random;

import static com.AILab5.CspAlgo.Utility.countColorsUsed;

public class HybridSearch

{
    public static LabAnswer hybridSearch (ColorGraph graph)
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
        // Constants
        final int MAX_COLOURS = graph.getNumberOfColors(), NUM_NODES = graph.getNumberOfNodes(), MAX_ITERATIONS = NUM_NODES * MAX_COLOURS, MAX_STAGNATION = NUM_NODES*2;
        final Random r = new Random();

        int iteration = 0, node = 0, stagnationCounter = 0, bestColour;

        long bestFitnessOld = Integer.MAX_VALUE, bestFitness, score, bestScore;

        // Search loop
        while (stagnationCounter < MAX_STAGNATION && iteration < MAX_ITERATIONS)
        {
            bestScore = Integer.MAX_VALUE;
            bestColour = -1;
            ans.statesScanned++;

            // Find best colour
            for (int c = 0; c < MAX_COLOURS; c++)
            {
                graph.setColor(node, c);
                score = fitness(graph);
                if (score < bestScore)
                {
                    bestScore = score;
                    bestColour = c;
                }
            }

            // Assign colour
            graph.setColor(node, bestColour);
            bestFitness = bestScore;

            node = (node + 1) % NUM_NODES;
            iteration++;
            if (bestFitnessOld == bestFitness) stagnationCounter++;
            else
            {
                bestFitnessOld = bestFitness;
                stagnationCounter = 0;
            }
        }
        return true;
    }

    private static long fitness (ColorGraph graph)
    {
        long part1 = 0, part2 = 0;
        final int l = graph.getNumberOfColors();
        for (int c = 0; c < l; c++)
        {
            // count bad edges of colour i
            int[] nodes = graph.getNodesByColour(c);
            int violations = 0;
            for (int node : nodes)
            {
                int[] nodeNeighbours = graph.getNeighbors(node);
                for (int neighbour : nodeNeighbours)
                    if (graph.getColor(neighbour) == c) violations++;
            }
            part1 += 2 * violations * graph.colorCount(c);
            part2 += (Math.pow(graph.colorCount(c), 2));
        }
        return part1 - part2;
    }
}
