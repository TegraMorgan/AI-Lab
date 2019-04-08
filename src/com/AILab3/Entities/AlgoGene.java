package com.AILab3.Entities;

import java.util.Comparator;

public class AlgoGene implements Comparable

{
    public String str;         // String of the gene
    public int fitness;        // Genetic fitness of the gene - less is better
    public int inverseFitness; // Inverse value of fitness - greater is better
    public int age;
    public static final ByFitness BY_FITNESS = new ByFitness();
    public static final ByAge BY_AGE = new ByAge();

    public AlgoGene ()
    {
        str = "";
        fitness = 0;
        inverseFitness = 0;
        age = 0;
    }

    public AlgoGene (String _str, int _fit, int _age, int _ifit)
    {
        str = _str;
        fitness = _fit;
        age = _age;
        inverseFitness = _ifit;
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
