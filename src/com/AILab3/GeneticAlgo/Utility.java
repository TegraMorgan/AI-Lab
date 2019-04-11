package com.AILab3.GeneticAlgo;

import com.AILab3.Entities.Gene;
import com.AILab3.Entities.StringGene;

import java.util.Vector;

public class Utility
{

    public static void printMeanVariance (float[] averages)
    {
        System.out.println("Population average fitness: " + averages[0] + " | Population variance: " + averages[1]);
    }

    public static float[] calcPopMeanVarGeneric (Vector<Gene> population)
    {
        float[] res = new float[2];
        int t;
        float avgPopFit = 0;
        float popFitVar = 0;
        for (int i = 0; i < Constants.GA_POPSIZE; i++)
        {
            t = population.get(i).fitness;
            avgPopFit += t;
            popFitVar += t * t;
        }
        popFitVar = (popFitVar - ((avgPopFit * avgPopFit) / Constants.GA_POPSIZE)) / (Constants.GA_POPSIZE - 1);
        avgPopFit /= Constants.GA_POPSIZE;
        res[0] = avgPopFit;
        res[1] = popFitVar;
        return res;
    }
}
