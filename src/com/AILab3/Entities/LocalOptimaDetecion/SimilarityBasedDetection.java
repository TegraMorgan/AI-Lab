package com.AILab3.Entities.LocalOptimaDetecion;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Interfaces.ILocalOptimaSignals;
import com.AILab3.GeneticAlgo.Constants;

import java.util.Vector;

public class SimilarityBasedDetection implements ILocalOptimaSignals
{
    private static final float POPULATION_TO_COMPARE = (Constants.GA_POPSIZE * 0.5f);
    private int oldBest;
    private int bcounter;

    public SimilarityBasedDetection ()
    {
        oldBest = -1;
        bcounter = 0;
    }

    @Override
    public boolean detectLocalOptima (Vector<Gene> p)
    {
        int pb = p.get(0).fitness;
        if (oldBest == -1)
        {
            oldBest = pb;
            return false;
        }
        if (oldBest > pb)
        {
            bcounter = 0;
            oldBest = pb;
            return false;
        }
        bcounter++;
        int sm = 0, c = 0;
        for (int i = 0; i < POPULATION_TO_COMPARE; i++)
            for (int j = i + 1; j < POPULATION_TO_COMPARE; j++)
            {
                c++;
                sm += p.get(i).similar(p.get(j));
            }
        sm = (sm / c) + (bcounter / 10);
        if (sm + (bcounter / 10) > 80)
        {
            bcounter = 0;
            return true;
        }
        return false;
    }
}
