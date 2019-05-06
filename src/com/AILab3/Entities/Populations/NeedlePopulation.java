package com.AILab3.Entities.Populations;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.NeedleGene;
import com.AILab3.Entities.Interfaces.IPopType;
import com.AILab3.GeneticAlgo.Constants;

import java.util.Vector;

public class NeedlePopulation implements IPopType
{
    @Override
    public void initPopulation (Object n, Vector<Gene> p)
    {
        int customPopulationSize = (int) n;
        int age_factor = customPopulationSize / 5 + 1;
        for (int i = 0; i < customPopulationSize; i++)
        {
            char[] g = new char[20];
            for (int j = 0; j < 20; j++)
            {
                float ra = Constants.r.nextFloat();
                g[j] = ra < 0.25f ? '0' : ra < 0.5f ? '1' : '?';
            }
            NeedleGene a = new NeedleGene(g, 0, i / age_factor, 0);
            p.add(a);
        }
    }
}
