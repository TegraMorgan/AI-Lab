package com.AILab3.Entities;

import java.util.Arrays;
import java.util.Random;
import java.lang.Math;
import java.util.Vector;

@SuppressWarnings("WeakerAccess")
public class QueensBrain
{
    private static Random random = new Random();
    private float fitness;
    private int[] queens;

    //On average on each mutate() call n * MUTATION_PROBABILITY queens would be mutated
    public static final float MUTATION_PROBABILITY = 0.01f;

    //On average on each crossover() call n * MUTATION_PROBABILITY queens would be PMX swapped
    public static final float CROSSOVER_PROBABILITY = 0.5f;

    public QueensBrain (int n)
    {
        queens = new int[n];
        Arrays.setAll(queens, i -> i);
        for (int i = 0; i < n; i++)
        {
            swap(i, i + random.nextInt((n - i)));
        }
        calculateFitness();
    }

    private void swap (int q1, int q2)
    {
        int temp = queens[q1];
        queens[q1] = queens[q2];
        queens[q2] = temp;
    }

    public QueensBrain (QueensBrain b)
    {
        fitness = b.fitness;
        queens = b.queens.clone();
    }

    public float getFitness ()
    {
        return fitness;
    }

    @Override
    public String toString ()
    {
        String str = "";
        for (int y = 0; y < queens.length; y++)
        {
            for (int x = 0; x < queens.length; x++)
            {
                str += queens[x] == y ? "[Q]" : "[ ]";
            }
            str += "\n";
        }
        return str;
    }

    private boolean threaten (int i1, int i2)
    {
        return queens[i1] == queens[i2] || Math.abs(i1 - i2) == Math.abs(queens[i1] - queens[i2]);
    }

    private void calculateFitness ()
    {
        int count = 0;
        for (int i = 0; i < queens.length; i++)
        {
            for (int j = i + 1; j < queens.length; j++)
            {
                if (threaten(i, j))
                {
                    count++;
                }
            }
        }
        fitness = 1f / (count * count + 1f);
    }

    public boolean foundSolution ()
    {
        return fitness == 1f;
    }

    private QueensBrain mutate ()
    {
        Vector<Integer> shuffledIndices = new Vector<>();
        for (int i = 0; i < queens.length; i++)
        {
            if (random.nextFloat() < MUTATION_PROBABILITY)
            {
                shuffledIndices.add(i);
            }
        }
        for (int i = 0; i < shuffledIndices.size(); i++)
        {
            int r = i + random.nextInt((shuffledIndices.size() - i));
            swap(shuffledIndices.elementAt(i), shuffledIndices.elementAt(r));
            int temp = shuffledIndices.elementAt(i);
            shuffledIndices.setElementAt(shuffledIndices.elementAt(r), i);
            shuffledIndices.setElementAt(temp, r);
        }
        calculateFitness();
        return this;
    }

    public static QueensBrain getBaby (QueensBrain p1, QueensBrain p2)
    {
        return (random.nextBoolean() ? crossover(p1, p2) : crossover(p2, p1)).mutate();
    }

    private int indexOf (int x)
    {
        int y = -1;
        while (queens[++y] != x) ;
        return y;
    }

    private static QueensBrain crossover (QueensBrain b1, QueensBrain b2)
    {
        b1 = new QueensBrain(b1);
        Vector<Integer> crossovers = new Vector<>();
        for (int i = 0; i < b1.queens.length; i++)
        {
            if (random.nextFloat() < CROSSOVER_PROBABILITY)
            {
                crossovers.add(i);
            }
        }
        for (int c : crossovers)
        {
            b1.swap(c, b1.indexOf(b2.queens[c]));
        }
        return b1;
    }
}