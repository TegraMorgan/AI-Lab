package com.AILab3;

import com.AILab3.Entities.AlgoGene;
import com.AILab3.Solution.Solution;

import java.util.Random;
import java.util.Vector;

@SuppressWarnings("WeakerAccess")
public class Main
{
    //#region Constants and static members
    public static final int GA_POPSIZE = 2048;
    public static final int GA_MAXITER = 16384;
    public static final float GA_ELITRATE = 0.10f;
    public static final float GA_MUTATIONRATE = 0.25f;
    public static final int RAND_MAX = GA_POPSIZE * 4;
    public static final float GA_MUTATION = RAND_MAX * GA_MUTATIONRATE;
    public static final String GA_TARGET = "Hello world!";
    static Random r = new Random();
    //#endregion

    //#region Utility functions
    public static void initPopulation (Vector<AlgoGene> population, Vector<AlgoGene> buffer)
    {
        int targetSize = GA_TARGET.length();
        int age_factor = GA_POPSIZE / 5 + 1;
        StringBuilder sb = new StringBuilder(targetSize);
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            AlgoGene citizen = new AlgoGene();
            for (int j = 0; j < targetSize; j++)
                sb.append((char) ((r.nextInt(RAND_MAX) % 90) + 32));
            citizen.str = sb.toString();
            citizen.age = i / age_factor;
            population.add(citizen);
            sb.delete(0, sb.length());
        }
        //buffer.setSize(GA_POPSIZE);
        buffer.addAll(population);
    }

    public static void printBest (Vector<AlgoGene> gav)
    { System.out.println("Best: " + gav.get(0).str + " (" + gav.get(0).fitness + ")"); }

    //#endregion

    //#region Fitness functions

    /**
     * Function that calculates fitness of a given population
     *
     * @param population
     * @return
     */
    public static void calcFitness (Vector<AlgoGene> population, String method)
    {
        switch (method)
        {
            case "bull":
                bulPgiaFitness(population);
                break;
            default:
                defaultFitness(population);
                break;
        }
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
            _p.get(i).inverseFitness = (5 * _l) - _f;
        }
    }

    public static void defaultFitness (Vector<AlgoGene> population)
    {
        String target = GA_TARGET;
        int tsize = target.length();
        int worst = 89 * tsize;
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            int fitness = 0;
            for (int j = 0; j < tsize; j++)
            {
                int cf = Math.abs((population.get(i).str.charAt(j) - target.charAt(j)));
                fitness += cf;
            }
            population.get(i).fitness = fitness;
        }
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            population.get(i).inverseFitness = worst - population.get(i).fitness;
        }
    }
    //#endregion

    //#region Selection Algorithms

    /**
     * Randomly select those who will stay
     *
     * @param population the population
     * @param ark        the population that will be saved
     * @param eliteSize  size of the population to be saved
     */
    public static void stochasticUniversalSampling (Vector<AlgoGene> population, Vector<AlgoGene> ark,
                                                    int eliteSize, String mutationMethod, boolean aging)
    {
        int[] aggregateInvertFitness = new int[GA_POPSIZE];
        int first = 0;
        if (aging)
            // We don't want the children to reproduce, children have age on 1 now
            // We sorted the population by age already
            while (population.get(first).age < 2) first++;
        // invert fitness value
        aggregateInvertFitness[first] = population.get(first).inverseFitness;
        for (int i = first + 1; i < GA_POPSIZE; i++)
            // put all fitness values into array for future use
            aggregateInvertFitness[i] = aggregateInvertFitness[i - 1] + (population.get(i).inverseFitness);
        // distance between the drawn fitness values
        // the last element of aggregate function will always contain the total sum
        int rouletteSize = aging ? (int) ((GA_POPSIZE - eliteSize) * 0.5d) : eliteSize * 2;
        Vector<AlgoGene> roulette = new Vector<>();
        int fitnessJump = (aggregateInvertFitness[GA_POPSIZE - 1] / rouletteSize);
        // start randomly between 0 and the jump distance
        int start = r.nextInt(fitnessJump);
        int populationIterator = first;
        // Fill the roulette
        for (int rouletteIterator = 0; rouletteIterator < rouletteSize; rouletteIterator++)
        {
            while (aggregateInvertFitness[populationIterator] < (rouletteIterator * fitnessJump) + start)
                populationIterator++;
            roulette.add(population.get(populationIterator));
        }
        int one, two;
        for (int arkIterator = eliteSize; arkIterator < GA_POPSIZE; arkIterator++)
        {
            do
            {
                one = r.nextInt(rouletteSize);
                two = r.nextInt(rouletteSize);
            } while (one == two);
            // Add the element to the ark
            switch (mutationMethod)
            {
                case "onePoint":
                    ark.setElementAt(onePointCrossover(roulette.get(one), roulette.get(two)), arkIterator);
                    break;
                default:
                    break;
            }
        }
    }

    public static void randomSampling (Vector<AlgoGene> population, Vector<AlgoGene> buffer,
                                       String method, int selection_size, boolean aging)
    {
        int i1, i2, f = 0, l = GA_POPSIZE - 1, m = GA_POPSIZE;
        if (aging)
        {
            while (population.get(f).age < 1) f++;
            while (population.get(l).age > 3) l--;
            m = l - f + 1;
        }
        for (int i = selection_size; i < GA_POPSIZE; i++)
        {
            do
            {
                i1 = r.nextInt(m) + f;
                i2 = r.nextInt(m) + f;
            } while (i1 == i2);

            switch (method)
            {
                case "onePoint":
                    buffer.setElementAt(onePointCrossover(population.get(i1), population.get(i2)), i);
                    break;
                default:
                    break;
            }
        }
    }

    private static void tournamentSelection (Vector<AlgoGene> pop,
                                             Vector<AlgoGene> ark,
                                             int ssize,
                                             String mutationMethod)
    {
        int sampleSize = 10;
        Vector<AlgoGene> sample = new Vector<>(sampleSize);
        int _w = pop.get(GA_POPSIZE - 1).inverseFitness;
        int _mf1, _mf2, i1, i2;
        for (int i = ssize; i < GA_POPSIZE; i++)
        {
            _mf1 = _mf2 = _w;
            i1 = i2 = 0;
            for (int j = 0; j < sampleSize; j++)
            {
                // select random member from the top 50%
                sample.setElementAt(pop.get(r.nextInt(GA_POPSIZE / 2)), j);
                if (sample.get(j).fitness < _mf1)
                {
                    _mf2 = _mf1;
                    i2 = i1;
                    _mf1 = sample.get(j).fitness;
                    i1 = j;
                } else if (sample.get(j).fitness < _mf2)
                {
                    _mf2 = sample.get(j).fitness;
                    i2 = j;
                }
            }
            switch (mutationMethod)
            {
                case "onePoint":
                    ark.setElementAt(onePointCrossover(sample.get(i1), sample.get(i2)), i);
                    break;
                default:
                    break;
            }

        }
    }

    private static void elitism (Vector<AlgoGene> population, Vector<AlgoGene> ark, int eliteSize, boolean aging)
    {
        for (int i = 0; i < eliteSize; i++)
        {
            ark.set(i, population.get(i));
            if (aging) ark.get(i).age++;
        }
    }

    //#endregion

    //#region Crossover functions
    public static AlgoGene onePointCrossover (AlgoGene one, AlgoGene two)
    {
        AlgoGene ret = new AlgoGene();
        int tsize = GA_TARGET.length();
        int spos = (r.nextInt(tsize));
        ret.str = one.str.substring(0, spos) + two.str.substring(spos, tsize);
        if (r.nextInt(RAND_MAX) < GA_MUTATION) mutateOnePoint(ret);
        return ret;
    }

    public static void mutateOnePoint (AlgoGene member)
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
        member.str = sb.toString();
    }
    //#endregion

    //#region GA main functions
    public static void selection (Vector<AlgoGene> population, Vector<AlgoGene> survivors,
                                  String selectionMethod, String mutationMethod, boolean aging)
    {
        int selection_size;
        if (aging)
        {
            // Aging
            selection_size = GA_POPSIZE - 1;
            population.sort(AlgoGene.BY_AGE);
            while (population.get(selection_size).age > 3) selection_size--;
        } else
            // Regular elitism
            selection_size = (int) (GA_POPSIZE * GA_ELITRATE);
        elitism(population, survivors, selection_size, aging);
        switch (selectionMethod)
        {
            case "sus":
                stochasticUniversalSampling(population, survivors, selection_size, mutationMethod, aging);
                break;
            default:
                randomSampling(population, survivors, mutationMethod, selection_size, aging);
        }
    }
    //#endregion

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
            long totalElapsed = System.nanoTime();
            long generationElapsed = System.nanoTime();
            long time = 0;
            Vector<AlgoGene> population = new Vector<>(), buffer = new Vector<>();
            Vector<AlgoGene> temp;
            float[] averages;
            boolean aging = true;
            initPopulation(population, buffer);
            // TODO tick() https://www.geeksforgeeks.org/clock-tick-method-in-java-with-examples/
            // System.currentTimeMillis()
            /*
            Algorithm list:
            Fitness - bull, default
            Selection - sus, default
            Mutation - onePoint
            Survivor Selection - True for aging, False for elitism
             */
            for (int generationNumber = 0; generationNumber < GA_MAXITER; generationNumber++)
            {
                calcFitness(population, "bull");                 // calculate fitness and determine the worst possible score
                averages = Solution.calcPopMeanVar(population);         // Calculate mean and variance fitness
                Solution.printMeanVariance(averages);                   // Print mean and variance fitness
                population.sort(AlgoGene.BY_FITNESS);                   // sort Population
                printBest(population);                                  // print the best one
                if ((population).get(0).fitness == 0) break;
                // mate the population together
                selection(population, buffer, "sus", "onePoint", aging);
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
            System.out.println("Total runtime: " + ((time - totalElapsed) / 1000000) + "." + (((time - totalElapsed) / 1000) % 1000) + " milliseconds");
        }
    }


}
