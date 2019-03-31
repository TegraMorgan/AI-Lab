package com.AILab3.Entities;

import java.util.Comparator;

public class AlgoGene implements Comparable

{
    public String str;         // String of the gene
    public int fitness;        // Genetic fitness of the gene
    public static final ByFitness BY_FITNESS = new ByFitness();
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


    @Override
    public int compareTo (Object o)
    {
        return Integer.compare(this.fitness, ((AlgoGene) o).fitness);
    }

    public static class ByFitness implements Comparator
    {

        @Override
        public int compare (Object o1, Object o2)
        {
            return Integer.compare(((AlgoGene) o1).fitness, ((AlgoGene) o2).fitness);
        }
    }
}
