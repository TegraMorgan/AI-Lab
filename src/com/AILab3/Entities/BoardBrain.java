package com.AILab3.Entities;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class BoardBrain
{
    public final static int MUTATE_RANGE = 100; //In average 1 out of MUTATE_RANGE positions would be mutated
    private static Random random = new Random();
    private float fitness;
    private final int N;
    private Point[] positions;

    public BoardBrain (int n)
    {
        positions = new Point[n];
        N = n;
        Arrays.setAll(positions, i -> new Point(random.nextInt(n), random.nextInt(n)));
        fitness = calculateFitness();
    }

    public BoardBrain (BoardBrain b)
    {
        fitness = b.fitness;
        N = b.N;
        positions = new Point[b.positions.length];
        Arrays.setAll(positions, i -> new Point(b.positions[i]));
    }

    private float calculateFitness ()
    {
        int count = 0;
        for (Point q1 : positions)
        {
            for (Point q2 : positions)
            {
                if (q1.x == q2.x || q1.y == q2.y || StrictMath.abs(q1.x - q2.x) == StrictMath.abs(q1.y - q2.y))
                {
                    count++;
                }
            }
        }
        return 1f / (float) count;
    }

    private void mutate ()
    {
        for (Point pos : positions)
        {
            if (random.nextInt(MUTATE_RANGE) == 0)
            {
                if (random.nextInt(2) == 0)
                {
                    pos.x = random.nextInt(N);
                }
                if (random.nextInt(2) == 0)
                {
                    pos.y = random.nextInt(N);
                }
            }
        }
        fitness = calculateFitness();
    }

    public static BoardBrain selectNext (BoardBrain[] gen, float fitnessSum)
    {
        float r = random.nextFloat() * fitnessSum, sum = gen[0].fitness;
        int i = 0;
        while (sum < r)
        {
            sum += gen[++i].fitness;
        }
        BoardBrain next = new BoardBrain((gen[i]));
        next.mutate();
        return next;
    }

    public static BoardBrain crossover (BoardBrain a, BoardBrain b)
    {
        //...
        return null;
    }
}