package com.AILab3.Entities.LocalOptimaEscape;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Interfaces.IEscapeLocalOptimum;

import java.util.Vector;

public class NichePenalty implements IEscapeLocalOptimum
{
    @Override
    public void startLocalEscape (Vector<Gene> p)
    {
        int l = p.size();
        Gene t = p.get(0);
        for (int j = 1; j < l; j++)
        {
            Gene o = p.get(j);
            if (t.similar(o) > 75)
            {
                o.fitness += 110;
                o.inverseFitness -= 110;
            }
        }
        p.sort(Gene.BY_FITNESS);
    }

    @Override
    public void endLocalEscape (Vector<Gene> population)
    {
    }
}
