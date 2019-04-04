package com.AILab3.Entities;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

public class QueensBrain
{
    //On average 1 out of MUTATE_RANGE queens would be mutated.
    public final static int MUTATE_RANGE = 10000;
    private static Random random = new Random();
    private float fitness;
    private final int N;
    private Point[] queens;
    private boolean[] board;

    public QueensBrain (int n)
    {
        queens = new Point[n];
        board = new boolean[n * n];
        N = n;
        Arrays.setAll(queens, i -> randomQueen());
        fitness = calculateFitness();
        for (Point queen : queens)
        {
            board[queen.y * n + queen.x] = true;
        }
    }

    private Point randomQueen ()
    {
        int x, y;
        do
        {
            x = random.nextInt(N);
            y = random.nextInt(N);
        } while (board[y * N + x]);
        board[y * N + x] = true;
        return new Point(x, y);
    }

    public QueensBrain (QueensBrain b)
    {
        fitness = b.fitness;
        N = b.N;
        queens = new Point[b.queens.length];
        Arrays.setAll(queens, i -> new Point(b.queens[i]));
        board = b.board.clone();
    }

    public float getFitness ()
    {
        return fitness;
    }

    private boolean threaten (Point q1, Point q2)
    {
        return q1.x == q2.x || q1.y == q2.y || Math.abs(q1.x - q2.x) == Math.abs(q1.y - q2.y);
    }

    @Override
    public String toString ()
    {
        String[] board = new String[N * N];
        Arrays.fill(board, "[ ]");
        for (Point p : queens)
        {
            board[p.y * N + p.x] = "[Q]";
        }
        String str = "";
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                str += board[i * N + j];
            }
            str += "\n";
        }
        return str;
    }

    private float calculateFitness ()
    {
        int count = 0;
        for (int i = 0; i < queens.length; i++)
        {
            for (int j = i + 1; j < queens.length; j++)
            {
                if (threaten(queens[i], queens[j]))
                {
                    count++;
                }
            }
        }
        return 1f / (float)(count + 1);
    }

    public boolean foundSolution ()
    {
        return fitness == 1;
    }

    private QueensBrain mutate ()
    {
        for (Point pos : queens)
        {
            if (random.nextInt(MUTATE_RANGE) == 0)
            {
                Point r = randomQueen();
                board[pos.y * N + pos.x] = false;
                pos.x = r.x;
                pos.y = r.y;
            }
        }
        fitness = calculateFitness();
        return this;
    }

    public static QueensBrain getBaby (QueensBrain[] gen, float fitnessSum)
    {
        QueensBrain p1 = getParent(gen, fitnessSum), p2;
        while ((p2 = getParent(gen, fitnessSum)) == p1) ;
        return new QueensBrain(p1).crossover(p2).mutate();
    }

    private static QueensBrain getParent (QueensBrain[] gen, float fitnessSum)
    {
        float r = random.nextFloat() * fitnessSum, sum = gen[0].fitness;
        int i = 0;
        while (sum < r)
        {
            sum += gen[++i].fitness;
        }
        return gen[i];
    }

    private QueensBrain crossover (QueensBrain b)
    {
        int h = random.nextBoolean() ? N : N / 2;
        for (int i = h - N / 2; i < h; i++)
        {
            queens[i].x = b.queens[i].x;
            queens[i].y = b.queens[i].y;
        }
        return this;
    }
}
