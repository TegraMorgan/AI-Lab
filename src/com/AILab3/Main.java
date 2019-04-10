package com.AILab3;

import com.AILab3.Entities.AlgoGene;
import com.AILab3.GeneticAlgo.*;
import com.AILab3.Solution.Solution;

import java.util.Vector;

@SuppressWarnings("WeakerAccess")
public class Main
{

    public static void textMain ()
    {
        long totalElapsed = System.nanoTime();
        long generationElapsed = System.nanoTime();
        long time = 0;
        Vector<AlgoGene> population = new Vector<>(), buffer = new Vector<>();
        float[] averages;
        boolean aging = false;
        Utility.initPopulation(population, buffer);
        // TODO tick() https://www.geeksforgeeks.org/clock-tick-method-in-java-with-examples/
        // System.currentTimeMillis()
            /*
            Algorithm list:
            Fitness - bull, default
            Selection - sus, tournament, default
            Mutation - onePoint, uniform
            Survivor Selection - True for aging, False for elitism
             */
        for (int generationNumber = 0; generationNumber < Constants.GA_MAXITER; generationNumber++)
        {
            Fitness.calcFitness(population, "bull");                 // calculate fitness and determine the worst possible score
            averages = Solution.calcPopMeanVar(population);         // Calculate mean and variance fitness
            Solution.printMeanVariance(averages);                   // Print mean and variance fitness
            population.sort(AlgoGene.BY_FITNESS);                   // sort Population
            Utility.printBest(population);                                  // print the best one
            if ((population).get(0).fitness == 0) break;
            // Select parents and survivors
            Selction.selection(population, buffer, "tournament", aging);
            // Future Parents are now in population, survivors in buffer
            // Amount of children to produce : GA_POP - buffer.size
            Mutation.mutation(population, buffer, "uniform");
            population = buffer;
            buffer = new Vector<>();
            time = System.nanoTime();
            System.out.println("Generation :" + generationNumber + " | " + ((time - generationElapsed) / 1000) + " microseconds");
            generationElapsed = time;
            System.out.println("Total runtime: " + ((time - totalElapsed) / 1000000) + "." + (((time - totalElapsed) / 1000) % 1000) + " milliseconds");
        }
    }


    public static void queensMain ()
    {

    }

    public static void main (String[] args)
    {
        boolean testing = false;
        boolean queens = true;
        String mode = "queens";
        switch (mode)
        {
            case "string":
                textMain();
                break;
            case "test":
                Tests.testing();
                break;
            case "queens":
                queensMain();
                break;
            default:
                break;
        }
    }
}
