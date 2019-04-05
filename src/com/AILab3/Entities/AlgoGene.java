package com.AILab3.Entities;

import java.util.Comparator;

public class AlgoGene implements Comparable

{
    public String str;         // String of the gene
    public int fitness;        // Genetic fitness of the gene
    public int age;
    public static final ByFitness BY_FITNESS = new ByFitness();
    public static final ByAge BY_AGE = new ByAge();

    public AlgoGene ()
    {
        str = "";
        fitness = 0;
        age = 0;
    }

    public AlgoGene (String s, int fit, int a)
    {
        str = s;
        fitness = fit;
        age = a;
    }

    @Override
    public int compareTo (Object o)
    {
        return Integer.compare(this.fitness, ((AlgoGene) o).fitness);
    }

    public static class ByAge implements Comparator<AlgoGene>
    {
        @Override
        public int compare (AlgoGene o1, AlgoGene o2)
        {
            return Integer.compare((o1).age, (o2).age);
        }
    }

    public static class ByFitness implements Comparator<AlgoGene>
    {
        @Override
        public int compare (AlgoGene o1, AlgoGene o2)
        {
            return Integer.compare((o1).fitness, (o2).fitness);
        }
    }
}
