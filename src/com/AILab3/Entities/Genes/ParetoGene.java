package com.AILab3.Entities.Genes;

import java.util.Arrays;

import static com.AILab3.GeneticAlgo.Constants.r;

public class ParetoGene extends Gene
{
    private static int min, max;
    public float[] gene;

    public ParetoGene (float[] _g)
    {
        this(_g, 0, 0, 0);
    }

    public ParetoGene (float[] _g, int _f, int _a, int _if)
    {
        super(_f, _a, _if);
        gene = _g;
    }

    public static void setMinMax (int mi, int ma)
    {
        min = mi;
        max = ma;
    }

    @Override
    public int similar (Gene other)
    {
        final float[] o = ((ParetoGene) other).gene;
        final float[] t = this.gene;
        int res = 0;
        for (int i = 0; i < this.gene.length; i++)
            if (o[i] == t[i]) res++;
        return (int) ((double) res / (double) this.getProblemSize() * 100);
    }

    @Override
    public int getProblemSize ()
    {
        return gene.length;
    }

    @Override
    public boolean isSolution ()
    {
        return false;
    }

    @Override
    public void updateFitness ()
    {
        Gene.fitnessAlgo.updateFitness(this);
    }

    @Override
    public void replace ()
    {
        final int l = this.getProblemSize();
        final float segmentSize = ((float) Math.abs(max - min)) / ((float) l), midSegment = segmentSize / 2;
        float[] gen = new float[l];
        for (int j = 0; j < l; j++)
            gen[j] = r.nextBoolean() ? j * segmentSize + midSegment + r.nextFloat() * midSegment : j * segmentSize + midSegment - r.nextFloat() * midSegment;
        this.gene = gen;
    }

    @Override
    public String toString ()
    {
        return Arrays.toString(gene);
    }
}
