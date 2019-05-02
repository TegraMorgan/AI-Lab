package com.AILab3.Entities.Populations;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.QueensGene;
import com.AILab3.Entities.Interfaces.IPopType;

import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.GA_POPSIZE;

public class QueenPopulation implements IPopType
{
    @Override
    public void initPopulation (Object n, Vector<Gene> p)
    {
        int numOfQueens = (int) n;
        if (numOfQueens > 4)
        {
            int age_factor = GA_POPSIZE / 5 + 1;
            for (int i = 0; i < GA_POPSIZE; i++)
            {
                QueensGene a = new QueensGene(numOfQueens, 0, i / age_factor, 0);
                p.add(a);
            }
        }
    }
}
