package com.AILab5.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess")
public class ColorGraph
{
    /**
     * Adjacency matrix
     */
    private final boolean[][] matrix;
    /**
     * Neighbours array
     */
    private final int[][] graph;
    /**
     * Nodes colours
     */
    private final int[] nodesColors;
    /**
     * Set of nodes by colour
     */
    private final RangeSet[] colorClasses;

    public ColorGraph (boolean[][] adjacency_matrix, int nColors)
    {
        final int n = adjacency_matrix.length;
        matrix = new boolean[n][n];
        graph = new int[n][];
        nodesColors = new int[n];
        colorClasses = new RangeSet[nColors];
        Arrays.setAll(colorClasses, i -> new RangeSet(n, false));
        for (int i = 0; i < n; i++)
        {
            nodesColors[i] = -1;
            int count = 0;
            for (int j = 0; j < n; j++)
                if (adjacency_matrix[i][j])
                {
                    matrix[i][j] = true;
                    count++;
                }
            graph[i] = new int[count];
            int index = 0;
            for (int j = 0; j < n && index < count; j++)
                if (matrix[i][j])
                    graph[i][index++] = j;
        }
    }

    public ColorGraph (boolean[][] adjacency_matrix)
    {
        this(adjacency_matrix, adjacency_matrix.length);
    }

    /**
     * Returns number of nodes in the graph
     */
    public int getNumberOfNodes ()
    {
        return matrix.length;
    }

    /**
     * Returns number of possible colors
     */
    public int getNumberOfColors ()
    {
        return colorClasses.length;
    }

    /**
     * Returns number of node neighbors
     */
    public int getNeighborsCount (int node)
    {
        return graph[node].length;
    }

    /**
     * Returns specific neighbor of the node
     */
    public int getNeighbor (int node, int i)
    {
        return graph[node][i];
    }

    public int[] getNeighbors (int node)
    {
        return graph[node];
    }

    /**
     * @return all the neighbors with color = -1
     */
    public Integer[] getUnColoredNeighbors (int node)
    {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < getNeighborsCount(node); i++)
        {
            if (getColor(getNeighbor(node, i)) == -1)
            {
                list.add(getNeighbor(node, i));
            }
        }
        return list.toArray(new Integer[0]);
    }

    /**
     * Returns a boolean array. True is the value is free, false if taken.
     *
     * @param node The node whose neighbours will be checked
     * @return Array of free colors
     */
    public boolean[] getAvailableColours (int node)
    {
        return getAvailableColours(node, colorClasses.length);
    }

    /**
     * Returns a boolean array. True is the value is free, false if taken.
     *
     * @param maxColor Limit colours search space
     * @param node     The node whose neighbours will be checked
     * @return Array of free colors
     */
    public boolean[] getAvailableColours (int node, int maxColor)
    {
        boolean[] res = new boolean[maxColor];
        Arrays.fill(res, true);
        final int neighboursCount = graph[node].length;
        for (int neighbour = 0; neighbour < neighboursCount; neighbour++)
            if (getColor(graph[node][neighbour]) != -1 && getColor(graph[node][neighbour]) < maxColor)
                res[getColor(graph[node][neighbour])] = false;
        return res;
    }

    /**
     * Run a function on each node in specific colour
     */
    public void foreachNodeInColorClass (int color, IntFunction func)
    {
        for (int node : colorClasses[color])
        {
            func.apply(node);
        }
    }

    /**
     * Run a function on each node in specific colour with a stop criterion
     */
    public void foreachNodeInColorClass (int color, IntFunction func, Supplier<Boolean> stop)
    {
        for (int node : colorClasses[color])
        {
            if (stop.get())
            {
                break;
            }
            func.apply(node);
        }
    }

    /**
     * Set a colour to a specific node
     */
    public void setColor (int node, int color)
    {
        if (nodesColors[node] >= 0)
        {
            colorClasses[nodesColors[node]].remove(node);
        }
        if (color >= 0)
        {
            colorClasses[color].add(node);
        }
        nodesColors[node] = color;
    }

    /**
     * Get nodes colour
     */
    public int getColor (int node)
    {
        return nodesColors[node];
    }

    /**
     * Returns true if vertexes are connected with a node
     */
    public boolean areConnected (int n1, int n2)
    {
        return matrix[n1][n2];
    }

    /**
     * Returns number of nodes of specific colour
     */
    public int colorCount (int c)
    {
        return colorClasses[c].size();
    }

    /**
     * Changes the colours in the graph so they will be at the beginning of the array
     *
     * @return return number of active colours
     */
    public int compactColours ()
    {
        int i = 0;
        int j = colorClasses.length - 1;
        while (i < j)
        {
            while (colorClasses[i].size() > 0 && i < j) i++;
            while (colorClasses[j].size() == 0 && i < j) j--;
            if (i < j)
                for (int node : colorClasses[j])
                    this.setColor(node, i);
        }
        return i;
    }

    /**
     * Returns the color from the given set, that will violate graph the least
     *
     * @param node     Vertex that is being checked
     * @param maxColor Number of colours to check
     * @return The best colour to use from the given set
     */
    public int findMinViolationsColour (int node, int maxColor)
    {
        int min = graph.length, minI = -1;
        for (int colour = 0; colour < maxColor; colour++)
        {
            int colorViolations = countPotentialViolations(node, colour);
            if (colorViolations < min)
            {
                min = colorViolations;
                minI = colour;
            }
        }
        return minI;
    }

    public int countAllViolations ()
    {
        final int allNodes = graph.length;
        int violations = 0;
        for (int node = 0; node < allNodes; node++)
            violations += countNodeViolations(node);
        return violations;
    }


    public int countNodeViolations (int node)
    {
        return countPotentialViolations(node, getColor(node));
    }

    /**
     * Count how much violations applying given colour to given vertex will create
     *
     * @param node   Vertex to be checked
     * @param colour Colour to be applied
     * @return Number of violations that will occur if given colour is applied
     */
    public int countPotentialViolations (int node, int colour)
    {
        int violations = 0;
        int[] ne = getNeighbors(node);
        if (colour == -1) return ne.length;
        for (int n : ne)
            if (getColor(n) == colour) violations++;
        return violations;
    }

    public int[] getNodesByColour (int c)
    {
        RangeSet t = colorClasses[c];
        int[] res = new int[t.size()];
        int i = 0;
        for (int cc : t)
        {
            res[i++] = cc;
        }
        return res;
    }

}
