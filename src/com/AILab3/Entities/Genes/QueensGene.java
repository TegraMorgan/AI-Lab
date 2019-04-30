package com.AILab3.Entities.Genes;


import java.util.Arrays;
import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.GA_POPSIZE;
import static com.AILab3.GeneticAlgo.Constants.r;


public class QueensGene extends Gene
{
    public int[] queens;


    public QueensGene (int _n)
    {
        this(_n, 0, 0, 0);
    }

    private QueensGene (int _n, int _f, int _a, int _if)
    {
        super(_f, _a, _if);
        queens = new int[_n];
        Arrays.setAll(queens, i -> i);
        for (int i = 0; i < _n; i++)
            swap(i, i + r.nextInt((_n - i)));

    }

    public QueensGene (QueensGene _qg)
    {
        super(_qg.fitness, 0, _qg.inverseFitness);
        this.queens = _qg.queens.clone();
    }


    public static void initPopulation (int numOfQueens, Vector<Gene> population)
    {
        int age_factor = GA_POPSIZE / 5 + 1;
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            QueensGene a = new QueensGene(numOfQueens, 0, i / age_factor, 0);
            population.add(a);
        }
    }

    public static void printBest (Vector<Gene> p)
    {
        QueensGene best = (QueensGene) p.get(0);
        String s = Arrays.toString(best.queens);
        //System.out.println("Best: " + s + " (" + best.fitness + ")");
        System.out.println("Best: " + best.fitness);
    }


    @Override
    public boolean isSolution ()
    {
        return fitness == 0;
    }

    @Override
    public void updateFitness ()
    {
        Gene.fitnessAlgo.updateFitness(this);
    }

    public void swap (int q1, int q2)
    {
        int temp = queens[q1];
        queens[q1] = queens[q2];
        queens[q2] = temp;
    }

    public int indexOf (int x)
    {
        int y = -1;
        //noinspection StatementWithEmptyBody
        while (queens[++y] != x) ;
        return y;
    }

    @Override
    public int similar (Gene other)
    {
        int res = 0;
        int[] o = ((QueensGene) other).queens;
        int[] t = this.queens;
        for (int i = 0; i < t.length; i++)
            if (o[i] != t[i]) res++;
        return (int) ((double) res / (double) this.getProblemSize() * 100);

    }

    @Override
    public int getProblemSize ()
    {
        return queens.length;
    }
}
