package com.AILab3.Entities.Genes;

import com.AILab3.GeneticAlgo.Constants;

import java.util.Arrays;

public class NeedleGene extends Gene
{
    public char[] gene;

    public NeedleGene (char[] _g, int _f, int _a, int _if)
    {
        super(0, 0, 0);
        gene = _g;
    }

    public NeedleGene (char[] _g)
    {
        this(_g, 0, 0, 0);
    }


    @Override
    public int similar (Gene _o)
    {
        int res = 0;
        char[] o = ((NeedleGene) _o).gene;
        char[] t = this.gene;
        for (int i = 0; i < o.length; i++)
            if (o[i] == t[i]) res++;
        return (int) ((double) res / (double) this.getProblemSize());
    }

    @Override
    public int getProblemSize ()
    {
        return gene.length;
    }

    @Override
    public boolean isSolution ()
    {
        int l = getProblemSize();
        for (int i = 0; i < l; i++)
            if (this.gene[i] != '1') return false;
        return true;
    }

    @Override
    public void updateFitness ()
    {
        Gene.fitnessAlgo.updateFitness(this);
    }

    @Override
    public void replace ()
    {
        int l = getProblemSize();
        for (int i = 0; i < l; i++)
        {
            float ra = Constants.r.nextFloat();
            gene[i] = ra < 0.25 ? '0' : ra < 0.5 ? '1' : '?';
        }
    }

    @Override
    public String toString ()
    {
        return Arrays.toString(gene);
    }
}
