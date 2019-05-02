package com.AILab3.Entities.Genes;


import java.util.Arrays;

import static com.AILab3.GeneticAlgo.Constants.r;


public class QueensGene extends Gene
{
    public int[] queens;


    public QueensGene (int _n)
    {
        this(_n, 0, 0, 0);
    }

    public QueensGene (int _n, int _f, int _a, int _if)
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
            if (o[i] == t[i]) res++;
        return (int) ((double) res / (double) this.getProblemSize() * 100);
    }

    @Override
    public int getProblemSize ()
    {
        return queens.length;
    }

    @Override
    public void replace ()
    {
        int _n = this.getProblemSize();
        Arrays.setAll(queens, i -> i);
        for (int i = 0; i < _n; i++)
            swap(i, i + r.nextInt((_n - i)));
    }

    @Override
    public String toString ()
    {
        return Arrays.toString(this.queens);
    }
}
