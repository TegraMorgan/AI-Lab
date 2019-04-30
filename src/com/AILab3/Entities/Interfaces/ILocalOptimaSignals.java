package com.AILab3.Entities.Interfaces;

import com.AILab3.Entities.Genes.Gene;

import java.util.Vector;

public interface ILocalOptimaSignals
{
    boolean detectLocalOptima (Vector<Gene> population);
}
