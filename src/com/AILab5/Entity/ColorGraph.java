package com.AILab5.Entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class ColorGraph
{
    private final boolean[][] matrix;
    private final int[][] graph;
    private final int[] nodesColors;
    private final HashSet<Integer>[] colorClasses;

    @SuppressWarnings("unchecked")
    public ColorGraph (int[][] adjacency_matrix, int nColors)
    {
        final int n = adjacency_matrix.length;
        matrix = new boolean[n][n];
        graph = new int[adjacency_matrix.length][];
        nodesColors = new int[n];
        colorClasses = new HashSet[nColors];
        Arrays.setAll(colorClasses, i -> new HashSet<>());
        for (int i = 0; i < n; i++)
        {
            nodesColors[i] = -1;
            int count = 0;
            for (int j = 0; j < n; j++)
            {
                if (adjacency_matrix[i][j] != 0)
                {
                    matrix[i][j] = true;
                    count++;
                }
            }
            graph[i] = new int[count];
            int index = 0;
            for (int j = 0; j < n && index < count; j++)
            {
                if (matrix[i][j])
                {
                    graph[i][index++] = j;
                }
            }
        }
    }

    public int getNeighborsCount (int node)
    {
        return graph[node].length;
    }

    public int getNeighbor (int node, int i)
    {
        return graph[node][i];
    }

    public void foreachNeighbor (int node, IntFunction func)
    {
        for (int n : graph[node])
        {
            func.apply(n);
        }
    }

    public void foreachNeighbor (int node, IntFunction func, Supplier<Boolean> stop)
    {
        for (int n : graph[node])
        {
            if (stop.get())
            {
                break;
            }
            func.apply(n);
        }
    }

    public void foreachNodeInColorClass (int color, IntFunction func)
    {
        for (int node : colorClasses[color])
        {
            func.apply(node);
        }
    }

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

    public int getColor (int node)
    {
        return nodesColors[node];
    }

    public boolean areConnected (int n1, int n2)
    {
        return matrix[n1][n2];
    }

    public int colorCount (int c)
    {
        return colorClasses[c].size();
    }

}
