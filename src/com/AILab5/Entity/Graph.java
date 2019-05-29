package com.AILab5.Entity;

import java.util.HashSet;
import java.util.Vector;

@SuppressWarnings("ALL")
public class Graph
{
    boolean[][] Edges;
    int[] vertexesColours;

    public Graph (int gSize)
    {
        Edges = new boolean[gSize][gSize];
        vertexesColours = new int[gSize];
    }

    public void addEdge (int v1, int v2)
    {
        if (testVertexesInput(v1, v2)) return;
        Edges[v1][v2] = true;
        Edges[v2][v1] = true;
    }

    public void removeEdge (int v1, int v2)
    {
        if (testVertexesInput(v1, v2)) return;
        Edges[v1][v2] = false;
        Edges[v2][v1] = false;
    }

    public Vector<Integer> getNeighbours (int v)
    {
        if (testVertexInput(v)) return null;
        Vector<Integer> res = new Vector<>();
        int[] g = new int[Edges.length];
        for (int i = 0; i < Edges.length; i++)
            if (Edges[v][i])
                res.add(i);
        return res;
    }

    public int getColour (int v)
    {
        if (!testVertexInput(v))
            return vertexesColours[v];
        else return -1;
    }

    public HashSet<Integer> getNeighboringColors (int v)
    {
        Vector<Integer> neighbours = getNeighbours(v);
        HashSet<Integer> res = new HashSet<>();
        for (Integer i : neighbours)
            res.add(vertexesColours[i]);
        return res;
    }

    private boolean testVertexesInput (int v1, int v2)
    {
        return v1 == v2 || testVertexInput(v1) || testVertexInput(v2);
    }

    private boolean testVertexInput (int v)
    {
        return v < 0 || v > vertexesColours.length;
    }
}
