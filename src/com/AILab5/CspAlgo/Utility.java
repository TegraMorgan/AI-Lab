package com.AILab5.CspAlgo;


import com.AILab5.Entity.ColorGraph;
import com.AILab5.Entity.LabAnswer;
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
    static final String[] fileList = {
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
    public static final int NO_OF_PROBLEMS = fileList.length; //36

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
        File _a = new File("Problems" + File.separator + fileList[filenum] + ".txt");
        try
        {
            FileReader _r = new FileReader(_a);
            BufferedReader _reader = new BufferedReader(_r);
            String _line;
            while ((_line = _reader.readLine()) != null)
            {
                if (_line.length() > 0)
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

    public static boolean isDeadEnd (ColorGraph graph, int node)
    {
        boolean[] nc = new boolean[graph.getNumberOfColors()];
        int count = 0;
        for (int i = 0; i < graph.getNeighborsCount(node); i++)
        {
            int c = graph.getColor(graph.getNeighbor(node, i));
            if (c >= 0 && !nc[c])
            {
                nc[c] = true;
                count++;
            }
        }
        return count == nc.length;
    }

    public static void printResults (LabAnswer ans, ColorGraph graph)
    {
        if (ans != null && ans.foundSolution)
        {
            ans.coloursUsed = Utility.countColorsUsed(graph);
            System.out.println("Solved in: " + executionTimeString(ans.executionTime));
            System.out.println("States scanned: " + ans.statesScanned);
            System.out.println("Solution check: " + isSolved(graph));
            System.out.println("Colours used = " + ans.coloursUsed);
        } else System.out.println("Not solved");
    }

    public static void machineOutput (LabAnswer ans, ColorGraph graph, int file_number)
    {
        StringBuilder str = new StringBuilder();
        if (ans != null && ans.foundSolution)
        {
            ans.coloursUsed = Utility.countColorsUsed(graph);
            str.append(file_number);
            str.append(",");
            str.append(ans.executionTime);
            str.append(",");
            str.append(graph.getNumberOfNodes());
            str.append(",");
            str.append(ans.statesScanned);
            str.append(",");
            str.append(isSolved(graph));
            str.append(",");
            str.append(ans.coloursUsed);
            System.out.println(str);
        } else System.out.println("Not solved");
    }


    public static String executionTimeString (long time_ns)
    {
        final long time_ms = TimeUnit.NANOSECONDS.toMillis(time_ns);
        final long time_sec = TimeUnit.NANOSECONDS.toSeconds(time_ns);
        final long time_min = TimeUnit.NANOSECONDS.toMinutes(time_ns);
        String str = "";
        if (time_min > 0)
            str += time_min + " Minutes, ";
        if (time_sec > 0)
            str += (time_sec % 60) + " Seconds, ";
        if (time_ms > 0)
            str += (time_ms % 1000) + " MicroSeconds, ";
        str += (time_ns % 1000000) + " NanoSeconds";
        return str;
    }

    public static int countColorsUsed (ColorGraph graph)
    {
        final int COLOURS = graph.getNumberOfColors();
        final int NODES = graph.getNumberOfNodes();
        int totalColoursUsed = 0;
        boolean[] isUsed = new boolean[COLOURS];
        Arrays.fill(isUsed, false);
        for (int node = 0; node < NODES; node++)
            isUsed[graph.getColor(node)] = true;
        for (int color = 0; color < COLOURS; color++)
            if (isUsed[color]) totalColoursUsed++;
        return totalColoursUsed;
    }

    public static boolean changeColour (int node, ColorGraph graph)
    {
        int currCol = graph.getColor(node);
        boolean[] availableColours = graph.getAvailableColours(node);
        for (int i = currCol + 1; i < availableColours.length; i++)
        {
            if (availableColours[i])
            {
                graph.setColor(node, i);
                return true;
            }
        }
        for (int i = 0; i < currCol; i++)
        {
            if (availableColours[i])
            {
                graph.setColor(node, i);
                return true;
            }
        }
        return false;
    }

    public static String[] parseInput (String[] args)
    {
        final int l = args.length;
        String[] ret = new String[l];
        int temp;
        if (l > 0)
            switch (args[0].toLowerCase())
            {
                case "all":
                    ret[0] = "all";
                    break;
                default:
                    try
                    {
                        temp = Integer.parseInt(ret[0]);
                        ret[0] = String.valueOf(temp);
                    } catch (NumberFormatException e)
                    {
                        ret[0] = "all";
                    }
            }
        if (l > 1)
            switch (args[1].toLowerCase())
            {
                case "objective":
                case "backjumping":
                case "forwardchecking":
                case "feasibility":
                case "hybrid":
                    ret[1] = args[1];
                    break;
                default:
                    ret[1] = "feasibility";
                    break;
            }
        if (l > 2)
        {
            if (ret[2].equals("objective"))
            {
                try
                {
                    temp = Integer.parseInt(args[2]);
                } catch (NumberFormatException e)
                {
                    temp = 0;
                }
                ret[2] = String.valueOf(temp);
            }
        } else ret[2] = "0";
        return ret;
    }
}
