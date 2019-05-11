package com.AILab3.Entities.Populations;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.ParetoGene;
import com.AILab3.Entities.Interfaces.IPopType;

import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.GA_POPSIZE;
import static com.AILab3.GeneticAlgo.Constants.r;

public class ParetoPopulation implements IPopType
{
    /**
     * @param arguments Type of int[] - Min,Max,Precision
     * @param p         Population
     */
    @Override
    public void initPopulation (Object arguments, Vector<Gene> p)
    {
        final int[] args = (int[]) arguments;
        final int min = args[0], max = args[1], l = args[2];
        final float segmentSize = ((float) Math.abs(max - min)) / ((float) l), midSegment = segmentSize / 2;
        int age_factor;
        float center, randomMove;
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            age_factor = GA_POPSIZE / 5 + 1;
            float[] gen = new float[l];
            for (int j = 0; j < l; j++)
            {
                center = min + j * segmentSize + midSegment;
                randomMove = r.nextFloat() * midSegment;
                gen[j] = r.nextBoolean() ? center + randomMove : center - randomMove;
            }
            p.add(new ParetoGene(gen, 0, i / age_factor, 0));
        }
        ParetoGene.setMinMax(min, max);
    }
}
