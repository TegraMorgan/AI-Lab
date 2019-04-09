package com.AILab3;

import com.AILab3.Entities.AlgoGene;
import com.AILab3.GeneticAlgo.*;
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
    public static Random r = new Random();
    //#endregion

    public static void main (String[] args) throws Exception
    {
        boolean testing = false;
        if (testing) Tests.testing();
        else
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
            for (int generationNumber = 0; generationNumber < GA_MAXITER; generationNumber++)
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
            }
            System.out.println("Total runtime: " + ((time - totalElapsed) / 1000000) + "." + (((time - totalElapsed) / 1000) % 1000) + " milliseconds");
        }
    }


}
