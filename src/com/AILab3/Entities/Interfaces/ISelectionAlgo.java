package com.AILab3.Entities.Interfaces;


import com.AILab3.Entities.Genes.Gene;

import java.util.Vector;

public interface ISelectionAlgo
{
    void selectParents (Vector<Gene> population,
                        Vector<Gene> parentsRoulette,
                        int amountOfChildrenToGenerate,
                        boolean aging);
}
