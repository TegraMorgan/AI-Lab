package com.AILab3;


import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.KnapsackGene;
import com.AILab3.Entities.Interfaces.*;
import com.AILab3.GeneticAlgo.Utility;

import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.GA_MAXITER;
import static com.AILab3.GeneticAlgo.Constants.GA_TARGET;


public class Main
{

    public static void main (String[] args)
    {
        /*
        Possible arguments
        Arg 0 - string             | queens             | knapsack
        Arg 1 - default            | bull (string)      |
        Arg 2 - sus                | tournament         | default
        Arg 3 - onePoint (!queens) | uniform (!queens)  |
        Arg 4 - aging              | elitism            |
        Arg 5 - default (simlarty) | variance           | noDetection
        Arg 6 - hyper              | niche              | default (Random immigrants)
         */
        String[] ag = new String[]{"string", "bull", "sus", "uniform", "elitism", "noDetection", "default"};
        String[] param = Utility.checkUserParameters(ag);
        if (param == null) return;
        String mode = param[0];
        IPopType pt = Utility.ExtractPopulationType(param);
        ISelectionAlgo sa = Utility.ExtractSelectionAlgo(param);
        IFitnessAlgo fa = Utility.ExtractFitnessAlgo(param);
        IMutationAlgo ma = Utility.ExtractMutationAlgo(param);
        ILocalOptimaSignals los = Utility.ExtractLocalOptimumSignal(param);
        IEscapeLocalOptimum elo = Utility.ExtractEscapeLocalOptimum(param);
        /* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
        int problemsCount;
        if (param[0].equals("knapsack")) problemsCount = 9;
        else problemsCount = 1;
        Gene.initGene(pt, fa, sa, ma, los, elo, args[4].equals("aging"));
        for (int problem = 0; problem < problemsCount; problem++)
        {
            Object populationInitData;
            long startTime = System.nanoTime();
            Vector<Gene> population = new Vector<>(), buffer = new Vector<>();
            float[] averages;
            switch (mode)
            {
                case "string":
                    populationInitData = GA_TARGET;
                    break;
                case "queens":
                    populationInitData = 300;
                    break;
                case "knapsack":
                    populationInitData = KnapsackGene.parseProblem(problem);
                    System.out.println("Problem " + problem);
                    break;
                default:
                    populationInitData = 0;
                    break;
            }
            Gene.initPopulation(populationInitData, population);
            for (int generationNumber = 0; generationNumber < GA_MAXITER; generationNumber++)
            {
                population.forEach(Gene::updateFitness);                // Fitness
                population.sort(Gene.BY_FITNESS);                       // sort Population
                averages = Utility.calcPopMeanVariance(population);     // Calculate mean and variance fitness
                Utility.output(averages, population, generationNumber);
                if ((population).get(0).isSolution())
                {
                    System.out.println("Solved in " + generationNumber);
                    break;            // if solution found - exit
                }
                if (generationNumber == GA_MAXITER - 1)
                    System.out.println("Not solved");
                Gene.selection(population, buffer); // Select parents and survivors
                // Future Parents are now in population, survivors in buffer
                Gene.mutation(population, buffer);
                // swap pop and buffer, reset buffer
                population = buffer;
                buffer = new Vector<>();
            }
            /* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
            long runTime = System.nanoTime() - startTime;
            System.out.println("Total runtime: " + (runTime / 1000000) + "." + ((runTime / 1000) % 1000) + " milliseconds");
        }
    }
}
