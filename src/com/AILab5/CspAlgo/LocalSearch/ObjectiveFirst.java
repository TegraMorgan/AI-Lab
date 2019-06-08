package com.AILab5.CspAlgo.LocalSearch;

import com.AILab5.Entity.ColorGraph;
import com.AILab5.Entity.LabAnswer;

import java.util.HashMap;
import java.util.Random;

import static com.AILab5.CspAlgo.Utility.countColorsUsed;

public class ObjectiveFirst
{

    /**
     * This method implements tabu, and annealing and does not stop until it reaches the absolute minimum of colours
     *
     * @param graph
     * @return
     */
    public static LabAnswer objectiveFunctionFirstSearch (ColorGraph graph,int persistence)
    {
        final long t0 = System.nanoTime();
        LabAnswer ans = new LabAnswer();

        ans.foundSolution = sequenceFeasibility(graph, ans,persistence);

        ans.executionTime = System.nanoTime() - t0;
        ans.coloursUsed = countColorsUsed(graph);
        return ans;
    }

    /**
     * This method implements tabu, and annealing and does not stop until it reaches the absolute minimum of colours
     *
     * @param graph
     * @return
     */
    public static LabAnswer objectiveFunctionFirstSearch (ColorGraph graph)
    {
        return objectiveFunctionFirstSearch(graph, 10);
    }

    /**
     * Find a graph colouring using a sequence of feasibility problems
     *
     * @param graph Graph to be run on
     * @param ans   LabAnswer to be updated
     * @return True if some solution is found
     */

    private static boolean sequenceFeasibility (ColorGraph graph, LabAnswer ans,final int persistence)
    {

        FeasibilityFirst.greedyFeasibleSearch(graph, ans);
        ColorGraph originalGraph = new ColorGraph(graph), improvedGraph;
        int coloursUsed, attempts = 0;
        boolean improvedColoring;
        do
        {
            // Copy the original graph
            improvedGraph = new ColorGraph(originalGraph);

            // Compact colours and find actual amount of colours used
            coloursUsed = improvedGraph.compactColours();

            // Find the smallest colour group
            int minI = findSmallestColour(improvedGraph, coloursUsed, ans);

            // Find the nodes in the colour group
            int[] problemNodes = improvedGraph.getNodesByColour(minI);
            // Swap the small colour with the last colour
            recolorNodes(improvedGraph, minI, -1); // Remove colour from smallest group
            ans.statesScanned += problemNodes.length;
            // Move last colour to take place of the colour
            if (minI != coloursUsed - 1)
                recolorNodes(improvedGraph, coloursUsed - 1, minI);

            // Decrease colour pool
            coloursUsed--;
            for (int emptyNode : problemNodes)
                improvedGraph.setColor(emptyNode, improvedGraph.findMinViolationsColour(emptyNode, coloursUsed));

            // Run tabucol algorithm
            improvedColoring = tabucol(improvedGraph, coloursUsed, ans);
            if (improvedColoring)
            {
                originalGraph = improvedGraph;
                attempts = 0;
            } else attempts++;
        } while (attempts < persistence);
        graph.copy(originalGraph);
        return graph.countAllViolations() == 0;
    }

    private static boolean tabucol (ColorGraph graph, int maxColors, LabAnswer ans)
    {
        // Setup
        final long MAXIMUM_ITERATIONS = 5000, TABU_TIME = MAXIMUM_ITERATIONS / 2, ALLOWED_ATTEMPTS_TO_FIX_THE_GRAPH = graph.getNumberOfNodes() * maxColors;
        long currentIteration = 0;
        Random r = new Random();
        MoveList tabuList = new MoveList();
        String bestMove = "";

        // while graph invalid or not timeout
        while (graph.countAllViolations() > 0 && currentIteration < MAXIMUM_ITERATIONS)
        {
            int newVertex, newColour;
            int bestImprovement = Integer.MIN_VALUE;
            // make a number of attempts to randomly fix the graph
            for (long attempt = 0; attempt < ALLOWED_ATTEMPTS_TO_FIX_THE_GRAPH; attempt++)
            {
                do
                {
                    // choose random vertex and colour
                    newVertex = r.nextInt(graph.getNumberOfNodes());
                    newColour = r.nextInt(maxColors);
                    ans.statesScanned++;
                }
                // check that those are not tabu
                while (tabuList.isTabu(newVertex, newColour, currentIteration) && (graph.getColor(newVertex) != newColour));

                // measure violations
                final int improvement = graph.countNodeViolations(newVertex) - graph.countPotentialViolations(newVertex, newColour);
                // compare with current best result
                if (improvement > bestImprovement || bestImprovement == Integer.MIN_VALUE)
                {
                    bestImprovement = improvement;
                    bestMove = tabuList.encode(newVertex, newColour);
                }
                if (improvement > 0)
                    attempt = ALLOWED_ATTEMPTS_TO_FIX_THE_GRAPH;
            }

            // Decode the best move found
            int[] moveData = tabuList.decode(bestMove);

            // Apply the best move found
            graph.setColor(moveData[0], moveData[1]);

            // Put best move to Tabu list
            tabuList.add(moveData[0], moveData[1], TABU_TIME);

            currentIteration++;
        }
        return graph.countAllViolations() == 0;
    }

    /**
     * Find vertexes of one color and recolor them to a new color
     *
     * @param graph     Graph to be worked on
     * @param oldColour Original colour
     * @param newColour New colour
     */
    private static void recolorNodes (ColorGraph graph, int oldColour, int newColour)
    {
        int[] nodesToRecolour = graph.getNodesByColour(oldColour);
        for (int n : nodesToRecolour)
            graph.setColor(n, newColour);
    }

    /**
     * Find which colour has the least vertexes colored
     *
     * @param graph       Graph to be worked on
     * @param coloursUsed Amount of colours to scan
     * @param ans         LabAnswer in order to update amount of scanned nodes
     * @return The colour that has the least vertexes
     */
    private static int findSmallestColour (ColorGraph graph, int coloursUsed, LabAnswer ans)
    {
        int min = graph.getNumberOfNodes(), minI = -1, t2;
        for (int i = 0; i < coloursUsed; i++)
        {
            t2 = graph.colorCount(i);
            ans.statesScanned++;
            if (t2 < min && t2 != 0)
            {
                min = t2;
                minI = i;
            }
        }
        return minI;
    }

    private static class MoveList
    {
        HashMap<String, Long> moves = new HashMap<>();

        void add (int node, int colour, Long delay)
        {
            moves.put(encode(node, colour), delay);
        }

        private String encode (int node, int colour)
        {
            return (node) + "|" + (colour);
        }

        private int[] decode (String s)
        {
            String[] s2 = s.split("\\|");
            return new int[]{Integer.parseInt(s2[0]), Integer.parseInt(s2[1])};
        }

        boolean isTabu (int node, int colour, Long step)
        {
            final String key = encode(node, colour);
            final Long val = moves.get(key);
            if (val != null)
            {
                if (val < step)
                {
                    moves.remove(key);
                    return false;
                } else
                {
                    return true;
                }
            }
            return false;
        }
    }
}
