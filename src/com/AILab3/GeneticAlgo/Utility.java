package com.AILab3.GeneticAlgo;

import com.AILab3.Entities.AlgoGene;

import java.util.Vector;

public class Utility
{
    public static void initPopulation (Vector<AlgoGene> population, Vector<AlgoGene> buffer)
    {
        int targetSize = Constants.GA_TARGET.length();
        int age_factor = Constants.GA_POPSIZE / 5 + 1;
        StringBuilder sb = new StringBuilder(targetSize);
        for (int i = 0; i < Constants.GA_POPSIZE; i++)
        {
            AlgoGene citizen = new AlgoGene();
            for (int j = 0; j < targetSize; j++)
                sb.append((char) ((Constants.r.nextInt(Constants.RAND_MAX) % 90) + 32));
            citizen.str = sb.toString();
            citizen.age = i / age_factor;
            population.add(citizen);
            sb.delete(0, sb.length());
        }
        buffer.addAll(population);
    }

    public static void printBest (Vector<AlgoGene> gav)
    { System.out.println("Best: " + gav.get(0).str + " (" + gav.get(0).fitness + ")"); }
}
