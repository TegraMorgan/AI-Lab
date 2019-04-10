package com.AILab3.GeneticAlgo;

import com.AILab3.Entities.AlgoGene;

import java.util.Vector;

public class Fitness
{
    /**
     * Function that calculates fitness of a given population
     *
     * @param population
     * @return
     */
    public static void calcFitness (Vector<AlgoGene> population, String method)
    {
        switch (method)
        {
            case "bull":
                bulPgiaFitness(population);
                break;
            default:
                defaultFitness(population);
                break;
        }
    }

    public static void bulPgiaFitness (Vector<AlgoGene> _p)
    {
        /*
        One of the basic rules of the game is that the target word cannot contain
        duplicate letters. This algorithm ranking suffers if target OR guess
        contain duplicate letters
         */
        /* Target Length */
        int _l = Constants.GA_TARGET.length();
        /* Fitness */
        int _f;
        /* Gene */
        String _g;
        for (int i = 0; i < Constants.GA_POPSIZE; i++)
        {
            /*
            Reset fitness
            If we have a match fitness will become 0
             */
            _f = _l * 5;
            _g = _p.get(i).str;
            for (int k = 0; k < _l; k++)
            {
                if (Constants.GA_TARGET.charAt(k) == _g.charAt(k))
                    _f -= 5;
                else
                    for (int j = 0; j < _l; j++)
                    {
                        if (_g.charAt(k) == Constants.GA_TARGET.charAt(j))
                        {
                            _f -= 2;
                            break;
                        }
                    }
            }
            _p.get(i).fitness = _f;
            _p.get(i).inverseFitness = (5 * _l) - _f;
        }
    }

    public static void defaultFitness (Vector<AlgoGene> population)
    {
        String target = Constants.GA_TARGET;
        int tsize = target.length();
        int worst = 89 * tsize;
        for (int i = 0; i < Constants.GA_POPSIZE; i++)
        {
            int fitness = 0;
            for (int j = 0; j < tsize; j++)
            {
                int cf = Math.abs((population.get(i).str.charAt(j) - target.charAt(j)));
                fitness += cf;
            }
            population.get(i).fitness = fitness;
        }
        for (int i = 0; i < Constants.GA_POPSIZE; i++)
        {
            population.get(i).inverseFitness = worst - population.get(i).fitness;
        }
    }
}
