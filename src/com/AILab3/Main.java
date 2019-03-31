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
    public static final int RAND_MAX = 2;
    public static final float GA_MUTATION = RAND_MAX * GA_MUTATIONRATE;
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

    boolean fitness_sort (AlgoGene x, AlgoGene y)
    { return (x.fitness < y.fitness); }

    void sort_by_fitness (Vector<AlgoGene> population)
    {
        population.sort(AlgoGene.BY_FITNESS);
    }

    /**
     * Select only the best for breeding
     *
     * @param population all the population
     * @param buffer     empty buffer
     * @param esize      size of the elite
     */
    void elitism (Vector<AlgoGene> population, Vector<AlgoGene> buffer, int esize)
    {

        for (int i = 0; i < esize; i++)
        {
            buffer.get(i).str = population.get(i).str;
            buffer.get(i).fitness = population.get(i).fitness;
        }
    }

    void mutate (AlgoGene member)
    {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        int tsize = GA_TARGET.length();
        int ipos = r.nextInt() % tsize;
        int delta = (r.nextInt() % 90) + 32;
        if (ipos > 0)
            sb.append(member.str, 0, ipos - 1);
        sb.append((char) ((member.str.charAt(ipos) + delta) % 122));
        if (ipos < member.str.length()) sb.append(member.str, ipos + 1, tsize);
        member.str = sb.toString();
    }

    void mate (Vector<AlgoGene> population, Vector<AlgoGene> buffer)
    {
        int esize = (int) (GA_POPSIZE * GA_ELITRATE);
        int tsize = GA_TARGET.length(), spos, i1, i2;
        Random r = new Random();
        elitism(population, buffer, esize);

        // Mate the rest
        for (int i = esize; i < GA_POPSIZE; i++)
        {
            i1 = r.nextInt() % (GA_POPSIZE / 2);
            i2 = r.nextInt() % (GA_POPSIZE / 2);
            spos = r.nextInt() % tsize;
            buffer.get(i).str = population.get(i1).str.substring(0, spos) + population.get(i2).str.substring(spos, tsize - spos);
            if (r.nextInt() < GA_MUTATION) mutate(buffer.get(i));
        }
    }

    void print_best (Vector<AlgoGene> gav)
    { System.out.println("Best: " + gav.get(0).str + " (" + gav.get(0).fitness + ")"); }

    void swap (Vector<AlgoGene> population,
               Vector<AlgoGene> buffer)
    {
        Vector<AlgoGene> temp = population;
        population = buffer;
        buffer = temp;
    }

    public static void main (String[] args)
    {
        Main._genes.add(new AlgoGene());
    }
}
