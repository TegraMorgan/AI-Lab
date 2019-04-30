package com.AILab3.Entities.Mutations;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.StringGene;
import com.AILab3.Entities.Interfaces.IMutationAlgo;
import com.AILab3.GeneticAlgo.Constants;

public class StringOnePointCrossover extends BaseMutation
{
    @Override
    public Gene mutate (Gene p1, Gene p2)
    {
        StringGene one = (StringGene) p1, two = (StringGene) p2;
        StringGene ret = new StringGene();
        int tsize = p1.getProblemSize();
        int spos = (Constants.r.nextInt(tsize));
        ret.str = one.str.substring(0, spos) + two.str.substring(spos, tsize);
        if (Constants.r.nextInt(Constants.RAND_MAX) < getMutationRate()) IMutationAlgo.mutateOnePoint(ret);
        return ret;
    }
}
