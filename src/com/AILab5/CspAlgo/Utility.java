package com.AILab5.CspAlgo;


import org.apache.commons.math3.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

@SuppressWarnings("ALL")
public class Utility
{
    String[] fileList = {"DSJC125.1", "DSJC125.5", "DSJC250.1", "DSJC250.5", "DSJC500.1", "DSJC500.5", "DSJC1000.1", "DSJC1000.5", "DSJR500.1", "DSJR500.5"};

    public boolean[][] parseProblem (int filenum)
    {
        int _nodesCount = -1, _edgesCount = -1;
        Vector<Pair<Integer, Integer>> _edg = new Vector<>();
        File _a = new File("Problems\\" + fileList[filenum] + ".txt");
        try
        {
            FileReader _r = new FileReader(_a);
            BufferedReader _reader = new BufferedReader(_r);
            String _line;
            while ((_line = _reader.readLine()) != null)
            {
                switch (_line.charAt(0))
                {
                    case 'p':
                        String[] splitInput = _line.split(" ");
                        _nodesCount = Integer.parseInt(splitInput[3]);
                        _edgesCount = Integer.parseInt(splitInput[4]);
                        break;
                    case 'e':
                        String[] s = _line.split(" ");
                        _edg.add(new Pair<>(Integer.parseInt(s[1]), Integer.parseInt(s[2])));
                        break;
                    default:
                        break;
                }
            }
            _reader.close();
            _r.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        if (_nodesCount == -1 || _edgesCount == -1) return null;
        boolean[][] res = new boolean[_nodesCount][_nodesCount];
        for (int i = 0; i < _nodesCount; i++)
            Arrays.fill(res[i], false);
        for (Pair<Integer, Integer> p : _edg)
        {
            res[p.getFirst()][p.getSecond()] = true;
            res[p.getSecond()][p.getFirst()] = true;
        }
        return res;
    }
}
