package com.AILab3;

import com.AILab3.Entities.AlgoGene;

import java.util.Random;
import java.util.Vector;

public class Main
{
    public static final int GA_POPSIZE = 2048;
    public static final int GA_MAXITER = 16384;
    public static final float GA_ELITRATE = 0.10f;
    public static final float GA_MUTATIONRATE = 0.25f;
    //public static final int GA_MUTATION = RAND_MAX * GA_MUTATIONRATE;
    public static final String GA_TARGET = "Hello world!";
    public static Vector<AlgoGene> _genes;

    void initPopulation (Vector<AlgoGene> population, Vector<AlgoGene> buffer)
    {
        int targetSize = GA_TARGET.length();
        Random r = new Random();
        StringBuilder sb = new StringBuilder(targetSize);
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            AlgoGene citizen = new AlgoGene();
            for (int j = 0; j < targetSize; j++)
                sb.append((char) ((r.nextInt() % 90) + 32));
            citizen.str = sb.toString();
            population.add(citizen);
            sb.delete(0, sb.length());
        }
        buffer.setSize(GA_POPSIZE);
    }

    void calc_fitness (Vector<AlgoGene> population)
    {
        String target = GA_TARGET;
        int tsize = target.length();
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            int fitness = 0;
            for (int j = 0; j < tsize; j++)
            {
                fitness += Math.abs((population.get(i).str.charAt(j) - target.charAt(j)));
            }
            population.get(i).fitness = fitness;
        }
    }




    public static void main (String[] args)
    {
        Main._genes.add(new AlgoGene());
    }
}
