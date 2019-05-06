package com.AILab3.Entities.Interfaces;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.KnapsackGene;
import com.AILab3.Entities.Genes.StringGene;
import com.AILab3.GeneticAlgo.Constants;

public interface IMutationAlgo
{
    static void mutateOnePoint (StringGene member)
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

    static void mutateOnePoint (int[] g)
    {
        // Mutate one char
        g[Constants.r.nextInt(KnapsackGene.count)] = (Constants.r.nextInt(2));
    }

    static void mutateOnePoint (char[] g)
    {
        // Mutate one char
        float ra = Constants.r.nextFloat();
        g[Constants.r.nextInt(g.length)] = ra < 0.25f ? '0' : ra < 0.5f ? '1' : '?';
    }

    Gene mutate (Gene p1, Gene p2);

    void enableHyperMutation ();

    void disableHyperMutation ();

}
