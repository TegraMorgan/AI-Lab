package com.AILab3.Entities;

import java.util.Arrays;

public class QueensPopulation
{
    public static final int GENERATION_SIZE = 10000;
    private final int N;
    private QueensBrain[] gen = new QueensBrain[GENERATION_SIZE];

    public QueensPopulation (int n) { N = n; }

    public String repopulateTillSolution ()
    {
        Arrays.setAll(gen, i -> new QueensBrain(N));
        int genCount = 0;
        QueensBrain solution = null;
        while (solution == null)
        {   //while no solution: repopulate
            QueensBrain[] newGen = new QueensBrain[GENERATION_SIZE];
            float sum = 0;
            for (QueensBrain b : gen)
            {
                sum += b.getFitness();
            }
            for (int i = 0; i < GENERATION_SIZE && solution == null; i++)
            {
                QueensBrain baby = QueensBrain.getBaby(gen, sum);
                newGen[i] = baby;
                if (baby.foundSolution())
                {
                    solution = baby;
                }
            }
            genCount++;
            gen = newGen;
        }
        return "Gen " + genCount + ":\n" + solution.toString();
    }
}
