package com.AILab3.Entities.Interfaces;

import com.AILab3.Entities.Genes.Gene;

import java.util.Vector;

public interface IEscapeLocalOptimum
{
    void startLocalEscape (Vector<Gene> population);

    void endLocalEscape (Vector<Gene> population);
}
