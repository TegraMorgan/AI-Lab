package com.AILab3.GeneticAlgo;

import com.AILab3.Entities.Fitness.BullPgiaStringFitness;
import com.AILab3.Entities.Fitness.KnapsackDefaultFitness;
import com.AILab3.Entities.Fitness.QueensThreatFitness;
import com.AILab3.Entities.Fitness.StringDefaultFitness;
import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Interfaces.*;
import com.AILab3.Entities.LocalOptimaDetecion.EmptyOptimaDetection;
import com.AILab3.Entities.LocalOptimaDetecion.SimilarityBasedDetection;
import com.AILab3.Entities.LocalOptimaDetecion.VarianceBasedDetection;
import com.AILab3.Entities.LocalOptimaEscape.HyperMutation;
import com.AILab3.Entities.LocalOptimaEscape.NichePenalty;
import com.AILab3.Entities.LocalOptimaEscape.RandomImmigrants;
import com.AILab3.Entities.Mutations.*;
import com.AILab3.Entities.Populations.KnapsackPopulation;
import com.AILab3.Entities.Populations.QueenPopulation;
import com.AILab3.Entities.Populations.StringPopulation;
import com.AILab3.Entities.Selections.RandomSampling;
import com.AILab3.Entities.Selections.StochasticUniversalSampling;
import com.AILab3.Entities.Selections.TournamentSelection;

import java.util.Vector;

public class Utility
{

    private static int currentBest = Integer.MAX_VALUE;

    private static void printMeanVariance (float[] averages)
    {
        System.out.println("Population average fitness: " + averages[0] + " | Population standard deviation: " + Math.sqrt(averages[1]));
    }

    /**
     * Calculates population mean and variance
     *
     * @param population Population
     * @return Array of floats with population mean and variance
     */
    public static float[] calcPopMeanVariance (Vector<Gene> population)
    {
        float[] res = new float[2];
        int t, k = population.get(0).fitness;
        float Ex = 0;
        float Ex2 = 0;
        int popSize = population.size();
        for (Gene gene : population)
        {
            t = gene.fitness - k;
            Ex += t;
            Ex2 += t * t;
        }
        Ex2 = (Ex2 - ((Ex * Ex) / popSize)) / (popSize - 1);
        Ex = k + (Ex / popSize);
        res[0] = Ex;
        res[1] = Ex2;
        return res;
    }

    public static String[] checkUserParameters (String[] args)
    {
        int argc = 7;
        String[] param = new String[argc];
        boolean argsOK = true;
        if (args.length >= 1 && args.length <= argc - 1)
        {
            switch (args[0])
            {
                case "queens":
                case "string":
                case "knapsack":
                    System.out.println("Insufficient parameters. All parameters will be default");
                    param[0] = args[0];
                    param[1] = "default";
                    param[2] = "default";
                    param[3] = "uniform";
                    param[4] = "elitism";
                    param[5] = "default";
                    param[6] = "default";
                    break;
                default:
                    System.out.println("Cannot parse \"" + args[0] + "\"");
                    argsOK = false;
            }
        } else if (args.length >= argc)
        {
            switch (args[0])
            {
                case "queens":
                case "string":
                case "knapsack":
                    param[0] = args[0];
                    break;
                default:
                    System.out.println("Cannot parse \"" + args[0] + "\"");
                    argsOK = false;
            }
            switch (args[1])
            {
                case "bull":
                    if (param[0].equals("string")) param[1] = args[1];
                    else
                    {
                        System.out.println("Only string problem can work with bull. Opting to default.");
                        param[1] = "default";
                    }
                    break;
                case "default":
                    param[1] = args[1];
                    break;
                default:
                    System.out.println(args[1] + " is unknown. Using default fitness method instead");
                    param[1] = "default";
            }
            switch (args[2])
            {
                case "sus":
                case "tournament":
                case "default":
                    param[2] = args[2];
                    break;
                default:
                    System.out.println(args[2] + " is unknown. Using default selection method instead");
                    param[2] = "default";
            }
            switch (args[3])
            {
                case "onePoint":
                case "uniform":
                    param[3] = args[3];
                    break;
                default:
                    System.out.println(args[3] + " is unknown. Using uniform mutation method instead");
                    param[3] = "uniform";
            }
            switch (args[4])
            {
                case "aging":
                case "elitism":
                    param[4] = args[4];
                    break;
                default:
                    System.out.println(args[4] + " is unknown. Using elitism");
                    param[3] = "elitism";
            }
            switch (args[5])
            {
                case "default":
                case "simlarty":
                case "variance":
                case "noDetection":
                    param[5] = args[5];
                    break;
                default:
                    System.out.println(args[5] + " is unknown. Using default");
                    param[5] = "default";
                    break;
            }
            switch (args[6])
            {
                case "default":
                case "niche":
                case "hyper":
                    param[6] = args[6];
                    break;
                default:
                    System.out.println(args[6] + " is unknown. Using default");
                    param[6] = "default";
                    break;
            }
        } else
        {
            argsOK = false;
            System.out.println("No parameters detected.");
            System.out.println("Please at least state type of problem to be solved - string|queens|knapsack.");
            System.out.println("Exiting.");
        }
        if (!argsOK) return null;
        else return param;
    }

