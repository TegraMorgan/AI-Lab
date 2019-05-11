package com.AILab3.Entities.Fitness;

import com.AILab3.Entities.Genes.ParetoGene;
import com.AILab3.Entities.Interfaces.IFitnessAlgo;

public class ParetoFitness implements IFitnessAlgo
{
    @Override
    public void updateFitness (Object gene)
    {
        final ParetoGene gen = (ParetoGene) gene;
        final int l = gen.getProblemSize();
        float[] g = gen.gene;
        float fitn = 0;
        for (int i = 0; i < l; i++)
        {

        }
    }
}
