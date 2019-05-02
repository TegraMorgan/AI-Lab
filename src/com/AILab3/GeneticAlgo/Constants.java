package com.AILab3.GeneticAlgo;

import java.util.Random;

public class Constants

{
    public static final int GA_MAXITER = 16323;
    public static final int GA_POPSIZE = 2048;
    public static final float GA_ELITRATE = 0.35f;
    public static final int RAND_MAX = GA_POPSIZE * 4;
    public static final String GA_TARGET = "This will take a really long time to solve!";
    public static Random r = new Random();
}
