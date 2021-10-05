import org.jfree.data.category.DefaultCategoryDataset;
import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.util.SupplierUtil;

import java.util.*;

public class CycleFind {

    LinkedList<Integer> adjacencyList[];
    Stack<Integer> stack;

    void initializeAdjList(int numberOfNodes) {
        adjacencyList = new LinkedList[numberOfNodes+1];
        for(int i = 0;i<adjacencyList.length;i++) {
            adjacencyList[i] = new LinkedList<>();
        }
        // Initializing stack for cycle finding
         stack = new Stack<>();
    }

    DefaultUndirectedGraph<String, DefaultEdge> generateGraph(int v, int e) {
        GnmRandomGraphGenerator<String, DefaultEdge> graph = new GnmRandomGraphGenerator<String, DefaultEdge>(v, e);
        DefaultUndirectedGraph<String, DefaultEdge> undirectedGraph = new DefaultUndirectedGraph<>
                (SupplierUtil.createStringSupplier(1), SupplierUtil.DEFAULT_EDGE_SUPPLIER, false);
        graph.generateGraph(undirectedGraph, null);
        return undirectedGraph;
    }

    void createGraphAndList(int numberOfNodes, int numberOfEdges) {
        DefaultUndirectedGraph<String, DefaultEdge> currentGraph = generateGraph(numberOfNodes, numberOfEdges);
//        System.out.println(Arrays.toString(currentGraph.toString().split("]")));
        String[] str = currentGraph.toString().split("]");
        initializeAdjList(numberOfNodes);
        int a = 0;
        int b = 0;
        for(char ch: str[1].toCharArray()) {
            if(Character.isDigit(ch)) {
                if(a > 0) b = Character.getNumericValue(ch);
                else a = Character.getNumericValue(ch);
                if(a > 0 && b > 0) {
                    adjacencyList[a].add(b);
                    adjacencyList[b].add(a);
                    a = 0;
                    b = 0;
                }
            }
        }
        // Printing the adjacency linkedlist
//        for(LinkedList<Integer> list: adjacencyList) {
//            System.out.println(list);
//        }
    }

    boolean findCycle(int currentNode, int parent, boolean[] visitedArr) {
        visitedArr[currentNode] = true;
//        System.out.println("pushing: " + currentNode);
        stack.push(currentNode);
        for (int currentIndex : adjacencyList[currentNode]) {
            if (!visitedArr[currentIndex]) {
                if(findCycle(currentIndex, currentNode, visitedArr)) return true;
            }
            else if (currentIndex != parent) {
                // Pushing the final node where the cycle is found
                stack.push(currentIndex);
                return true;
            }
        }
        stack.pop();
        return false;
    }

    boolean initiateCycleFindingProcess(int numberOfNodes) {
        boolean[] visitedArr = new boolean[numberOfNodes+1];
        for (int i = 1; i <= numberOfNodes; i++)
        {
             /*Not visiting the already visited node because suppose we have traversed a node 5 whose parent is 2
             and in the following iteration 5 is marked as visited and then the control returns in this loop from
             findCycle(). If we traverse node 5 here, we would send its parent as -1 but originally the parent
             node of 5 is 2. Hence, to avoid this conflict, we do not traverse the already visited node
             */
            if(!visitedArr[i]) if (findCycle(i, -1, visitedArr)) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        CycleFind cycle = new CycleFind();
        int nodes = 10;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i = 1;i<=nodes;i++) {
            int edges = (int)(Math.random() * i*(i-1)/2);
            cycle.createGraphAndList(i, edges);
            long startTime = System.nanoTime();
            if(cycle.initiateCycleFindingProcess(i)) {
//                System.out.println("Cycle found. Cycle Path: ");
                int cycleStartEndNode = cycle.stack.pop();
                int completeCycleNode = cycle.stack.peek();
//                System.out.println("start end node: " + cycleStartEndNode);
                while (!cycle.stack.empty()) {
                    // Will print only until the point where the cycle was found, not beyond that
                    if(cycle.stack.peek() != cycleStartEndNode) {
//                        System.out.print(cycle.stack.pop() + " ");
                        cycle.stack.pop();
                    } else break;
                }
//                System.out.print(cycleStartEndNode + " " + completeCycleNode + "\n");
            } else {
//                System.out.println("Cycle not found");
            }
            long endTime = System.nanoTime();
            dataset.addValue(endTime - startTime, "", Integer.toString(i+edges));
//            System.out.println("Execution time: " + (endTime - startTime));
        }
        // Plotting the line graph for processing time as a function of the graph size
//        System.out.println("Yolo!");
        PlotLineGraph plotLineGraph = new PlotLineGraph("Cycle Finding Graph");
        plotLineGraph.plot(dataset, "Cycle Finding Graph" , "Nodes + Edges", "Execution Time(in ns)");
    }
}
