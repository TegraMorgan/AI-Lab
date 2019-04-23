package com.AILab3.Entities.Fitness;

import com.AILab3.Entities.Genes.QueensGene;
import com.AILab3.Entities.Interfaces.IFitnessAlgo;

import static org.apache.commons.math3.util.CombinatoricsUtils.binomialCoefficient;

public class QueensThreatFitness implements IFitnessAlgo
{
    @Override
    public void updateFitness (Object gene)
    {
        QueensGene qg = (QueensGene) gene;
        int count = 0;
        for (int i = 0; i < qg.queens.length; i++)
            for (int j = i + 1; j < qg.queens.length; j++)
                if (threaten(qg.queens, i, j))
                    count++;
        qg.fitness = count;
        // This is the worst fitness can get 👇🏻
        qg.inverseFitness = (int) (binomialCoefficient(qg.queens.length, 2) - qg.fitness);
    }

    private boolean threaten (int[] queens, int i1, int i2)
    {
        return queens[i1] == queens[i2] || Math.abs(i1 - i2) == Math.abs(queens[i1] - queens[i2]);
    }
}
