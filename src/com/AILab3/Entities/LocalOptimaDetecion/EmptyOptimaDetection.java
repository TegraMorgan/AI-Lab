package com.AILab3.Entities.LocalOptimaDetecion;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Interfaces.ILocalOptimaSignals;

import java.util.Vector;

public class EmptyOptimaDetection implements ILocalOptimaSignals
{
    @Override
    public boolean detectLocalOptima (Vector<Gene> population)
    {
        return false;
    }
}
