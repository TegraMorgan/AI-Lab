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
        for (int i = 0; i < l; i++)
        {
            Gene t = p.get(i);
            for (int j = i + 1; j < l; j++)
            {
                Gene o = p.get(j);
                if (t.similar(o) > 75)
                {
                    int pe = 10;
                    o.fitness += pe;
                    o.inverseFitness -= pe;
                }
            }
        }
    }

    @Override
    public void endLocalEscape (Vector<Gene> population)
    {
    }
}
