package com.AILab3.Entities.Mutations;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.ParetoGene;

import static com.AILab3.GeneticAlgo.Constants.RAND_MAX;
import static com.AILab3.GeneticAlgo.Constants.r;

public class ParetoOnePointCrossover extends BaseMutation
{
    @Override
    public Gene mutate (Gene p1, Gene p2)
    {
        final ParetoGene one = (ParetoGene) p1, two = (ParetoGene) p2;
        final int tsize = p1.getProblemSize(), crossPosition = (r.nextInt(tsize));
        int[] t = ParetoGene.getMinMax();
        double[] g = new double[tsize];
        System.arraycopy(one.gene, 0, g, 0, crossPosition);
        System.arraycopy(two.gene, crossPosition, g, crossPosition, tsize - crossPosition);

        if (r.nextInt(RAND_MAX) < getMutationRate()) mutateOnePoint(g, t[0], t[1], tsize);
        return new ParetoGene(g);
    }

    private void mutateOnePoint (double[] g, int min, int max, int length)
    {
        final int point = r.nextInt(length);
        final double chunk = Math.abs(max - min) / length, halfChunk = chunk / 2;
        double newVaule = min + (chunk * point) + halfChunk;
        if (r.nextBoolean()) newVaule += halfChunk * r.nextFloat();
        else newVaule -= halfChunk * r.nextFloat();
        g[point] = newVaule;
    }
}
