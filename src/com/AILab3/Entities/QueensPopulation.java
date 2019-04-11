package com.AILab3.Entities;

import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Random;

public class QueensPopulation
{
    private final int GEN_SIZE;
    private static Random random = new Random();
    private final int N;
    private QueensBrain[] gen;
    private QueensBrain solution = null;
    private final float ELITE;
    private int genCount;

    public QueensPopulation (int n, int genSize, float elite)
    {
        N = n;
        GEN_SIZE = genSize;
        gen = new QueensBrain[GEN_SIZE];
        genCount = 1;
        ELITE = elite;
    }

    private double[] fitnessCumSum ()
    {
        double[] cumsum = new double[GEN_SIZE];
        cumsum[0] = gen[0].getFitness();
        for (int i = 1; i < GEN_SIZE; i++)
        {
            cumsum[i] = cumsum[i - 1] + gen[i].getFitness();
        }
        double sum = cumsum[GEN_SIZE - 1];
        for (int i = 0; i < GEN_SIZE; i++)
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
            if (q.getFitness() > best.getFitness())
            {
                best = q;
            }
        }
        return best;
    }

    public QueensBrain repopulate (int maxGen)
    {
        Arrays.setAll(gen, i -> new QueensBrain(N));
        Arrays.sort(gen, QueensBrain.BY_FITNESS);
        solution = null;
        genCount = 1;
        QueensBrain[] newGen = new QueensBrain[GEN_SIZE];
        int eliteSize = (int) (N * ELITE);
        while ((solution = getSolution()) == null && genCount < maxGen)
        {
            Arrays.sort(gen, QueensBrain.BY_FITNESS);
            double[] cumsum = fitnessCumSum();
            Arrays.setAll(newGen, i -> i < eliteSize ? gen[i] : child(cumsum));
            gen = newGen;
            genCount++;
        }
        return solution == null ? gen[0] : solution;
    }
}