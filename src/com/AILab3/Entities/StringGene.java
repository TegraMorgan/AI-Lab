package com.AILab3.Entities;


import com.AILab3.GeneticAlgo.Mutation;

import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.*;

public class StringGene extends Gene

{
    public static int targetLength;
    private static String target;
    public String str;         // String of the gene

    public StringGene ()
    {
        this("", 0, 0, 0);
    }

    public StringGene (String _str, int _fit, int _age, int _invFit)
    {
        super(_fit, _age, _invFit);
        str = _str;
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

            switch (mutationAlgo)
            {
                case "onePoint":
                    ark.add(Mutation.onePointCrossover((StringGene) parents.get(i1), (StringGene) parents.get(i2)));
                    break;
                case "uniform":
                    ark.add(Mutation.uniformCrossover((StringGene) parents.get(i1), (StringGene) parents.get(i2)));
                    break;
                default:
                    break;
            }
        }
    }

    public static void initPopulation (String t, Vector<Gene> population)
    {
        target = t;
        targetLength = t.length();
        int age_factor = GA_POPSIZE / 5 + 1;
        StringBuilder sb = new StringBuilder(targetLength);
        for (int i = 0; i < GA_POPSIZE; i++)
        {
            for (int j = 0; j < targetLength; j++)
                sb.append((char) ((r.nextInt(RAND_MAX) % 90) + 32));
            population.add(new StringGene(sb.toString(), 0, i / age_factor, 0));
            sb.delete(0, sb.length());
        }
    }

    public static void printBest (Vector<Gene> gav)
    {
        StringGene sg = (StringGene) gav.get(0);
        System.out.println("Best: " + sg.str + " (" + sg.fitness + ")");
    }

    @Override
    public boolean isSolution ()
    {
        return this.fitness == 0;
    }

    @Override
    public void updateFitness ()
    {
        if (fitnessAlgo.equals("bull"))
        {
            this.bulPgiaFitness();
        } else
        {
            this.defaultFitness();
        }
    }

    private void defaultFitness ()
    {
        int worst = 89 * targetLength;
        int fitness = 0;
        for (int j = 0; j < targetLength; j++)
        {
            int cf = Math.abs((this.str.charAt(j) - target.charAt(j)));
            fitness += cf;
        }
        this.fitness = fitness;
        this.inverseFitness = worst - fitness;
    }

    private void bulPgiaFitness ()
    {
    /*
    One of the basic rules of the game is that the target word cannot contain
    duplicate letters. This algorithm ranking suffers if target OR guess
    contain duplicate letters
     */
        /* Target Length */
        int _l = GA_TARGET.length();
        /* Fitness */
        int _f;
        /* Gene */
        String _g;

            /*
            Reset fitness
            If we have a match fitness will become 0
             */
        _f = _l * 5;
        _g = this.str;
        for (int k = 0; k < _l; k++)
        {
            if (GA_TARGET.charAt(k) == _g.charAt(k))
                _f -= 5;
            else
                for (int j = 0; j < _l; j++)
                {
                    if (_g.charAt(k) == GA_TARGET.charAt(j))
                    {
                        _f -= 2;
                        break;
                    }
                }
        }
        this.fitness = _f;
        this.inverseFitness = (5 * _l) - _f;
    }
}
