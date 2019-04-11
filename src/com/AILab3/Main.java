package com.AILab3;


import com.AILab3.Entities.Gene;
import com.AILab3.Entities.KnapsackGene;
import com.AILab3.Entities.QueensPopulation;
import com.AILab3.Entities.StringGene;
import com.AILab3.GeneticAlgo.Utility;

import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.GA_MAXITER;
import static com.AILab3.GeneticAlgo.Constants.GA_TARGET;
import static com.AILab3.GeneticAlgo.Tests.testing;


@SuppressWarnings("WeakerAccess")
public class Main
{

    public static void textMain (String[] args)
    {
        long totalElapsed = System.nanoTime();
        long generationElapsed = System.nanoTime();
        long time = 0;

        Vector<Gene> population = new Vector<>(), buffer = new Vector<>();
        float[] averages;
        // Select algorithms
        if (args.length == 5)
        {
            StringGene.fitnessAlgo = args[1];
            StringGene.selectionAlgo = args[2];
            StringGene.mutationAlgo = args[3];
            StringGene.aging = args[4].equals("aging");
        } else
        {
            StringGene.fitnessAlgo = "bull";                // Fitness - bull, default
            StringGene.selectionAlgo = "tournament";        // Selection - sus, tournament, default
            StringGene.mutationAlgo = "uniform";            // Mutation - onePoint, uniform
            StringGene.aging = false;                       // Survivor Selection - True for aging, False for elitism
        }
        StringGene.initPopulation(GA_TARGET, population);

        for (int generationNumber = 0; generationNumber < GA_MAXITER; generationNumber++)
        {
            population.forEach(Gene::updateFitness);

            averages = Utility.calcPopMeanVarGeneric(population);   // Calculate mean and variance fitness
            Utility.printMeanVariance(averages);                    // Print mean and variance fitness
            population.sort(Gene.BY_FITNESS);                       // sort Population
            StringGene.printBest(population);                       // print the best one
            if ((population).get(0).fitness == 0) break;            // if solution found - exit

            Gene.selection(population, buffer); // Select parents and survivors
            // Future Parents are now in population, survivors in buffer

            StringGene.mutation(population, buffer);

            // swap pop and buffer, reset buffer
            population = buffer;
            buffer = new Vector<>();

            time = System.nanoTime();
            System.out.println("Generation :" + generationNumber + " | " + ((time - generationElapsed) / 1000) + " microseconds");
            generationElapsed = time;
        }
        System.out.println("Total runtime: " + ((time - totalElapsed) / 1000000) + "." + (((time - totalElapsed) / 1000) % 1000) + " milliseconds");
    }


    public static void queensMain (String[] args)
    {
        QueensPopulation q1 = new QueensPopulation(1000, 25, 0f);
        int tests = 10;
        int conflictsCount = 0;
        for (int i = 0; i < tests; i++)
        {
            conflictsCount += q1.repopulate(15).minimalConflicts();
        }
        System.out.println("Average " + (float) conflictsCount / tests);
    }


    public static void knapsackMain (String[] args)
    {
        float[] averages;
        if (args.length == 5)
        {
            StringGene.fitnessAlgo = args[1];
            StringGene.selectionAlgo = args[2];
            StringGene.mutationAlgo = args[3];
            StringGene.aging = args[4].equals("aging");
        } else
        {
            KnapsackGene.fitnessAlgo = "default";                   // Fitness - default
            KnapsackGene.selectionAlgo = "tournament";              // Selection - sus, tournament, default
            KnapsackGene.mutationAlgo = "uniform";                  // Mutation - onePoint, uniform
            KnapsackGene.aging = false;                             // Survivor Selection - True for aging, False for elitism
        }

        for (int p = 1; p < 9; p++)
        {
            Vector<Gene> population = new Vector<>();
            Vector<Gene> buffer = new Vector<>();
            long totalElapsed = System.nanoTime();
            long generationElapsed = System.nanoTime();
            long time = 0;
            int numOfItems = KnapsackGene.parseProblem(p);
            System.out.println("Problem " + p);
            KnapsackGene.initPopulation(numOfItems, population);
            for (int generationNumber = 0; generationNumber < GA_MAXITER; generationNumber++)
            {
                // Fitness
                population.forEach(Gene::updateFitness);
                averages = Utility.calcPopMeanVarGeneric(population);
                Utility.printMeanVariance(averages); // Print mean and variance fitness
                // Sort
                population.sort(Gene.BY_FITNESS);
                KnapsackGene.PrintBest(population);
                if (population.get(0).isSolution())
                {
                    System.out.println("Problem " + p + " solved");
                    break;
                }

                // Selection
                Gene.selection(population, buffer);
                // Population contains potential parents, buffer contains survivors


                // Mutation
                KnapsackGene.mutation(population, buffer);

                // swap pop and buffer, reset buffer
                population = buffer;
                buffer = new Vector<>();

                time = System.nanoTime();
                System.out.println("Generation :" + generationNumber + " | " + ((time - generationElapsed) / 1000) + " microseconds");
                generationElapsed = time;
            }
            System.out.println("Total runtime: " + ((time - totalElapsed) / 1000000) + "." + (((time - totalElapsed) / 1000) % 1000) + " milliseconds");
        }

    }


    public static void main (String[] args)
    {
        String[] param = Utility.extractUserParameters(args);
        if (param == null) return;
        String mode = param[0];
        switch (mode)
        {
            case "string":
                textMain(param);
                break;
            case "test":
                testing();
                break;
            case "queens":
                queensMain(param);
                break;
            case "knapsack":
                knapsackMain(param);
                break;
            default:
                break;
        }
    }
}
