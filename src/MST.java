import org.jfree.data.category.DefaultCategoryDataset;
import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.util.SupplierUtil;

import java.util.*;

public class MST {

    LinkedList<Integer> adjacencyList[];
    HashMap<String, Integer> map = new HashMap<>();
    ArrayList<String> list;

    void initializeAdjList(int numberOfNodes) {
        adjacencyList = new LinkedList[numberOfNodes+1];
        for(int i = 0;i<adjacencyList.length;i++) {
            adjacencyList[i] = new LinkedList<>();
        }
    }

    DefaultUndirectedGraph<String, DefaultEdge> generateGraph(int v, int e) {
        GnmRandomGraphGenerator<String, DefaultEdge> graph = new GnmRandomGraphGenerator<String, DefaultEdge>(v, e);
        DefaultUndirectedGraph<String, DefaultEdge> undirectedGraph = new DefaultUndirectedGraph<>
                (SupplierUtil.createStringSupplier(1), SupplierUtil.DEFAULT_EDGE_SUPPLIER, false);
        graph.generateGraph(undirectedGraph, null);
        return undirectedGraph;
    }

    void createGraphAndList(int numberOfNodes, int numberOfEdges) {
        initializeAdjList(numberOfNodes);
//        for (int i = 1; i <= numberOfNodes; i++) {
//            System.out.println("Enter number of neighbours: ");
//            Scanner sc = new Scanner(System.in);
//            int n = sc.nextInt();
//            for (int j = 0; j < n; j++) {
//                System.out.println("Enter neighbour and weight of edge: ");
//                int neighbour = sc.nextInt();
//                int weight = sc.nextInt();
//                adjacencyList[i].add(neighbour);
//                adjacencyList[neighbour].add(i);
//                map.put(i + " " + neighbour, weight);
//                map.put(neighbour + " " + i, weight);
//            }
//        }
        int max = 100;
        int min = 0;
        int range = max - min + 1;
        for(int i=1;i<=numberOfEdges;i++) {
            if(i < numberOfNodes) {
                adjacencyList[i].add(i+1);
                adjacencyList[i+1].add(i);
                int weight = (int)(Math.random() * range);
                map.put(i + " " + (i+1), weight);
                map.put((i+1) + " " + i, weight);
//                System.out.println(map);
            }
            // After connecting the graph above, now adding randomized weighted edges to create cycles
            else {
//                System.out.println("Bruh");
                int vertice1;
                int vertice2;
                while(true) {
                    vertice1 = new Random().nextInt(numberOfNodes - min) + 1;
                    vertice2 = new Random().nextInt(numberOfNodes - min) + 1;
                    System.out.println(vertice1 + " " + vertice2);
                    if(vertice1 != vertice2 && !map.containsKey(vertice1 + " " + vertice2)) {
//                        System.out.println("Breaking!");
                        break;
                    }
                }
                adjacencyList[vertice1].add(vertice2);
                adjacencyList[vertice2].add(vertice1);
                int weight = (int)(Math.random() * range + min);
                map.put(vertice1 + " " + vertice2, weight);
                map.put(vertice2 + " " + vertice1, weight);
//                System.out.println(map);
            }
        }
        for(LinkedList<Integer> list: adjacencyList) {
            System.out.println(list);
        }
        System.out.println("Map: " + map);
    }

    boolean findCycle(int currentNode, int parent, boolean[] visitedArr) {
        visitedArr[currentNode] = true;
        list.add(parent + " " + currentNode);
        for (int currentIndex : adjacencyList[currentNode]) {
            if (!visitedArr[currentIndex]) {
                if(findCycle(currentIndex, currentNode, visitedArr)) return true;
            }
            else if (currentIndex != parent) {
                // Pushing the final node where the cycle is found
                String str = currentNode + " " + currentIndex;
                list.add(str);

                // Finding the max weight edge from the cycled edges
                int lastIndex = Integer.parseInt(str.split(" ")[1]);
                int loop = list.size()-2;
                int index = list.size()-1;
                int maxVal = map.get(str);
                while(loop>=0) {
                    if(maxVal < map.get(list.get(loop))) {
                        maxVal = map.get(list.get(loop));
                        index = loop;
                    }
                    if(list.get(loop).contains(Integer.toString(lastIndex))) break;
                    loop--;
                }
                String[] vertices = list.get(index).split(" ");
                int num1 = Integer.parseInt(vertices[0]);
                int num2 = Integer.parseInt(vertices[1]);

                // Removing the max weighted edge from the adjacency list
                adjacencyList[num1].remove(new Integer(num2));
                adjacencyList[num2].remove(new Integer(num1));
                return true;
            }
        }
        list.remove(list.size()-1);
        return false;
    }

    boolean initiateCycleFindingProcess(int numberOfNodes) {
        for(int j=0;j<9;j++) {
            // Initializing stack for cycle finding
            list = new ArrayList<String>();
            boolean[] visitedArr = new boolean[numberOfNodes+1];
            if (!findCycle(1, -1, visitedArr)) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        MST mst = new MST();
        int nodes = 1000;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i = 3;i<=nodes;i++) {
            int edges;
            if(i < 6) {
                int range = (i*(i-1)/2) - i-1 +1 ;
                edges = (int)(Math.random() * range + (i-1));
            } else {
                int range = i+8 - i-1 + 1;
                edges = (int)(Math.random() * range + (i-1));
            }
            System.out.println("nodes: " + i + ", edges : " + edges);
            mst.createGraphAndList(i, edges);
//            long startTime = System.nanoTime();
            mst.initiateCycleFindingProcess(i);
//            // Printing the adjacency linkedlist
            System.out.println("MST found: ");
            for(LinkedList<Integer> list: mst.adjacencyList) {
                System.out.println(list);
            }
//            long endTime = System.nanoTime();
//            dataset.addValue(endTime - startTime, "", Integer.toString(i+edges));
//            System.out.println("Execution time: " + (endTime - startTime));
        }
        // Plotting the line graph for processing time as a function of the graph size
//        System.out.println("Yolo!");
//        PlotLineGraph plotLineGraph = new PlotLineGraph("Cycle Finding Graph");
//        plotLineGraph.plot(dataset, "Cycle Finding Graph" , "Nodes + Edges", "Execution Time(in ns)");
    }
}
