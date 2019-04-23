package com.AILab3.Entities.Mutations;

import com.AILab3.Entities.Genes.Gene;
import com.AILab3.Entities.Genes.StringGene;
import com.AILab3.Entities.Interfaces.IMutationAlgo;
import com.AILab3.GeneticAlgo.Constants;

public class StringUniformCrossover implements IMutationAlgo
{
    @Override
    public Gene mutate (Gene p1, Gene p2)
    {
        StringGene one = (StringGene) p1, two = (StringGene) p2;
        StringBuilder sb = new StringBuilder();
        StringGene[] pool = {one, two};
        int _l = Constants.GA_TARGET.length();
        for (int i = 0; i < _l; i++)
            sb.append(pool[Constants.r.nextInt(2)].str.charAt(i));
        StringGene sg = new StringGene(sb.toString(), 0, 0, 0);
        if (Constants.r.nextInt(Constants.RAND_MAX) < Constants.GA_MUTATION) IMutationAlgo.mutateOnePoint(sg);
        return sg;
    }
}
