package com.AILab3.GeneticAlgo;

import com.AILab3.Entities.AlgoGene;
import com.AILab3.Main;

import java.util.Vector;

public class Selction
{
    public static void selection (Vector<AlgoGene> population, Vector<AlgoGene> ark,
                                  String selectionMethod, boolean aging)
    {
        int selection_size;
        Vector<AlgoGene> parents = new Vector<>();
        if (aging)
        {
            // Aging
            selection_size = Main.GA_POPSIZE - 1;
            population.sort(AlgoGene.BY_AGE);
            while (population.get(selection_size).age > 3) selection_size--;
        } else
            // Regular elitism
            selection_size = (int) (Main.GA_POPSIZE * Main.GA_ELITRATE);
        copyTop(population, ark, selection_size, aging);

        // Select parents
        switch (selectionMethod)
        {
            case "sus":
                stochasticUniversalSampling(population, parents, Main.GA_POPSIZE - selection_size, aging);
                break;
            case "tournament":
                tournamentSelection(population, parents, Main.GA_POPSIZE - selection_size);
                break;
            default:
                randomSampling(population, parents, Main.GA_POPSIZE - selection_size, aging);
        }
        // Replace population with potential parents
        population.clear();
        population.addAll(parents);
    }

    /**
     * Randomly select those who will stay
     *
     * @param population                 the population
     * @param parentsRoulette            the population that will be saved
     * @param amountOfChildrenToGenerate size of the population to be saved
     */
    public static void stochasticUniversalSampling (Vector<AlgoGene> population, Vector<AlgoGene> parentsRoulette,
                                                    int amountOfChildrenToGenerate, boolean aging)
    {
        parentsRoulette.clear();
        int[] aggregateInvertFitness = new int[Main.GA_POPSIZE];
        int first = 0;
        if (aging)
            // We don't want the children to reproduce, children have age of 1 now
            // We sorted the population by age already
            while (population.get(first).age < 2) first++;
        // invert fitness value
        aggregateInvertFitness[first] = population.get(first).inverseFitness;
        for (int i = first + 1; i < Main.GA_POPSIZE; i++)
            // put all fitness values into array for future use
            aggregateInvertFitness[i] = aggregateInvertFitness[i - 1] + (population.get(i).inverseFitness);
        // distance between the drawn fitness values
        // the last element of aggregate function will always contain the total sum
        int rouletteSize = aging ? amountOfChildrenToGenerate * 2 : Main.GA_POPSIZE / 4;
        int fitnessJump = (aggregateInvertFitness[Main.GA_POPSIZE - 1] / rouletteSize);
        // start randomly between 0 and the jump distance
        int start = Main.r.nextInt(fitnessJump);
        int populationIterator = first;
        // Fill the parents
        for (int parentRouletteIterator = 0; parentRouletteIterator < rouletteSize; parentRouletteIterator++)
        {
            while (aggregateInvertFitness[populationIterator] < (parentRouletteIterator * fitnessJump) + start)
                populationIterator++;
            parentsRoulette.add(population.get(populationIterator));
        }
    }

    public static void randomSampling (Vector<AlgoGene> population, Vector<AlgoGene> parents,
                                       int childrenToGenerate, boolean aging)
    {
        parents.clear();
        int i1, i2, f = 0, l = Main.GA_POPSIZE - 1, m = Main.GA_POPSIZE;
        if (aging)
        {
            while (population.get(f).age < 1) f++;
            while (population.get(l).age > 3) l--;
            m = l - f + 1;
        }
        for (int i = childrenToGenerate; i < Main.GA_POPSIZE; i++)
        {
            do
            {
                i1 = Main.r.nextInt(m) + f;
                i2 = Main.r.nextInt(m) + f;
            } while (i1 == i2);
            parents.add(population.get(i1));
            parents.add(population.get(i2));
        }
    }

    private static void tournamentSelection (Vector<AlgoGene> pop, Vector<AlgoGene> parents, int childrenToGenerate)
    {
        parents.clear();
        int sampleSize = 10;
        Vector<AlgoGene> tournament = new Vector<>(sampleSize);
        int _w = pop.get(Main.GA_POPSIZE - 1).fitness;
        int _mf1, _mf2, i1, i2;
        for (int i = 0; i < childrenToGenerate; i++)
        {
            _mf1 = _mf2 = _w;
            i1 = i2 = 0;
            for (int j = 0; j < sampleSize; j++)
            {
                // select random member from the top 50%
                tournament.add(pop.get(Main.r.nextInt(Main.GA_POPSIZE / 2)));
                // in the end two best will be selected
                if (tournament.get(j).fitness < _mf1)
                {
                    _mf2 = _mf1;
                    i2 = i1;
                    _mf1 = tournament.get(j).fitness;
                    i1 = j;
                } else if (tournament.get(j).fitness < _mf2)
                {
                    _mf2 = tournament.get(j).fitness;
                    i2 = j;
                }
            }
            parents.add(tournament.get(i1));
            parents.add(tournament.get(i2));
            tournament.clear();
        }
    }

    private static void copyTop (Vector<AlgoGene> population, Vector<AlgoGene> ark, int eliteSize, boolean aging)
    {
        ark.clear();
        for (int i = 0; i < eliteSize; i++)
        {
            ark.add(population.get(i));
            if (aging) ark.get(i).age++;
        }
    }
}
