package com.AILab3.Entities.Mutations;

import com.AILab3.Entities.Interfaces.IMutationAlgo;

public abstract class BaseMutation implements IMutationAlgo
{
    private static final float NORMAL_MUTATION = 2048f;
    private static final float HYPER_MUTATION = 2048 * 4 * 0.75f;
    private static boolean hm = false;

    static float getMutationRate ()
    {
        return hm ? HYPER_MUTATION : NORMAL_MUTATION;
    }

    @Override
    public void enableHyperMutation ()
    {
        hm = true;
    }

    @Override
    public void disableHyperMutation ()
    {
        hm = false;
    }
}
