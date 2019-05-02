package com.AILab3.Entities.LocalOptimaEscape;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Interfaces.IEscapeLocalOptimum;

import java.util.Vector;

public class RandomImmigrants implements IEscapeLocalOptimum
{

    @Override
    public void startLocalEscape (Vector<Gene> p)
    {
        float KEBAB_RATE = 0.2f;
        int l = p.size(), r = (int) (l * KEBAB_RATE);
        for (int i = r; i < l; i++)
            p.get(i).replace();
    }

    @Override
    public void endLocalEscape (Vector<Gene> population)
    {

    }
}
