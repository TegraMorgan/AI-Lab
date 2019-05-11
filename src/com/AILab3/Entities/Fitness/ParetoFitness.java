package com.AILab3.Entities.Fitness;

import com.AILab3.Entities.Genes.ParetoGene;
import com.AILab3.Entities.Interfaces.IFitnessAlgo;

import java.util.Comparator;
import java.util.Vector;

public class ParetoFitness implements IFitnessAlgo
{
    private static final Comparator<Coord> SORT_BY_X = Comparator.comparingDouble(o -> o.byX);

    @Override
    public void updateFitness (Object o)
    {
        final ParetoGene pareto = (ParetoGene) o;
        final int l = pareto.getProblemSize();
        double[] gene = pareto.gene;
        double fitn = 0, f, g;
        Vector<Coord> coordinates = new Vector<>(), frontier = new Vector<>();
        for (int i = 0; i < l; i++)
        {
            f = CalculateF(gene[i]);
            g = CalculateG(gene[i]);
            coordinates.add(new Coord(f, g));
        }
        coordinates.sort(SORT_BY_X);
        int fr_it = 0, co_it = 0;
        frontier.add(coordinates.get(0));
        do
        {
            while (co_it < l && coordinates.get(co_it).byY <= frontier.get(fr_it).byY)
            {
                co_it++;
            }
            if (co_it < l && coordinates.get(co_it).byY > frontier.get(fr_it).byY)
            {
                frontier.add(coordinates.get(co_it));
                fr_it++;
            }
        } while (co_it < l);
        pareto.fitness = l - frontier.size();
        pareto.inverseFitness = frontier.size();
    }

    private double CalculateF (final double v)
    {
        return Math.pow(v + 2, 2) - 10;
    }

    private double CalculateG (final double v)
    {
        return Math.pow(v - 2, 2) + 10;
    }

    class Coord
    {
        double byX, byY;

        Coord ()
        {
            byX = 0;
            byY = 0;
        }

        Coord (double _x, double _y)
        {
            byX = _x;
            byY = _y;
        }
    }
}
