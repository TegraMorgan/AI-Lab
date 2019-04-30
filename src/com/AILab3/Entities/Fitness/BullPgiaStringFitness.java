package com.AILab3.Entities.Fitness;

import com.AILab3.Entities.Genes.StringGene;
import com.AILab3.Entities.Interfaces.IFitnessAlgo;

public class BullPgiaStringFitness implements IFitnessAlgo
{
    @Override
    public void updateFitness (Object gene)
    {
        StringGene sg = (StringGene) gene;
            /*
                One of the basic rules of the game is that the target word cannot contain
                duplicate letters. This algorithm ranking suffers if target OR guess
                contain duplicate letters
            */

        /* Target Length */
        int _l = sg.getProblemSize();
        /* Fitness */
        int _f;
        /* Gene */
        String _g;

            /*
            Reset fitness
            If we have a match fitness will become 0
             */
        _f = _l * 5;
        _g = sg.str;
        for (int k = 0; k < _l; k++)
        {
            if (StringGene.target.charAt(k) == _g.charAt(k))
                _f -= 5;
            else
                for (int j = 0; j < _l; j++)
                {
                    if (_g.charAt(k) == StringGene.target.charAt(j))
                    {
                        _f -= 2;
                        break;
                    }
                }
        }
        sg.fitness = _f;
        sg.inverseFitness = (5 * _l) - _f;
    }
}
