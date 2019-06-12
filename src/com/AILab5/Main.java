package com.AILab5;

import com.AILab5.CspAlgo.LocalSearch.FeasibilityFirst;
import com.AILab5.CspAlgo.LocalSearch.HybridSearch;
import com.AILab5.CspAlgo.LocalSearch.ObjectiveFirst;
import com.AILab5.CspAlgo.Utility;
import com.AILab5.Entity.ColorGraph;
import com.AILab5.Entity.LabAnswer;
import com.AILab5.Entity.Solutions;

import static com.AILab5.CspAlgo.Utility.*;

@SuppressWarnings("SpellCheckingInspection")
public class Main
{
    public static void main (String[] args)
    {

        // Read and parse the arguments
        String[] input = Utility.parseInput(args);
        int start, end;
        if (input[0].charAt(0) == 'a')
        {
            start = 0;
            end = NO_OF_PROBLEMS;
        } else
        {
            start = Integer.valueOf(input[0]);
            if (start>=NO_OF_PROBLEMS) return;
            end = start + 1;
        }
        int file_number = start;
        while (file_number < end)
        {
            // Read and parse problem
            boolean[][] _gr = parseProblem(file_number);
            if (_gr == null) return;
            LabAnswer answer;

            ColorGraph graph = new ColorGraph(_gr);
            switch (input[1])
            {
                case "objective":
                    int per = Integer.valueOf(input[2]);
                    if (per == 0) answer = ObjectiveFirst.objectiveFunctionFirstSearch(graph);
                    else answer = ObjectiveFirst.objectiveFunctionFirstSearch(graph, per);
                    break;

                case "backjumping":
                    answer = Solutions.straightforwardBackJumping(graph, true);
                    break;

                case "forwardchecking":
                    answer = Solutions.arcConsistencyForwardChecking(graph);
                    break;

                case "feasibility":
                    answer = FeasibilityFirst.feasibleFirstSearch(graph);
                    break;

                case "hybrid":
                    answer = HybridSearch.hybridSearch(graph);
                    break;

                default:
                    return;
            }
            //printResults(answer, graph);
            machineOutput(answer, graph,file_number);
            file_number++;
        }
    }
}