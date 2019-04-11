package com.AILab3;

import com.AILab3.Entities.Gene;
import com.AILab3.Entities.KnapsackGene;
import com.AILab3.Entities.StringGene;
import com.AILab3.GeneticAlgo.Utility;

import static com.AILab3.GeneticAlgo.Constants.*;
import static com.AILab3.GeneticAlgo.Tests.testing;
import java.util.Vector;


@SuppressWarnings("WeakerAccess")
public class Main
{

    public static void textMain ()
    {
        long totalElapsed = System.nanoTime();
        long generationElapsed = System.nanoTime();
        long time = 0;

        Vector<Gene> population = new Vector<>(), buffer = new Vector<>();
        float[] averages;
        // Select algorithms
        StringGene.aging = false;                             // Survivor Selection - True for aging, False for elitism
        StringGene.fitnessAlgo = "bull";                // Fitness - bull, default
        StringGene.selectionAlgo = "tournament";        // Selection - sus, tournament, default
        StringGene.mutationAlgo = "uniform";            // Mutation - onePoint, uniform

        StringGene.initPopulation(GA_TARGET, population);

        for (int generationNumber = 0; generationNumber < GA_MAXITER; generationNumber++)
        {
            population.forEach(Gene::updateFitness);

            averages = Utility.calcPopMeanVarGeneric(population);   // Calculate mean and variance fitness
            Utility.printMeanVariance(averages);                            // Print mean and variance fitness
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


    public static void queensMain ()
    {

    }


    public static void knapsackMain ()
    {
        long totalElapsed = System.nanoTime();
        long generationElapsed = System.nanoTime();
        long time = 0;
        Vector<Gene> population = new Vector<>();
        Vector<Gene> buffer = new Vector<>();
        float[] averages;
        // Survivor Selection - True for aging, False for elitism
        // Select algorithms
        KnapsackGene.aging = false;                             // Survivor Selection - True for aging, False for elitism
        KnapsackGene.fitnessAlgo = "default";                   // Fitness - default
        KnapsackGene.selectionAlgo = "sus";                     // Selection - sus, tournament, default
        KnapsackGene.mutationAlgo = "uniform";                  // Mutation - onePoint, uniform

        int numOfItems = KnapsackGene.parseProblem(1);
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
            if ((population).get(0).fitness <= 370) break;

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

    public static void main (String[] args)
    {
        String mode = "knapsack";
        switch (mode)
        {
            case "string":
                textMain();
                break;
            case "test":
                testing();
                break;
            case "queens":
                queensMain();
                break;
            case "knapsack":
                knapsackMain();
                break;
            default:
                break;
        }
    }
}
