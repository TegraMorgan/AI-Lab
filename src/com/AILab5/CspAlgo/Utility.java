package com.AILab5.CspAlgo;


import com.AILab5.Entity.ColorGraph;
import org.apache.commons.math3.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
public class Utility
{
    static String[] fileList = {
            "1-Insertions_4", "1-Insertions_5", "1-Insertions_6"
            , "2-Insertions_3", "2-Insertions_4", "2-Insertions_5"
            , "3-Insertions_3", "3-Insertions_4", "3-Insertions_5"
            , "1-FullIns_3", "1-FullIns_4", "1-FullIns_5"
            , "2-FullIns_3", "2-FullIns_4", "2-FullIns_5"
            , "3-FullIns_3", "3-FullIns_4", "3-FullIns_5"
            , "4-FullIns_3", "4-FullIns_4", "4-FullIns_5"
            , "fpsol2.i.1", "fpsol2.i.2", "fpsol2.i.3"
            , "inithx.i.1", "inithx.i.2", "inithx.i.3"
            , "DSJC125.5", "DSJC250.5", "DSJC500.1"
            , "DSJC500.5", "DSJR500.1", "DSJR500.1c"
            , "le450_15a", "le450_15b", "le450_15c"};
    public static final int NO_OF_PROBLEMS = fileList.length;
    /**
     * Decodes input file
     *
     * @param filenum number of problem to be solved
     * @return Returns boolean adjacency matrix
     */
    public static boolean[][] parseProblem (int filenum)
    {
        if (filenum < 0 || filenum > NO_OF_PROBLEMS) return null;
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
                        _nodesCount = Integer.parseInt(splitInput[2]);
                        _edgesCount = Integer.parseInt(splitInput[3]);
                        break;
                    case 'e':
                        String[] s = _line.split(" ");
                        _edg.add(new Pair<>(Integer.parseInt(s[1]) - 1, Integer.parseInt(s[2]) - 1));
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
        boolean[][] adjacencyMatrix = new boolean[_nodesCount][_nodesCount];
        for (int i = 0; i < _nodesCount; i++)
            Arrays.fill(adjacencyMatrix[i], false);
        for (Pair<Integer, Integer> p : _edg)
        {
            adjacencyMatrix[p.getFirst()][p.getSecond()] = true;
            adjacencyMatrix[p.getSecond()][p.getFirst()] = true;
        }
        return adjacencyMatrix;
    }

    public static boolean isAllowed (ColorGraph graph, int node)
    {
        int c = graph.getColor(node);
        for (int i = 0; i < graph.getNeighborsCount(node); i++)
            if (graph.getColor(graph.getNeighbor(node, i)) == c)
                return false;
        return true;
    }

    public static boolean isSolved (ColorGraph graph)
    {
        for (int i = 0; i < graph.getNumberOfNodes(); i++)
        {
            if (!isAllowed(graph, i))
            {
                return false;
            }
        }
        return true;
    }

    public static String executionTimeString (long time_ns)
    {
        final long time_ms = TimeUnit.NANOSECONDS.toMillis(time_ns);
        final long time_sec = TimeUnit.NANOSECONDS.toSeconds(time_ns);
        final long time_min = TimeUnit.NANOSECONDS.toMinutes(time_ns);
        String str = "";
        if(time_min > 0)
            str += time_min + " Minutes, ";
        if(time_sec > 0)
            str += (time_sec % 60) + " Seconds, ";
        if(time_ms > 0)
            str += (time_ms % 1000) + " MicroSeconds, ";
        str += (time_ns % 1000000) + " NanoSeconds";
        return str;
    }
}
