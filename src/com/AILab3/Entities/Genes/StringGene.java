package com.AILab3.Entities.Genes;


import static com.AILab3.GeneticAlgo.Constants.RAND_MAX;
import static com.AILab3.GeneticAlgo.Constants.r;

public class StringGene extends Gene

{
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
            if (o.charAt(i) == t.charAt(i)) res++;
        return (int) ((double) res / (double) this.getProblemSize() * 100);
    }

    @Override
    public int getProblemSize ()
    {
        return target.length();
    }

    @Override
    public void replace ()
    {
        int targetLength = this.getProblemSize();
        StringBuilder sb = new StringBuilder(targetLength);
        for (int j = 0; j < targetLength; j++)
            sb.append((char) ((r.nextInt(RAND_MAX) % 90) + 32));
        this.str = sb.toString();
    }

    @Override
    public String toString ()
    {
        return this.str;
    }
}
