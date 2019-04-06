package com.AILab3.Entities;

import java.util.Arrays;
import java.util.Random;

public class QueensPopulation
{
    public static final int GENERATION_SIZE = 30;
    private static Random random = new Random();
    private final int N;
    private QueensBrain[] gen = new QueensBrain[GENERATION_SIZE];
    private QueensBrain solution = null;
    private int genCount;

    public QueensPopulation (int n)
    {
        N = n;
        Arrays.setAll(gen, i -> new QueensBrain(N));
        genCount = 1;
    }

    private double[] fitnessCumSum ()
    {
        double[] cumsum = new double[GENERATION_SIZE];
        cumsum[0] = gen[0].getFitness();
        for (int i = 1; i < GENERATION_SIZE; i++)
        {
            cumsum[i] = cumsum[i - 1] + gen[i].getFitness();
        }
        double sum = cumsum[GENERATION_SIZE - 1];
        for (int i = 0; i < GENERATION_SIZE; i++)
        {
            cumsum[i] /= sum;
        }
        return cumsum;
    }

    private QueensBrain wheelOfFortune (double[] cumsum)
    {
        double r = random.nextDouble();
        int i = -1;
        while (cumsum[++i] < r) ;
        return gen[i];
    }

    private QueensBrain child (double[] cumsum)
    {
        QueensBrain p1 = wheelOfFortune(cumsum), p2;
        while ((p2 = wheelOfFortune(cumsum)) == p1) ;
        return QueensBrain.getBaby(p1, p2);
    }

    public QueensBrain getSolution ()
    {
        if (solution == null)
        {
            for (QueensBrain qb : gen)
            {
                if (qb.foundSolution())
                {
                    solution = qb;
                    break;
                }
            }
        }
        return solution;
    }

    public int getGenerationCount ()
    {
        return genCount;
    }

    private QueensBrain best ()
    {
        QueensBrain best = gen[0];
        for (QueensBrain q : gen)
        {
            if (q.getFitness() >= best.getFitness())
            {
                best = q;
            }
        }
        return best;
    }

    public void repopulateTillSolution ()
    {
        genCount = 1;
        QueensBrain[] newGen = new QueensBrain[GENERATION_SIZE];
        while ((solution = getSolution()) == null)
        {
            double[] cumsum = fitnessCumSum();
            Arrays.setAll(newGen, i -> i == 0 ? best() : child(cumsum));
            gen = newGen;
            genCount++;
            if (genCount % 2 == 0)
            {
                System.out.println("Gen " + genCount + ", F = " + Math.sqrt(1f / best().getFitness() - 1));
            }
        }
    }
}