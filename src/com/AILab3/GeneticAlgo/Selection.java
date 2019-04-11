package com.AILab3.GeneticAlgo;

import com.AILab3.Entities.Gene;
import com.AILab3.Entities.StringGene;

import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.GA_POPSIZE;
import static com.AILab3.GeneticAlgo.Constants.r;

public class Selection
{
    public static void copyTop (Vector<Gene> population, Vector<Gene> ark, int eliteSize, boolean aging)
    {
        ark.clear();
        for (int i = 0; i < eliteSize; i++)
        {
            ark.add(population.get(i));
            if (aging) ark.get(i).age++;
        }
    }

    public static void stochasticUniversalSampling (Vector<Gene> population,
                                                    Vector<Gene> parentsRoulette,
                                                    int amountOfChildrenToGenerate,
                                                    boolean aging)
    {
        parentsRoulette.clear();
        int[] aggregateInvertFitness = new int[GA_POPSIZE];
        int first = 0;
        if (aging)
            // We don't want the children to reproduce, children have age of 1 now
            // We sorted the population by age already
            while (population.get(first).age < 2) first++;
        // invert fitness value
        aggregateInvertFitness[first] = population.get(first).inverseFitness;
        for (int i = first + 1; i < GA_POPSIZE; i++)
            // put all fitness values into array for future use
            aggregateInvertFitness[i] = aggregateInvertFitness[i - 1] + (population.get(i).inverseFitness);
        // distance between the drawn fitness values
        // the last element of aggregate function will always contain the total sum
        int rouletteSize = aging ? amountOfChildrenToGenerate * 2 : GA_POPSIZE / 4;
        int fitnessJump = (aggregateInvertFitness[GA_POPSIZE - 1] / rouletteSize);
        // start randomly between 0 and the jump distance
        int start = r.nextInt(fitnessJump);
        int populationIterator = first;
        // Fill the parents
        for (int parentRouletteIterator = 0; parentRouletteIterator < rouletteSize; parentRouletteIterator++)
        {
            while (aggregateInvertFitness[populationIterator] < (parentRouletteIterator * fitnessJump) + start)
                populationIterator++;
            parentsRoulette.add(population.get(populationIterator));
        }
    }

    public static void randomSampling (Vector<Gene> population, Vector<Gene> parents,
                                       int childrenToGenerate, boolean aging)
    {
        parents.clear();
        int i1, i2, f = 0, l = GA_POPSIZE - 1, m = GA_POPSIZE;
        if (aging)
        {
            while (population.get(f).age < 1) f++;
            while (population.get(l).age > 3) l--;
            m = l - f + 1;
        }
        for (int i = childrenToGenerate; i < GA_POPSIZE; i++)
        {
            do
            {
                i1 = r.nextInt(m) + f;
                i2 = r.nextInt(m) + f;
            } while (i1 == i2);
            parents.add(population.get(i1));
            parents.add(population.get(i2));
        }
    }

    public static void tournamentSelection (Vector<Gene> pop, Vector<Gene> parents, int childrenToGenerate)
    {
        parents.clear();
        int sampleSize = 10;
        Vector<Gene> tournament = new Vector<>(sampleSize);
        int _w = pop.get(GA_POPSIZE - 1).fitness;
        int _mf1, _mf2, i1, i2;
        for (int i = 0; i < childrenToGenerate; i++)
        {
            _mf1 = _mf2 = _w;
            i1 = i2 = 0;
            for (int j = 0; j < sampleSize; j++)
            {
                // select random member from the top 50%
                tournament.add(pop.get(r.nextInt(GA_POPSIZE / 2)));
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
}
