import org.jfree.data.category.DefaultCategoryDataset;
import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.util.SupplierUtil;

import java.util.*;

public class FindMST_raw {
    LinkedList<Integer> adjacencyList[];
    HashMap<String, int[]> map = new HashMap<>();
    ArrayList<String> list = new ArrayList<>();

    void initializeAdjList(int numberOfNodes) {
        adjacencyList = new LinkedList[numberOfNodes+1];
        for(int i = 0;i<adjacencyList.length;i++) {
            adjacencyList[i] = new LinkedList<>();
        }
        // Initializing stack for cycle finding
//        stack = new Stack<>();
    }

    DefaultUndirectedGraph<String, DefaultEdge> generateGraph(int v, int e) {
        GnmRandomGraphGenerator<String, DefaultEdge> graph = new GnmRandomGraphGenerator<String, DefaultEdge>(v, e);
        DefaultUndirectedGraph<String, DefaultEdge> undirectedGraph = new DefaultUndirectedGraph<>
                (SupplierUtil.createStringSupplier(1), SupplierUtil.DEFAULT_EDGE_SUPPLIER, false);
        graph.generateGraph(undirectedGraph, null);
        return undirectedGraph;
    }

    void createGraphAndList(int numberOfNodes, int numberOfEdges) {
//        DefaultUndirectedGraph<String, DefaultEdge> currentGraph = generateGraph(numberOfNodes, numberOfEdges);
////        System.out.println(Arrays.toString(currentGraph.toString().split("]")));
//        String[] str = currentGraph.toString().split("]");
        initializeAdjList(numberOfNodes);
        for(int i=1;i<=numberOfNodes;i++) {
            System.out.println("Enter number of neighbours: ");
            Scanner sc = new Scanner(System.in);
            int n = sc.nextInt();
            for(int j=0;j<n;j++) {
                System.out.println("Enter neighbour and weight of edge: ");
                int neighbour = sc.nextInt();
                int weight = sc.nextInt();
                    adjacencyList[i].add(neighbour);
                    adjacencyList[neighbour].add(i);
                    map.put(i + " " + neighbour, new int[]{weight, 0});
                    map.put(neighbour + " " + i, new int[]{weight, 0});
            }
        }
//         Printing the adjacency linkedlist
        for(LinkedList<Integer> list: adjacencyList) {
            System.out.println(list);
        }
        System.out.println("Map: " + map);
    }

    void findCycle(int currentNode, int parent, boolean[] visitedArr) {
        visitedArr[currentNode] = true;
        list.add(parent + " " + currentNode);
        System.out.println("Current list: " + list);
        for (int currentIndex : adjacencyList[currentNode]) {
            System.out.println(Arrays.toString(visitedArr));
            if (!visitedArr[currentIndex]) {
                findCycle(currentIndex, currentNode, visitedArr);
            }
            else if (currentIndex != parent && parent != -1) {
                String str = currentNode + " " + currentIndex;
                char[] chars = str.toCharArray();
                Arrays.sort(chars);
                String sorted = new String(chars);
                sorted = sorted.charAt(1) + " " + sorted.charAt(2);
//                System.out.println(sorted);
                if(map.get(sorted)[1] == 0) {
                    int[] temp = map.get(sorted);
                    temp[1] = 1;
                    map.put(sorted, temp);
                    list.add(str);
                } else continue;
                System.out.println(" bruh : " + str);
                int lastIndex = Integer.parseInt(str.split(" ")[1]);
                int loop = list.size()-2;
                int index = list.size()-1;
                int maxVal = map.get(str)[0];
                while(loop>=0) {
                    if(maxVal < map.get(list.get(loop))[0]) {
                        maxVal = map.get(list.get(loop))[0];
                        index = loop;
                    }
                    if(list.get(loop).contains(Integer.toString(lastIndex))) break;
                    loop--;
                }
                System.out.println("Removing: " + list.get(index));
                list.remove(index);
                System.out.println("List after removal: " + list);
            }
        }
//        return false;
    }

    void initiateCycleFindingProcess(int numberOfNodes) {
        boolean[] visitedArr = new boolean[numberOfNodes+1];
        findCycle(1, -1, visitedArr);
    }

    public static void main(String[] args) {
        FindMST_raw cycle = new FindMST_raw();
        int i = 7;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        for(int i = 1;i<=nodes;i++) {
//              int edges = (int)(Math.random() * i*(i-1)/2);
//              cycle.createGraphAndList(i, edges);
                cycle.createGraphAndList(i, 0);
//            long startTime = System.nanoTime();
            cycle.initiateCycleFindingProcess(i);
            System.out.println("MST edges: ");
            for(int j=1;j<cycle.list.size();j++) {
                System.out.println(cycle.list.get(j));
            }

//            long endTime = System.nanoTime();
//            dataset.addValue(endTime - startTime, "", Integer.toString(i+edges));
//            System.out.println("Execution time: " + (endTime - startTime));
//        }
        // Plotting the line graph for processing time as a function of the graph size
//        System.out.println("Yolo!");
//        PlotLineGraph plotLineGraph = new PlotLineGraph("Cycle Finding Graph");
//        plotLineGraph.plot(dataset, "Cycle Finding Graph" , "Nodes + Edges", "Execution Time(in ns)");
    }
}
