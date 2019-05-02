package com.AILab3.Entities.Populations;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.KnapsackGene;
import com.AILab3.Entities.Interfaces.IPopType;

import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.GA_POPSIZE;
import static com.AILab3.GeneticAlgo.Constants.r;

public class KnapsackPopulation implements IPopType
{
    @Override
    public void initPopulation (Object n, Vector<Gene> p)
    {
        int numOfItems = (int) n;
        int age_factor = GA_POPSIZE / 5 + 1;
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            int[] t = new int[numOfItems];
            t[r.nextInt(numOfItems)] = 1;
            p.add(new KnapsackGene(t, 0, i / age_factor, 0));
        }
    }
}
