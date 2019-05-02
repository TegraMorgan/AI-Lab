package com.AILab3.Entities.Populations;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.StringGene;
import com.AILab3.Entities.Interfaces.IPopType;

import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.*;

public class StringPopulation implements IPopType
{
    @Override
    public void initPopulation (Object n, Vector<Gene> p)
    {
        String t = (String) n;
        StringGene.target = t;
        int targetLength = t.length();
        int age_factor = GA_POPSIZE / 5 + 1;
        StringBuilder sb = new StringBuilder(targetLength);
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            for (int j = 0; j < targetLength; j++)
                sb.append((char) ((r.nextInt(RAND_MAX) % 90) + 32));
            p.add(new StringGene(sb.toString(), 0, i / age_factor, 0));
            sb.delete(0, sb.length());
        }
    }
}
