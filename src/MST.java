import org.jfree.data.category.DefaultCategoryDataset;

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

    void createRandomGraphAndList(int numberOfNodes, int numberOfEdges) {
        initializeAdjList(numberOfNodes);
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
            }
            // After connecting the graph above, now adding randomized weighted edges to create cycles
            else {
                int vertice1;
                int vertice2;
                while(true) {
                    vertice1 = new Random().nextInt(numberOfNodes - min) + 1;
                    vertice2 = new Random().nextInt(numberOfNodes - min) + 1;
                    if(vertice1 != vertice2 && !map.containsKey(vertice1 + " " + vertice2)) {
                        break;
                    }
                }
                adjacencyList[vertice1].add(vertice2);
                adjacencyList[vertice2].add(vertice1);
                int weight = (int)(Math.random() * range + min);
                map.put(vertice1 + " " + vertice2, weight);
                map.put(vertice2 + " " + vertice1, weight);
            }
        }
        // For printing the adjacency list and weights for graph
//        for(LinkedList<Integer> list: adjacencyList) {
//            System.out.println(list);
//        }
//        System.out.println("Map: " + map);
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

    boolean findMST(int numberOfNodes) {
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
        int nodes = 4500;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i = 3;i<=nodes;i += 10) {
            int edges;
            if(i < 6) {
                int range = (i*(i-1)/2) - i-1 +1;
                edges = (int)(Math.random() * range + (i-1));
            } else {
                int range = i+8 - i-1 + 1;
                edges = (int)(Math.random() * range + (i-1));
            }
            mst.createRandomGraphAndList(i, edges);
            long startTime = System.nanoTime();
            mst.findMST(i);
            long endTime = System.nanoTime();
            // Printing the adjacency linkedlist
//            System.out.println("MST found: ");
//            for(LinkedList<Integer> list: mst.adjacencyList) {
//                System.out.println(list);
//            }
            dataset.addValue(endTime - startTime, "", Integer.toString(i+edges));
        }
        // Plotting the line graph for processing time as a function of the graph size
        PlotLineGraph plotLineGraph = new PlotLineGraph("Finding Minimum Spanning Tree");
        plotLineGraph.plot(dataset, "Minimum Spanning Tree" , "Vertices + Edges", "Execution Time(in ns)");
    }
}
