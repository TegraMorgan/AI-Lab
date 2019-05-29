package com.AILab5.CspAlgo;

import java.io.FileOutputStream;
import java.io.IOException;

public class Tests
{
    public static void testing ()
    {
        try
        {
            byte[] ba = {54, 56, 57};
            FileOutputStream out = new FileOutputStream("data\\output.txt");
            for (byte b : ba)
                out.write(b);
            out.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
