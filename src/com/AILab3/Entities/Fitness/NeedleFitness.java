package com.AILab3.Entities.Fitness;

import com.AILab3.Entities.Genes.NeedleGene;
import com.AILab3.GeneticAlgo.Constants;

public class NeedleFitness implements com.AILab3.Entities.Interfaces.IFitnessAlgo
{
    @Override
    public void updateFitness (Object gene)
    {
        NeedleGene g = (NeedleGene) gene;
        char[] t = g.gene;
        int l = 1000;
        int s = g.getProblemSize();
        for (int i = 0; i < l; i++)
        {
            char[] fen = new char[s];
            for (int j = 0; j < s; j++)
                if (t[j] != '?') fen[j] = t[j];
                else fen[j] = Constants.r.nextFloat() > 0.5 ? '1' : '0';
            if (isNeedle(fen))
            {
                g.fitness = i;
                g.inverseFitness = l - i;
                return;
            }
        }
        g.fitness = l;
        g.inverseFitness = 0;
    }

    private boolean isNeedle (char[] n)
    {
        for (char c : n) if (c != '1') return false;
        return true;
    }
}

