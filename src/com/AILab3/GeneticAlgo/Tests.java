package com.AILab3.GeneticAlgo;

import com.AILab3.Entities.AlgoGene;
import com.AILab3.Main;

public class Tests
{
    public static void testing ()
    {
        // Create variables
        StringBuilder sb = new StringBuilder();
        AlgoGene member = new AlgoGene();

        // Initialize
        int targetSize = Main.GA_TARGET.length();
        for (int j = 0; j < targetSize; j++)
            sb.append((char) ((Main.r.nextInt(Main.RAND_MAX) % 90) + 32));
        member.str = sb.toString();
        int le = sb.length();
        sb.delete(0, le);

        // test
        int tsize = Main.GA_TARGET.length();
        int ipos = 11;//r.nextInt(RAND_MAX) % tsize;
        int delta = (Main.r.nextInt(Main.RAND_MAX) % 90) + 32;
        if (ipos > 0)
            sb.append(member.str, 0, ipos);
        sb.append((char) ((member.str.charAt(ipos) + delta) % 122));
        if (ipos + 1 < member.str.length()) sb.append(member.str, ipos + 1, tsize);
        le = sb.length();
        member.str = sb.toString();
    }
}
