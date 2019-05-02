package com.AILab3.Entities.Genes;

import com.AILab3.Entities.Interfaces.*;
import com.AILab3.GeneticAlgo.Utility;

import java.util.Comparator;
import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.*;

public abstract class Gene
{
    public static final ByFitness BY_FITNESS = new ByFitness();
    private static final ByAge BY_AGE = new ByAge();
    static IFitnessAlgo fitnessAlgo;
    private static ISelectionAlgo selectionAlgo;
    public static IMutationAlgo mutationAlgo;
    private static ILocalOptimaSignals LocalOptimumDetection;
    private static IEscapeLocalOptimum escapeLocalOptimum;
    private static IPopType populationType;
    private static boolean aging = false;
    public int fitness;        // Genetic fitness of the gene - less is better
    public int inverseFitness; // Inverse value of fitness - greater is better
    public int age;

    Gene (int _f, int _a, int _if)
    {
        fitness = _f;
        age = _a;
        inverseFitness = _if;
    }

    public static void initGene (IPopType pt, IFitnessAlgo fa,
                                 ISelectionAlgo sa,
                                 IMutationAlgo ma,
                                 ILocalOptimaSignals los,
                                 IEscapeLocalOptimum elo,
                                 boolean a)
    {
        fitnessAlgo = fa;
        selectionAlgo = sa;
        mutationAlgo = ma;
        LocalOptimumDetection = los;
        escapeLocalOptimum = elo;
        populationType = pt;
        aging = a;
    }

    public static void selection (Vector<Gene> population, Vector<Gene> ark)
    {
        if (LocalOptimumDetection.detectLocalOptima(population)) escapeLocalOptimum.startLocalEscape(population);
        else escapeLocalOptimum.endLocalEscape(population);
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
        Utility.copyTop(population, ark, selection_size, aging);

        // Select parents
        selectionAlgo.selectParents(population, parents, GA_POPSIZE - selection_size, aging);
        // Replace population with potential parents
        population.clear();
        population.addAll(parents);
    }

    public static void mutation (Vector<Gene> parents, Vector<Gene> ark)
    {
        int start = ark.size();
        int psize = parents.size();
        int i1, i2;
        for (int i = start; i < GA_POPSIZE; i++)
        {
            do
            {
                i1 = r.nextInt(psize);
                i2 = r.nextInt(psize);
            } while (i1 == i2);
            ark.add(Gene.mutationAlgo.mutate(parents.get(i1), parents.get(i2)));
        }
    }

    public static void initPopulation (Object n, Vector<Gene> p)
    {
        populationType.initPopulation(n, p);
    }

    public abstract int similar (Gene o);

    public abstract int getProblemSize ();

    public abstract boolean isSolution ();

    public abstract void updateFitness ();

    public static void PrintBest (Vector<Gene> p)
    {
        Gene best = p.get(0);
        String s = best.toString();
        System.out.println("Best: " + s + " (" + best.fitness + ")");
    }

    public abstract void replace ();

    public abstract String toString ();

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
