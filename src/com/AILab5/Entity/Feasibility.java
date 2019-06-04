package com.AILab5.Entity;

import java.util.*;

import static com.AILab5.CspAlgo.Utility.*;

@SuppressWarnings("WeakerAccess")
public class Feasibility
{
    public static LabAnswer feasibilityMain (ColorGraph graph)
    {
        final long t0 = System.nanoTime();
        LabAnswer ans = new LabAnswer();

        ans.foundSolution = sequenceFeasibility(graph, ans);

        ans.executionTime = System.nanoTime() - t0;
        ans.coloursUsed = countColorsUsed(graph);
        return ans;
    }

    /**
     * Find a graph colouring using a sequence of feasibility problems
     *
     * @param graph Graph to be run on
     * @param ans   LabAnswer to be updated
     * @return True if some solution is found
     */

    private static boolean sequenceFeasibility (ColorGraph graph, LabAnswer ans)
    {
        greedyFeasibility(graph, ans);

        // Compact colours and find actual amount of colours used
        int coloursUsed = graph.compactColours();

        // Find the smallest colour group
        int minI = findSmallestColour(graph, coloursUsed, ans);

        // Find the nodes in the colour group
        int[] problemNodes = graph.getNodesByColour(minI);
        // Swap the small colour with the last colour
        recolorNodes(graph, minI, -1); // Remove colour from smallest group
        ans.statesScanned += problemNodes.length;
        // Move last colour to take place of the colour
        if (minI != coloursUsed - 1)
            recolorNodes(graph, coloursUsed - 1, minI);

        // Decrease colour pool
        coloursUsed--;
        for (int emptyNode : problemNodes)
            graph.setColor(emptyNode, graph.findMinViolationsColour(emptyNode, coloursUsed));

        // Run tabucol algorithm
        return tabucol(graph, coloursUsed, ans);
    }

    private static class MoveList
    {
        HashMap<String, Long> moves = new HashMap<>();

        public void add (int node, int colour, Long delay)
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

        @SuppressWarnings("unused")
        public int[][] bestMoves ()
        {
            Long bestScore = -1L;
            int counter = 0;

            for (Long p : moves.values())
                if (p > bestScore)
                {
                    bestScore = p;
                    counter = 0;
                } else if (p.equals(bestScore)) counter++;

            int[][] res = new int[counter][];
            counter = 0;

            for (Map.Entry<String, Long> p : moves.entrySet())
                if (p.getValue().equals(bestScore))
                    res[counter++] = decode(p.getKey());
            return res;
        }

        public boolean isTabu (int node, int colour, Long step)
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

    public static boolean tabucol (ColorGraph graph, int maxColors, LabAnswer ans)
    {
        // Setup
        final long TABU_TIME = 50, MAXIMUM_ITERATIONS = 5000, ATTEMPTS = graph.getNumberOfNodes() * maxColors;
        long currentIteration = 0;
        Random r = new Random();
        MoveList tabuList = new MoveList();
        String bestMove = "";

        // while graph invalid or not timeout
        while (graph.countAllViolations() > 0 && currentIteration < MAXIMUM_ITERATIONS)
        {
            int newVertex, newColour;
            int best = -1000;
            // make a number of attempts to randomly fix the graph
            for (long attempt = 0; attempt < ATTEMPTS; attempt++)
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
                final int currentViolations = graph.countNodeViolations(newVertex);
                final int newViolations = graph.countPotentialViolations(newVertex, newColour);

                // compare with current best result
                if (currentViolations - newViolations > best || best == -1000)
                {
                    best = currentViolations - newViolations;
                    bestMove = tabuList.encode(newVertex, newColour);
                }
                if (currentViolations - newViolations > 0)
                    attempt = ATTEMPTS;
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
     * @param ans
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

    @SuppressWarnings("UnusedReturnValue")
    public static boolean greedyFeasibility (ColorGraph graph, LabAnswer ans)
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
        return true;
    }
}
