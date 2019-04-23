package com.AILab3.Entities.Selections;

import com.AILab3.Entities.Genes.Gene;

import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.GA_POPSIZE;
import static com.AILab3.GeneticAlgo.Constants.r;

public class RandomSampling implements com.AILab3.Entities.Interfaces.ISelectionAlgo
{
    @Override
    public void selectParents (Vector<Gene> population,
                               Vector<Gene> parents,
                               int childrenToGenerate,
                               boolean aging)
    {
        parents.clear();
        int i1, i2, f = 0, l = GA_POPSIZE - 1, m = GA_POPSIZE;
        if (aging)
        {
            while (population.get(f).age < 2) f++;
            while (population.get(l).age > 4) l--;
            m = l - f + 1;
        }
        for (int i = 0; i < childrenToGenerate; i++)
        {
            do
            {
                i1 = r.nextInt(m) + f;
                i2 = r.nextInt(m) + f;
            } while (i1 == i2);
            parents.add(population.get(i1));
            parents.add(population.get(i2));
        }
    }
}
