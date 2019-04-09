package com.AILab3.GeneticAlgo;

import com.AILab3.Entities.AlgoGene;
import com.AILab3.Main;

import java.util.Vector;

public class Mutation
{
    public static void mutation (Vector<AlgoGene> parents, Vector<AlgoGene> ark, String method)
    {
        int start = ark.size();
        int psize = parents.size();
        int i1, i2;
        for (int i = start; i < Main.GA_POPSIZE; i++)
        {
            do
            {
                i1 = Main.r.nextInt(psize);
                i2 = Main.r.nextInt(psize);
            } while (i1 == i2);

            switch (method)
            {
                case "onePoint":
                    ark.add(onePointCrossover(parents.get(i1), parents.get(i2)));
                    break;
                case "uniform":
                    ark.add(uniformCrossover(parents.get(i1), parents.get(i2)));
                    break;
                default:
                    break;
            }
        }
    }

    public static AlgoGene onePointCrossover (AlgoGene one, AlgoGene two)
    {
        AlgoGene ret = new AlgoGene();
        int tsize = Main.GA_TARGET.length();
        int spos = (Main.r.nextInt(tsize));
        ret.str = one.str.substring(0, spos) + two.str.substring(spos, tsize);
        if (Main.r.nextInt(Main.RAND_MAX) < Main.GA_MUTATION) mutateOnePoint(ret);
        return ret;
    }

    public static AlgoGene uniformCrossover (AlgoGene one, AlgoGene two)
    {
        StringBuilder sb = new StringBuilder();
        AlgoGene[] pool = {one, two};
        int _l = Main.GA_TARGET.length();
        for (int i = 0; i < _l; i++)
            sb.append(pool[Main.r.nextInt(2)].str.charAt(i));
        return new AlgoGene(sb.toString(), 0, 0, 0);
    }

    public static void mutateOnePoint (AlgoGene member)
    {
        StringBuilder sb = new StringBuilder();
        int tsize = Main.GA_TARGET.length();
        int ipos = Main.r.nextInt(Main.RAND_MAX) % tsize;
        int delta = (Main.r.nextInt(Main.RAND_MAX) % 90) + 32;
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
}
