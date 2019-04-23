package com.AILab3.Entities;


import com.AILab3.GeneticAlgo.Mutation;

import java.util.Arrays;
import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.GA_POPSIZE;
import static com.AILab3.GeneticAlgo.Constants.r;
import static org.apache.commons.math3.util.CombinatoricsUtils.binomialCoefficient;


public class QueensGene extends Gene
{
    public int[] queens;


    public QueensGene (int _n)
    {
        this(_n, 0, 0, 0);
    }

    public QueensGene (int _n, int _f, int _a, int _if)
    {
        super(_f, _a, _if);
        queens = new int[_n];
        Arrays.setAll(queens, i -> i);
        for (int i = 0; i < _n; i++)
            swap(i, i + r.nextInt((_n - i)));

    }

    public QueensGene (QueensGene _qg)
    {
        super(_qg.fitness, _qg.age, _qg.inverseFitness);
        this.queens = _qg.queens.clone();
    }


    public static void initPopulation (int numOfQueens, Vector<Gene> population)
    {
        int age_factor = GA_POPSIZE / 5 + 1;
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            QueensGene a = new QueensGene(numOfQueens, 0, i / age_factor, 0);
            population.add(a);
        }
    }

    public static void printBest (Vector<Gene> p)
    {
        QueensGene best = (QueensGene) p.get(0);
        String s = Arrays.toString(best.queens);
        System.out.println("Best: " + s + " (" + best.fitness + ")");
    }

    @Override
    public boolean isSolution ()
    {
        return fitness == 0;
    }

    @Override
    public void updateFitness ()
    {
        int count = 0;
        for (int i = 0; i < queens.length; i++)
            for (int j = i + 1; j < queens.length; j++)
                if (threaten(i, j))
                    count++;
        fitness = count;
        // This is the worst fitness can get 👇🏻
        inverseFitness = (int) (binomialCoefficient(queens.length, 2) - fitness);
    }

    public static void mutation (Vector<Gene> parents, Vector<Gene> ark)
    {
        int size = parents.size();
        int j = 0, i1, i2;
        for (int i = ark.size(); i < GA_POPSIZE; i++, j += 2)
        {
            i1 = j % size;
            i2 = (j + 1) % size;
            switch (mutationAlgo)
            {
                case "shuffle":
                    ark.add(Mutation.queenUniformShuffle((QueensGene) parents.get(i1), (QueensGene) parents.get(i2)));
                    break;
                default:
                    ark.add(Mutation.queenUniformShuffle((QueensGene) parents.get(i1), (QueensGene) parents.get(i2)));
                    break;
            }
        }
    }

    private boolean threaten (int i1, int i2)
    {
        return queens[i1] == queens[i2] || Math.abs(i1 - i2) == Math.abs(queens[i1] - queens[i2]);
    }

    public void swap (int q1, int q2)
    {
        int temp = queens[q1];
        queens[q1] = queens[q2];
        queens[q2] = temp;
    }

    public int indexOf (int x)
    {
        int y = -1;
        while (queens[++y] != x) ;
        return y;
    }


}
