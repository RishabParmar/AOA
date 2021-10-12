import org.jfree.data.category.DefaultCategoryDataset;

import java.util.*;

public class MST_2 {

    LinkedList<Integer> adjList[];
    HashMap<String, Integer> weightedMap = new HashMap<>();
    ArrayList<String> stack;
    DefaultCategoryDataset plotDataset = new DefaultCategoryDataset();

    void initList(int numberOfNodes) {
        adjList = new LinkedList[numberOfNodes+1];
        for(int i = 0;i<adjList.length;i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    void addRandomWeightedEdge(int v1, int v2, int edgeWeight) {
        weightedMap.put(v1 + " " + v2, edgeWeight);
        weightedMap.put(v2 + " " + v1, edgeWeight);
    }

    void createAdjList(int nodes, int edges) {
        int start = 0;
        int end = 100;
        int randomRange = end - start + 1;
        int i=1;
        while(i<=edges) {
            if(i < nodes) {
                adjList[i].add(i+1);
                adjList[i+1].add(i);
                int weight = (int)(Math.random() * randomRange);
                weightedMap.put(i + " " + (i+1), weight);
                weightedMap.put((i+1) + " " + i, weight);
            }
            // Random weighted edges
            else {
                int v1;
                int v2;
                while(true) {
                    v1 = new Random().nextInt(nodes - start) + 1;
                    v2 = new Random().nextInt(nodes - start) + 1;
                    if(v1 != v2 && !weightedMap.containsKey(v1 + " " + v2)) {
                        break;
                    }
                }
                adjList[v1].add(v2);
                adjList[v2].add(v1);
                int edgeWeight = (int)(Math.random() * randomRange + start);
                addRandomWeightedEdge(v1, v2, edgeWeight);
            }
            i++;
        }
//        for(LinkedList<Integer> list: adjList) {
//            System.out.println(list);
//        }
//        System.out.println(weightedMap);
    }

    void removeEdge(int index) {
        String[] edgePoints = stack.get(index).split(" ");
        int num1 = Integer.parseInt(edgePoints[0]);
        int num2 = Integer.parseInt(edgePoints[1]);
        adjList[num1].remove(new Integer(num2));
        adjList[num2].remove(new Integer(num1));
    }

    int getMaxWeightEdgeIndex(String str, int lastIndex) {
        int loop = stack.size()-2;
        int index = stack.size()-1;
        int maxVal = weightedMap.get(str);
        while(loop>=0) {
            if(maxVal < weightedMap.get(stack.get(loop))) {
                maxVal = weightedMap.get(stack.get(loop));
                index = loop;
            }
            if(stack.get(loop).contains(Integer.toString(lastIndex))) break;
            loop--;
        }
        return index;
    }

    boolean findCycle(int currentVertex, int parentNode, boolean[] visited) {
        visited[currentVertex] = true;
        stack.add(parentNode + " " + currentVertex);
        for (int nodeIndex : adjList[currentVertex]) {
            if (!visited[nodeIndex]) {
                if(findCycle(nodeIndex, currentVertex, visited)) return true;
            }
            else if (nodeIndex != parentNode) {
                String str = currentVertex + " " + nodeIndex;
                stack.add(str);
                int index = getMaxWeightEdgeIndex(str, Integer.parseInt(str.split(" ")[1]));
                removeEdge(index);
                return true;
            }
        }
        stack.remove(stack.size()-1);
        return false;
    }

    boolean initiateFindingMST(int numberOfNodes) {
        int j = 0;
        while(j<9) {
            stack = new ArrayList<String>();
            boolean[] visited = new boolean[numberOfNodes+1];
            if (!findCycle(1, -1, visited)) return false;
            j++;
        }
        return true;
    }

    int computeEdgesOfMST(int i) {
        int edges;
        if(i < 6) {
            int range = (i*(i-1)/2) - i-1 +1 ;
            edges = (int)(Math.random() * range + (i-1));
        } else {
            int range = i+8 - i-1 + 1;
            edges = (int)(Math.random() * range + (i-1));
        }
        return edges;
    }

    void doProcess(int iterator, int numberOfNodes) {
        while(iterator<=numberOfNodes) {
            initList(numberOfNodes);
            int edges = computeEdgesOfMST(iterator);
            createAdjList(iterator, edges);
            long sTime = System.nanoTime();
            initiateFindingMST(iterator);
            long eTime = System.nanoTime();
            // Printing the adjacency linkedlist
//            System.out.println("MST: ");
//            for(LinkedList<Integer> temp: mst.adjList) {
//                System.out.println(temp);
//            }
            plotDataset.addValue(eTime - sTime, "", Integer.toString(iterator+edges));
            iterator++;
        }
    }

    public static void main(String[] args) {
        MST_2 mst = new MST_2();
        int numberOfNodes = 10;
        int iterator = 3;
        mst.doProcess(iterator, numberOfNodes);
        // Plotting the line graph for processing time as a function of the graph size
        PlotLineGraph plotLineGraph = new PlotLineGraph("MST");
        plotLineGraph.plot(mst.plotDataset, "Graph for finding Minimum Spanning Tree" , "V + E", "Run Time(in ns)");
    }
}
