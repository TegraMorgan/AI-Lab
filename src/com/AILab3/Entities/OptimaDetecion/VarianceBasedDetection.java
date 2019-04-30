package com.AILab3.Entities.OptimaDetecion;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Interfaces.ILocalOptimaSignals;
import com.AILab3.GeneticAlgo.Constants;

import java.util.Vector;

public class VarianceBasedDetection implements ILocalOptimaSignals
{
    private static final float POPULATION_TO_COMPARE = (Constants.GA_POPSIZE * Constants.GA_ELITRATE);
    private int oldBest;
    private int bcounter;

    public VarianceBasedDetection ()
    {
        oldBest = -1;
        bcounter = 0;
    }

    /**
     * This function will detect local maxima based on
     * population fitness mean and variance
     *
     * @param population population to be observed
     * @return decision whether the population is in local maxima
     */
    @Override
    public boolean detectLocalOptima (Vector<Gene> population)
    {
        int t, k = population.get(0).fitness;
        int window = k + (bcounter / 10);
        float[] popData = new float[2];
        float Ex = 0, Ex2 = 0;
        if (oldBest == -1)
        {
            oldBest = k;
            return false;
        }
        if (oldBest > k)
        {
            bcounter = 0;
            oldBest = k;
            return false;
        }
        bcounter++;
        for (int i = 0; i < POPULATION_TO_COMPARE; i++)
        {
            t = population.get(i).fitness - k;
            Ex += t;
            Ex2 += t * t;
        }
        Ex2 = (Ex2 - ((Ex * Ex) / POPULATION_TO_COMPARE)) / (POPULATION_TO_COMPARE - 1);
        Ex = k + (Ex / POPULATION_TO_COMPARE);
        popData[0] = Ex;
        popData[1] = Ex2;
        if (popData[0] - Math.sqrt(popData[1]) - window < 0)
        {
            bcounter = 0;
            return true;
        }
        return false;
    }
}