    public static long[] aggregateInvertFitness (int popsize, int f, Vector<Gene> p)
    {
        long[] result = new long[popsize];
        for (int i = f + 1; i < popsize; i++)
            // put all fitness values into array for future use
            result[i] = result[i - 1] + (p.get(i).inverseFitness);
        return result;
    }

    public static void copyTop (Vector<Gene> population, Vector<Gene> ark, int eliteSize, boolean aging)
    {
        ark.clear();
        for (int i = 0; i < eliteSize; i++)
        {
            ark.add(population.get(i));
            if (aging) ark.get(i).age++;
        }
    }

    public static ISelectionAlgo ExtractSelectionAlgo (String[] args)
    {
        ISelectionAlgo sa;
        switch (args[2])
        {
            case "sus":
                sa = new StochasticUniversalSampling();
                break;
            case "tournament":
                sa = new TournamentSelection();
                break;
            case "default":
            default:
                sa = new RandomSampling();
                break;
        }
        return sa;
    }

    public static IFitnessAlgo ExtractFitnessAlgo (String[] args)
    {
        IFitnessAlgo fa;
        switch (args[0])
        {
            case "string":
                switch (args[1])
                {
                    case "bull":
                        fa = new BullPgiaStringFitness();
                        break;
                    case "default":
                    default:
                        fa = new StringDefaultFitness();
                        break;
                }
                break;
            case "queens":
                fa = new QueensThreatFitness();
                break;
            case "knapsack":
                fa = new KnapsackDefaultFitness();
                break;
            default:
                fa = null;
                break;
        }
        return fa;
    }

    public static IMutationAlgo ExtractMutationAlgo (String[] args)
    {
        IMutationAlgo ma;
        switch (args[0])
        {
            case "string":
                switch (args[3])
                {
                    case "onePoint":
                        ma = new StringOnePointCrossover();
                        break;
                    case "uniform":
                    default:
                        ma = new StringUniformCrossover();
                        break;
                }
                break;
            case "queens":
                ma = new QueensUniformShuffle();
                break;
            case "knapsack":
                switch (args[3])
                {
                    case "onePoint":
                        ma = new KnapsackOnePointCrossover();
                        break;
                    case "uniform":
                    default:
                        ma = new KnapsackUniformCrossover();
                        break;
                }
                break;
            default:
                ma = null;
                break;
        }
        return ma;
    }

    public static ILocalOptimaSignals ExtractLocalOptimumSignal (String[] param)
    {
        switch (param[5])
        {
            case "variance":
                return new VarianceBasedDetection();
            case "noDetection":
                return new EmptyOptimaDetection();
            default:
                return new SimilarityBasedDetection();
        }
    }

    public static IEscapeLocalOptimum ExtractEscapeLocalOptimum (String[] param)
    {
        switch (param[6])
        {
            case "niche":
                return new NichePenalty();
            case "hyper":
                return new HyperMutation();
            default:
                return new RandomImmigrants();
        }
    }

    public static void output (float[] averages, Vector<Gene> population, int generation)
    {
        Gene b = population.get(0);
        if (currentBest > b.fitness)
        {
            currentBest = b.fitness;
            System.out.println(b.fitness + "," + generation);
        }
        //Utility.printMeanVariance(averages);              // Print mean and variance fitness
    }

    public static IPopType ExtractPopulationType (String[] param)
    {
        switch (param[0])
        {
            case "string":
                return new StringPopulation();
            case "queens":
                return new QueenPopulation();
            case "knapsack":
                return new KnapsackPopulation();
            default:
                return null;
        }
    }
}
