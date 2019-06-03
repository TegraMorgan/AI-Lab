package com.AILab5.Entity;

import static com.AILab5.CspAlgo.Utility.countColorsUsed;

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
        boolean success;
        success = greedyFeasibility(graph, ans);

        // Compact colours and find actual amount of colours used
        int coloursUsed = graph.compactColours();

        // Find the smallest colour group
        int minI = findSmallestColour(graph, coloursUsed);

        // Find the nodes in the colour group
        int[] problemNodes = graph.getNodesByColour(minI);
        // Swap the small colour with the last colour
        recolorNodes(graph, minI, -1); // Remove colour from smallest group
        // Move last colour to take place of the colour
        if (minI!=coloursUsed-1)
            recolorNodes(graph, coloursUsed - 1, minI);

        // Decrease colour pool
        coloursUsed--;
        for (int emptyNode : problemNodes)
            graph.setColor(emptyNode, graph.findMinViolationsColour(emptyNode, coloursUsed));

        return success;
    }

    /**
     * Find vertexes of one color and recolor them to a new color
     *
     * @param graph
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
     * @param graph
     * @param coloursUsed
     * @return The colour that has the least vertexes
     */
    private static int findSmallestColour (ColorGraph graph, int coloursUsed)
    {
        int min = graph.getNumberOfNodes(), minI = -1, t2;
        for (int i = 0; i < coloursUsed; i++)
        {
            t2 = graph.colorCount(i);
            if (t2 < min && t2 != 0)
            {
                min = t2;
                minI = i;
            }
        }
        return minI;
    }

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
