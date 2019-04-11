package com.AILab3.GeneticAlgo;

import com.AILab3.Entities.KnapsackGene;
import com.AILab3.Entities.StringGene;

public class Mutation
{

    public static StringGene onePointCrossover (StringGene one, StringGene two)
    {
        StringGene ret = new StringGene();
        int tsize = Constants.GA_TARGET.length();
        int spos = (Constants.r.nextInt(tsize));
        ret.str = one.str.substring(0, spos) + two.str.substring(spos, tsize);
        if (Constants.r.nextInt(Constants.RAND_MAX) < Constants.GA_MUTATION) mutateOnePoint(ret);
        return ret;
    }

    public static StringGene uniformCrossover (StringGene one, StringGene two)
    {
        StringBuilder sb = new StringBuilder();
        StringGene[] pool = {one, two};
        int _l = Constants.GA_TARGET.length();
        for (int i = 0; i < _l; i++)
            sb.append(pool[Constants.r.nextInt(2)].str.charAt(i));
        return new StringGene(sb.toString(), 0, 0, 0);
    }

    private static void mutateOnePoint (StringGene member)
    {
        StringBuilder sb = new StringBuilder();
        int tsize = Constants.GA_TARGET.length();
        int ipos = Constants.r.nextInt(Constants.RAND_MAX) % tsize;
        int delta = (Constants.r.nextInt(Constants.RAND_MAX) % 90) + 32;
        // Copy beginning
        if (ipos > 0)
            sb.append(member.str, 0, ipos);
        // Mutate one char
        sb.append((char) ((member.str.charAt(ipos) + delta) % 122));
        // Copy end
        if (ipos + 1 < member.str.length())
            sb.append(member.str, ipos + 1, tsize);
        member.str = sb.toString();
    }

    public static KnapsackGene onePointCrossover (KnapsackGene one, KnapsackGene two)
    {
        int spos = (Constants.r.nextInt(KnapsackGene.count));
        int[] g = one.gene.clone();
        if (KnapsackGene.count - spos >= 0) System.arraycopy(two.gene, spos, g, spos, KnapsackGene.count - spos);
        if (Constants.r.nextInt(Constants.RAND_MAX) < Constants.GA_MUTATION) mutateOnePoint(g);
        return new KnapsackGene(g);
    }

    private static void mutateOnePoint (int[] g)
    {
        // Mutate one char
        g[Constants.r.nextInt(KnapsackGene.count)] = (Constants.r.nextInt(2));
    }

    public static KnapsackGene uniformCrossover (KnapsackGene one, KnapsackGene two)
    {
        int[] g = one.gene.clone();
        int _l = KnapsackGene.count;
        for (int i = 0; i < _l; i++)
            if (Constants.r.nextInt(2) == 1) g[i] = two.gene[i];
        if (Constants.r.nextInt(Constants.RAND_MAX) < Constants.GA_MUTATION) mutateOnePoint(g);
        return new KnapsackGene(g, 0, 0, 0);
    }
}
