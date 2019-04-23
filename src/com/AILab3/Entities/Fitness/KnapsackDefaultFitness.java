package com.AILab3.Entities.Fitness;

import com.AILab3.Entities.Genes.KnapsackGene;
import com.AILab3.Entities.Interfaces.IFitnessAlgo;

public class KnapsackDefaultFitness implements IFitnessAlgo
{
    @Override
    public void updateFitness (Object g)
    {
        KnapsackGene kg = (KnapsackGene) g;

        if (kg.gene == null || KnapsackGene.prices == null || kg.gene.length != KnapsackGene.prices.length)
        {
            System.out.println("CANNOT CALCULATE FITNESS!");
        } else
        {
            int sackValue = 0;
            int sackWeight = 0;
            for (int i = 0; i < KnapsackGene.prices.length; i++)
            {
                sackValue += KnapsackGene.prices[i] * kg.gene[i];
                sackWeight += KnapsackGene.weights[i] * kg.gene[i];
            }
            if (sackWeight > KnapsackGene.capacity)
                kg.fitness = KnapsackGene.priceSum + (sackWeight - KnapsackGene.capacity);
            else kg.fitness = KnapsackGene.priceSum - sackValue;
            kg.inverseFitness = KnapsackGene.priceSum - kg.fitness;
        }
    }
}
