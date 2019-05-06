package com.AILab3.Entities.Selections;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.GeneticAlgo.Utility;

import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.r;

public class StochasticUniversalSampling implements com.AILab3.Entities.Interfaces.ISelectionAlgo
{
    @Override
    public void selectParents (Vector<Gene> population,
                               Vector<Gene> parentsRoulette,
                               int amountOfChildrenToGenerate,
                               boolean aging)
    {
        parentsRoulette.clear();
        int first = 0;
        if (aging)
            // We don't want the children to reproduce, children have age of 1 now
            // We sorted the population by age already
            while (population.get(first).age < 2) first++;
        // invert fitness value
        int pops = population.size();
        long[] aggregateInvertFitness = Utility.aggregateInvertFitness(pops, first, population);
        // distance between the drawn fitness values
        // the last element of aggregate function will always contain the total sum
        int rouletteSize = aging ? amountOfChildrenToGenerate * 2 : pops / 4;
        int fitnessJump = (int) (aggregateInvertFitness[pops - 1] / rouletteSize);
        if (fitnessJump > 0)
        {
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
        } else
        {
            // fallback on random selection
            pops -= first;
            for (int parentRouletteIterator = 0; parentRouletteIterator < rouletteSize; parentRouletteIterator++)
                parentsRoulette.add(population.get(r.nextInt(pops) + first));
        }
    }
}
