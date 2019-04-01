package com.AILab3;

import com.AILab3.Entities.AlgoGene;

import java.util.Random;
import java.util.Vector;

@SuppressWarnings("WeakerAccess")
public class Main
{
    public static final int GA_POPSIZE = 2048;
    public static final int GA_MAXITER = 16384;
    public static final float GA_ELITRATE = 0.10f;
    public static final float GA_MUTATIONRATE = 0.25f;
    public static final int RAND_MAX = GA_POPSIZE * 4;
    public static final float GA_MUTATION = RAND_MAX * GA_MUTATIONRATE;
    public static final String GA_TARGET = "Hello world!";
    static Random r = new Random();

    public static void initPopulation (Vector<AlgoGene> population, Vector<AlgoGene> buffer)
    {
        int targetSize = GA_TARGET.length();

        StringBuilder sb = new StringBuilder(targetSize);
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            AlgoGene citizen = new AlgoGene();
            for (int j = 0; j < targetSize; j++)
                sb.append((char) ((r.nextInt(RAND_MAX) % 90) + 32));
            citizen.str = sb.toString();
            population.add(citizen);
            sb.delete(0, sb.length());
        }
        //buffer.setSize(GA_POPSIZE);
        buffer.addAll(population);
    }

    public static void calc_fitness (Vector<AlgoGene> population)
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

    public static void sort_by_fitness (Vector<AlgoGene> population)
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
    public static void elitism (Vector<AlgoGene> population, Vector<AlgoGene> buffer, int esize)
    {

        for (int i = 0; i < esize; i++) buffer.set(i, population.get(i));
            /*
            buffer.get(i).str = population.get(i).str;
            buffer.get(i).fitness = population.get(i).fitness;
            */
    }

    public static void mutate (AlgoGene member)
    {

        StringBuilder sb = new StringBuilder();
        int tsize = GA_TARGET.length();
        int ipos = r.nextInt(RAND_MAX) % tsize;
        int delta = (r.nextInt(RAND_MAX) % 90) + 32;
        // Copy beginning
        if (ipos > 0)
            sb.append(member.str, 0, ipos);
        // Mutate one char
        sb.append((char) ((member.str.charAt(ipos) + delta) % 122));
        // Copy end
        if (ipos + 1 < member.str.length())
            sb.append(member.str, ipos + 1, tsize);
        // TODO perhaps later we will inline those three
        member.str = sb.toString();
    }

    public static void mate (Vector<AlgoGene> population, Vector<AlgoGene> buffer)
    {
        int esize = (int) (GA_POPSIZE * GA_ELITRATE);
        int tsize = GA_TARGET.length(), spos, i1, i2;

        elitism(population, buffer, esize);

        // Mate the rest
        for (int i = esize; i < GA_POPSIZE; i++)
        {
            i1 = r.nextInt(RAND_MAX) % (GA_POPSIZE / 2);
            i2 = r.nextInt(RAND_MAX) % (GA_POPSIZE / 2);
            spos = (r.nextInt(RAND_MAX)) % tsize;
            buffer.get(i).str = population.get(i1).str.substring(0, spos) + population.get(i2).str.substring(spos, tsize);
            if (r.nextInt(RAND_MAX) < GA_MUTATION) mutate(buffer.get(i));
        }
    }

    public static void print_best (Vector<AlgoGene> gav)
    { System.out.println("Best: " + gav.get(0).str + " (" + gav.get(0).fitness + ")"); }

    public static void swap (Vector<AlgoGene> population,
                             Vector<AlgoGene> buffer)
    {
        Vector<AlgoGene> temp = population;
        population = buffer;
        buffer = temp;
    }

    public static void main (String[] args)
    {
        boolean testing = false;
        if (testing)
        {
            // Create variables

            StringBuilder sb = new StringBuilder();
            AlgoGene member = new AlgoGene();
            // Initialize
            int targetSize = GA_TARGET.length();
            for (int j = 0; j < targetSize; j++)
                sb.append((char) ((r.nextInt(RAND_MAX) % 90) + 32));
            member.str = sb.toString();
            int le = sb.length();
            sb.delete(0, le);
            // test
            int tsize = GA_TARGET.length();
            int ipos = 11;//r.nextInt(RAND_MAX) % tsize;
            int delta = (r.nextInt(RAND_MAX) % 90) + 32;

            if (ipos > 0)
                sb.append(member.str, 0, ipos);
            sb.append((char) ((member.str.charAt(ipos) + delta) % 122));
            if (ipos + 1 < member.str.length()) sb.append(member.str, ipos + 1, tsize);
            le = sb.length();
            member.str = sb.toString();
        } else
        {
            Vector<AlgoGene> pop_alpha = new Vector<>(), pop_beta = new Vector<>();
            Vector<AlgoGene> population, buffer;

            initPopulation(pop_alpha, pop_beta);
            population = pop_alpha;
            buffer = pop_beta;

            for (int i = 0; i < GA_MAXITER; i++)
            {
                calc_fitness(population);        // calculate fitness
                sort_by_fitness(population);     // sort them
                print_best(population);          // print the best one

                if ((population).get(0).fitness == 0) break;

                mate(population, buffer);        // mate the population together
                swap(population, buffer);        // swap buffers
            }
        }
    }
}
