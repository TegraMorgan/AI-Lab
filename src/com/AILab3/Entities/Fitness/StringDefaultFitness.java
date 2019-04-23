package com.AILab3.Entities.Fitness;

import com.AILab3.Entities.Genes.StringGene;
import com.AILab3.Entities.Interfaces.IFitnessAlgo;

public class StringDefaultFitness implements IFitnessAlgo
{
    @Override
    public void updateFitness (Object g)
    {
        StringGene sg = (StringGene) g;
        int worst = 89 * StringGene.targetLength;
        int fitness = 0;
        for (int j = 0; j < StringGene.targetLength; j++)
        {
            int cf = Math.abs((sg.str.charAt(j) - StringGene.target.charAt(j)));
            fitness += cf;
        }
        sg.fitness = fitness;
        sg.inverseFitness = worst - fitness;
    }
}
