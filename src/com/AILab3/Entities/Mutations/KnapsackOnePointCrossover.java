package com.AILab3.Entities.Mutations;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.KnapsackGene;
import com.AILab3.Entities.Interfaces.IMutationAlgo;
import com.AILab3.GeneticAlgo.Constants;

public class KnapsackOnePointCrossover extends BaseMutation
{
    @Override
    public Gene mutate (Gene p1, Gene p2)
    {
        KnapsackGene one = (KnapsackGene) p1, two = (KnapsackGene) p2;
        int spos = (Constants.r.nextInt(KnapsackGene.count));
        int[] g = one.gene.clone();
        if (KnapsackGene.count - spos >= 0) System.arraycopy(two.gene, spos, g, spos, KnapsackGene.count - spos);
        if (Constants.r.nextInt(Constants.RAND_MAX) < getMutationRate()) IMutationAlgo.mutateOnePoint(g);
        return new KnapsackGene(g);
    }
}
