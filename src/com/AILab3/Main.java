package com.AILab3;

import com.AILab3.Entities.AlgoGene;
import com.AILab3.Solution.Solution;

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

    //#region Fitness
    public static void calcFitness (Vector<AlgoGene> population)
    {
        bulPgiaFitness(population);
    }

    public static void bulPgiaFitness (Vector<AlgoGene> _p)
    {
        /*
        One of the basic rules of the game is that the target word cannot contain
        duplicate letters. This algorithm ranking suffers if target OR guess
        contain duplicate letters
         */
        /* Target Length */
        int _l = GA_TARGET.length();
        /* Fitness */
        int _f;
        /* Gene */
        String _g;
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            /*
            Reset fitness
            If we have a match fitness will become 0
             */
            _f = _l * 5;
            _g = _p.get(i).str;
            for (int k = 0; k < _l; k++)
            {
                if (GA_TARGET.charAt(k) == _g.charAt(k))
                    _f -= 5;
                else
                    for (int j = 0; j < _l; j++)
                    {
                        if (_g.charAt(k) == GA_TARGET.charAt(j))
                        {
                            _f -= 2;
                            break;
                        }
                    }
            }
            _p.get(i).fitness = _f;
        }
    }

    public static void defaultFitness (Vector<AlgoGene> population)
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
    //#endregion

    @SuppressWarnings("unchecked")
    public static void sortByFitness (Vector<AlgoGene> population)
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

    public static void printBest (Vector<AlgoGene> gav)
    { System.out.println("Best: " + gav.get(0).str + " (" + gav.get(0).fitness + ")"); }

    //#region Testing
    public static void testing ()
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
    }
    //#endregion

    public static void main (String[] args) throws Exception
    {
        boolean testing = false;
        if (testing) testing();
        else
        {
            long totalelapsed = System.nanoTime();
            long generationElapsed = System.nanoTime();
            long time = 0;
            Vector<AlgoGene> pop_alpha = new Vector<>(), pop_beta = new Vector<>();
            Vector<AlgoGene> population, buffer;
            Vector<AlgoGene> temp;
            initPopulation(pop_alpha, pop_beta);
            population = pop_alpha;
            buffer = pop_beta;
            float[] averages;

            for (int generationNumber = 0; generationNumber < GA_MAXITER; generationNumber++)
            {
                calcFitness(population);                                // calculate fitness
                averages = Solution.calcPopMeanVar(population);         // Calculate mean and variance fitness
                Solution.printMeanVariance(averages);                   // Print mean and variance fitness
                sortByFitness(population);                              // sort them
                printBest(population);                                  // print the best one
                if ((population).get(0).fitness == 0) break;
                mate(population, buffer);                               // mate the population together
                //#region Swap(population,buffer)
                // There is no pass by reference in Java. Thus the swapping will not be
                // extracted to method but done in the main function instead
                temp = population;
                population = buffer;
                buffer = temp;
                //#endregion
                time = System.nanoTime();
                System.out.println("Generation :" + generationNumber + " | " + ((time - generationElapsed) / 1000) + " microseconds");
                generationElapsed = time;
            }
            System.out.println("Total runtime: " + ((time - totalelapsed) / 1000000) + "." + (((time - totalelapsed) / 1000) % 1000) + " miliseconds");
        }
    }


}
