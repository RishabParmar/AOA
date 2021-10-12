import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CycleFindTest {
    @Test
    public void TestWhenCycleExists() {
        int numOfNodes = 4;
        int numOfEdges = 3;
        LinkedList<Integer>[] adjList = new LinkedList[numOfNodes+1];
        for(int i = 0; i < adjList.length; i++) adjList[i] = new LinkedList<>();

        adjList[1].addAll(Arrays.asList(2));
        adjList[2].addAll(Arrays.asList(3,4));
        adjList[3].addAll(Arrays.asList(2,4));
        adjList[4].addAll(Arrays.asList(2,3));

        CycleFind graph = new CycleFind(adjList, numOfEdges);
        Assert.assertTrue(graph.initiateCycleFindingProcess());
    }

    @Test
    public void TestWhenCycleDoesNotExist() {
        int numOfNodes = 4;
        int numOfEdges = 2;
        LinkedList<Integer>[] adjList = new LinkedList[numOfNodes+1];
        for(int i = 0; i < adjList.length; i++) adjList[i] = new LinkedList<>();

        adjList[1].addAll(Arrays.asList(2));
        adjList[2].addAll(Arrays.asList(1,4));
        adjList[4].addAll(Arrays.asList(2));

        CycleFind graph = new CycleFind(adjList, numOfEdges);
        Assert.assertFalse(graph.initiateCycleFindingProcess());
    }
}
