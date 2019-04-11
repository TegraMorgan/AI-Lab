package com.AILab3.Entities;

import java.util.Comparator;
import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.GA_ELITRATE;
import static com.AILab3.GeneticAlgo.Constants.GA_POPSIZE;
import static com.AILab3.GeneticAlgo.Selection.*;

public abstract class Gene
{
    public static final ByFitness BY_FITNESS = new ByFitness();
    private static final ByAge BY_AGE = new ByAge();
    public static String fitnessAlgo = "";
    public static String selectionAlgo = "";
    public static String mutationAlgo = "";
    public static boolean aging = false;
    public int fitness;        // Genetic fitness of the gene - less is better
    public int inverseFitness; // Inverse value of fitness - greater is better
    public int age;

    Gene (int _f, int _a, int _if)
    {
        fitness = _f;
        age = _a;
        inverseFitness = _if;
    }

    public static void selection (Vector<Gene> population, Vector<Gene> ark)
    {
        int selection_size;
        Vector<Gene> parents = new Vector<>();
        if (aging)
        {
            // Aging
            selection_size = GA_POPSIZE - 1;
            population.sort(Gene.BY_AGE);
            while (population.get(selection_size).age > 3) selection_size--;
        } else
            // Regular elitism
            selection_size = (int) (GA_POPSIZE * GA_ELITRATE);
        copyTop(population, ark, selection_size, aging);

        // Select parents
        switch (selectionAlgo)
        {
            case "sus":
                stochasticUniversalSampling(population, parents, GA_POPSIZE - selection_size, aging);
                break;
            case "tournament":
                tournamentSelection(population, parents, GA_POPSIZE - selection_size);
                break;
            default:
                randomSampling(population, parents, GA_POPSIZE - selection_size, aging);
        }
        // Replace population with potential parents
        population.clear();
        population.addAll(parents);
    }

    public abstract boolean isSolution ();

    public abstract void updateFitness ();

    public static class ByAge implements Comparator<Gene>
    {
        @Override
        public int compare (Gene o1, Gene o2)
        {
            return Integer.compare((o1).age, (o2).age);
        }
    }

    public static class ByFitness implements Comparator<Gene>
    {
        @Override
        public int compare (Gene o1, Gene o2)
        {
            return Integer.compare((o1).fitness, (o2).fitness);
        }
    }

}
