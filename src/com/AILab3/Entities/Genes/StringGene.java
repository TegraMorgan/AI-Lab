package com.AILab3.Entities.Genes;


import java.util.Vector;

import static com.AILab3.GeneticAlgo.Constants.*;

public class StringGene extends Gene

{
    public static int targetLength;
    public static String target;
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
            ark.add(Gene.mutationAlgo.mutate(parents.get(i1), parents.get(i2)));
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
        Gene.fitnessAlgo.updateFitness(this);
    }

    @Override
    public int similar (Gene other)
    {
        int res = 0;
        String o = ((StringGene) other).str;
        String t = this.str;
        for (int i = 0; i < t.length(); i++)
            if (o.charAt(i) != t.charAt(i)) res++;
        return res;
    }
}
