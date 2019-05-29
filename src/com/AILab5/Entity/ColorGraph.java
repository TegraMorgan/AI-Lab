package com.AILab5.Entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.IntFunction;
import java.util.function.Supplier;

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
    private final HashSet<Integer>[] colorClasses;

    @SuppressWarnings("unchecked")
    public ColorGraph (boolean[][] adjacency_matrix, int nColors)
    {
        final int n = adjacency_matrix.length;
        matrix = new boolean[n][n];
        graph = new int[n][];
        nodesColors = new int[n];
        colorClasses = new HashSet[nColors];
        Arrays.setAll(colorClasses, i -> new HashSet<>());
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

    /**
     * Returns number of nodes in the graph
     */
    public int getNumberOfNodes()
    {
        return matrix.length;
    }

    /**
     * Returns number of possible colors
     */
    public int getNumberOfColors()
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
     * Returns true is vertexes are connected with a node
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

}
