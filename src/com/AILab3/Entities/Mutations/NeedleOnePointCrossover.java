package com.AILab3.Entities.Mutations;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.NeedleGene;
import com.AILab3.Entities.Interfaces.IMutationAlgo;
import com.AILab3.GeneticAlgo.Constants;

public class NeedleOnePointCrossover extends BaseMutation
{
    @Override
    public Gene mutate (Gene p1, Gene p2)
    {
        NeedleGene one = (NeedleGene) p1, two = (NeedleGene) p2;
        int psz = one.getProblemSize();
        char[] g = new char[psz];
        int mpos = Constants.r.nextInt(psz);
        if (mpos >= 0)
        {
            System.arraycopy(one.gene, 0, g, 0, mpos);
            System.arraycopy(two.gene, mpos, g, mpos, psz - mpos);
        }
        if (Constants.r.nextFloat() < 0.2) IMutationAlgo.mutateOnePoint(g);
        return new NeedleGene(g);
    }
}
