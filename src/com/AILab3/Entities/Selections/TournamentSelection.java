package com.AILab3.Entities.Selections;

import com.AILab3.Entities.Genes.Gene;

import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.GA_POPSIZE;
import static com.AILab3.GeneticAlgo.Constants.r;

public class TournamentSelection implements com.AILab3.Entities.Interfaces.ISelectionAlgo
{
    @Override
    public void selectParents (Vector<Gene> pop,
                               Vector<Gene> parents,
                               int childrenToGenerate,
                               boolean aging)
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
