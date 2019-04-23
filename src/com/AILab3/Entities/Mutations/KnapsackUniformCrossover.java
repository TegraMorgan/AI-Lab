package com.AILab3.Entities.Mutations;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.KnapsackGene;
import com.AILab3.Entities.Interfaces.IMutationAlgo;
import com.AILab3.GeneticAlgo.Constants;

public class KnapsackUniformCrossover implements IMutationAlgo
{
    @Override
    public Gene mutate (Gene p1, Gene p2)
    {
        KnapsackGene one = (KnapsackGene) p1, two = (KnapsackGene) p2;
        int[] g = one.gene.clone();
        int _l = KnapsackGene.count;
        for (int i = 0; i < _l; i++)
            if (Constants.r.nextInt(2) == 1) g[i] = two.gene[i];
        if (Constants.r.nextInt(Constants.RAND_MAX) < Constants.GA_MUTATION) IMutationAlgo.mutateOnePoint(g);
        return new KnapsackGene(g, 0, 0, 0);
    }
}
