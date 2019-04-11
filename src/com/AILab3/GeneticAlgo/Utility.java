package com.AILab3.GeneticAlgo;

import com.AILab3.Entities.Gene;

import java.util.Vector;

public class Utility
{

    public static void printMeanVariance (float[] averages)
    {
        System.out.println("Population average fitness: " + averages[0] + " | Population variance: " + averages[1]);
    }

    public static float[] calcPopMeanVarGeneric (Vector<Gene> population)
    {
        float[] res = new float[2];
        int t;
        float avgPopFit = 0;
        float popFitVar = 0;
        for (int i = 0; i < Constants.GA_POPSIZE; i++)
        {
            t = population.get(i).fitness;
            avgPopFit += t;
            popFitVar += t * t;
        }
        popFitVar = (popFitVar - ((avgPopFit * avgPopFit) / Constants.GA_POPSIZE)) / (Constants.GA_POPSIZE - 1);
        avgPopFit /= Constants.GA_POPSIZE;
        res[0] = avgPopFit;
        res[1] = popFitVar;
        return res;
    }

    public static String[] extractUserParameters (String[] args)
    {
        String[] param = new String[5];
        boolean argsOK = true;
        if (args.length >= 1 && args.length <= 4)
        {
            switch (args[0])
            {
                case "string":
                case "queens":
                case "knapsack":
                    System.out.println("Insufficient parameters. All parameters will be default");
                    param[0] = args[0];
                    param[1] = "default";
                    param[2] = "default";
                    param[3] = "uniform";
                    param[4] = "elitism";
                    break;
                default:
                    System.out.println("Cannot parse \"" + args[0] + "\"");
                    argsOK = false;
            }
        } else if (args.length >= 5)
        {
            switch (args[0])
            {
                case "string":
                case "queens":
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
}
