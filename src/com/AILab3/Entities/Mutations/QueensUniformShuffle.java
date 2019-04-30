package com.AILab3.Entities.Mutations;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.QueensGene;
import com.AILab3.GeneticAlgo.Constants;

import java.util.Vector;

public class QueensUniformShuffle extends BaseMutation
{
    private static void queenMutate (QueensGene b1)
    {
        float mr = getMutationRate();
        Vector<Integer> shuffledIndices = new Vector<>();
        for (int i = 0; i < b1.queens.length; i++)
            if (Constants.r.nextInt(Constants.RAND_MAX) < mr)
                shuffledIndices.add(i);
        for (int i = 0; i < shuffledIndices.size(); i++)
        {
            int r = i + Constants.r.nextInt((shuffledIndices.size() - i));
            b1.swap(shuffledIndices.elementAt(i), shuffledIndices.elementAt(r));
            int temp = shuffledIndices.elementAt(i);
            shuffledIndices.setElementAt(shuffledIndices.elementAt(r), i);
            shuffledIndices.setElementAt(temp, r);
        }
    }

    @Override
    public Gene mutate (Gene p1, Gene p2)
    {
        QueensGene b1 = (QueensGene) p1, b2 = (QueensGene) p2;
        b1 = new QueensGene(b1);
        Vector<Integer> crossovers = new Vector<>();
        float mr = getMutationRate();
        for (int i = 0; i < b1.queens.length; i++)
            if (Constants.r.nextInt(Constants.RAND_MAX) < mr)
                crossovers.add(i);
        for (int c : crossovers)
            b1.swap(c, b1.indexOf(b2.queens[c]));
        if (Constants.r.nextInt(Constants.RAND_MAX) < mr)
            queenMutate(b1);
        return b1;
    }
}
