package com.AILab3.Entities;

public class AlgoGene

{
    public String str;         // String of the gene
    public int fitness;        // Genetic fitness of the gene

    public AlgoGene ()
    {
        str = "";
        fitness = 0;
    }

    public AlgoGene (String s, int fit)
    {
        str = s;
        fitness = fit;
    }
}
