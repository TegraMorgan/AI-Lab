package com.AILab3.Entities.LocalOptimaEscape;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Interfaces.IEscapeLocalOptimum;

import java.util.Vector;

public class HyperMutation implements IEscapeLocalOptimum
{
    @Override
    public void endLocalEscape (Vector<Gene> population)
    {
        Gene.mutationAlgo.disableHyperMutation();
    }

    @Override
    public void startLocalEscape (Vector<Gene> population)
    {
        Gene.mutationAlgo.enableHyperMutation();
    }
}
