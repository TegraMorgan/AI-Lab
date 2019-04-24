package com.AILab3.Entities.Genes;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.GA_POPSIZE;
import static com.AILab3.GeneticAlgo.Constants.r;

public class KnapsackGene extends Gene
{
    public static int capacity;
    public static int[] weights;
    public static int[] prices;
    public static int priceSum;
    public static int count;
    private static int[] solution;
    public int[] gene;

    public KnapsackGene (int[] _g)
    {
        this(_g, 0, 0, 0);
    }

    public KnapsackGene (int[] _g, int _f, int _a, int _if)
    {
        super(_f, _a, _if);
        gene = _g;
    }

    private static void loadData (int _c, int[] _w, int[] _p, int[] _so)
    {
        capacity = _c;
        weights = _w;
        prices = _p;
        solution = _so;
        priceSum = 0;
        for (int price : prices) priceSum += price;
        count = _w.length;
    }

    /*
    c - Capacity
    w - Weights
    p - Profit
    s - Solution
     */
    public static int parseProblem (int probNo)
    {
        char[] dataType = {'c', 'w', 'p', 's'};
        String[] fileNameConstants = {"data\\p0", "_", ".txt"};
        FileInputStream openFile;
        String[] receivedData = new String[4];
        for (int i = 0; i < 4; i++)
        {
            StringBuilder fileNameToOpen = new StringBuilder();
            StringBuilder dataFromFile = new StringBuilder();
            fileNameToOpen.append(fileNameConstants[0]);
            fileNameToOpen.append(probNo);
            fileNameToOpen.append(fileNameConstants[1]);
            fileNameToOpen.append(dataType[i]);
            fileNameToOpen.append(fileNameConstants[2]);
            try
            {
                openFile = new FileInputStream(fileNameToOpen.toString());
                int size = openFile.available();
                for (int j = 0; j < size; j++)
                    dataFromFile.append((char) openFile.read());
                openFile.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            receivedData[i] = dataFromFile.toString();
        }
        int capacity = 0;
        int[] weights = new int[0];
        int[] profits = new int[0];
        int[] solution = new int[0];
        try
        {
            capacity = Integer.parseInt(receivedData[0].replace("\n", ""));
            String[] w = receivedData[1].replace(" ", "").split("\n");
            String[] p = receivedData[2].replace(" ", "").split("\n");
            String[] so = receivedData[3].replace(" ", "").split("\n");
            weights = new int[w.length];
            profits = new int[p.length];
            solution = new int[so.length];
            for (int i = 0; i < w.length; i++)
            {
                weights[i] = Integer.parseInt(w[i]);
                profits[i] = Integer.parseInt(p[i]);
                solution[i] = Integer.parseInt(so[i]);
            }
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        loadData(capacity, weights, profits, solution);
        return weights.length;
    }

    public static void initPopulation (int numOfItems, Vector<Gene> population)
    {
        int age_factor = GA_POPSIZE / 5 + 1;
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            int[] t = new int[numOfItems];
            t[r.nextInt(numOfItems)] = 1;
            population.add(new KnapsackGene(t, 0, i / age_factor, 0));
        }
    }

    public static void PrintBest (Vector<Gene> p)
    {
        KnapsackGene best = (KnapsackGene) p.get(0);
        String s = Arrays.toString(best.gene);
        System.out.println("Best: " + s + " (" + best.fitness + ")");
    }

    public static void mutation (Vector<Gene> parents, Vector<Gene> ark)
    {
        int size = parents.size();
        int j = 0, i1, i2;
        for (int i = ark.size(); i < GA_POPSIZE; i++, j += 2)
        {
            i1 = j % size;
            i2 = (j + 1) % size;
            ark.add(Gene.mutationAlgo.mutate(parents.get(i1), parents.get(i2)));
        }
    }

    @Override
    public boolean isSolution ()
    {
        boolean ret = true;
        for (int i = 0; i < count; i++)
            if (this.gene[i] != solution[i]) ret = false;
        return ret;
    }

    @Override
    public void updateFitness ()
    {
        Gene.fitnessAlgo.updateFitness(this);
    }

    @Override
    public int similar (Gene other)
    {
        int[] o = ((KnapsackGene) other).gene;
        int[] t = this.gene;
        int res = 0;
        for (int i = 0; i < this.gene.length; i++)
            if (o[i] != t[i]) res++;
        return res;
    }
}
