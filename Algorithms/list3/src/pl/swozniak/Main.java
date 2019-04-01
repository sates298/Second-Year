package pl.swozniak;

import pl.swozniak.graph.DirectedWeightedGraph;
import pl.swozniak.input.CommandLineInterface;
import pl.swozniak.queue.PriorityQueue;

public class Main {

    public static void main(String[] args) {
        heapTest();
        djikstrTest();
        CommandLineInterface commandLineInterface = new CommandLineInterface();
//        commandLineInterface.run(args);
    }

    private static void heapTest(){
        PriorityQueue heap = new PriorityQueue(10);
        heap.insert(3, 17);
        heap.insert(3, 12);
        heap.insert(3, 9);
        heap.insert(2, 10);
        heap.insert(2, 0);
        heap.print();
        heap.pop();
        heap.print();
        heap.insert(5, 12);
        heap.insert(5, 11);
        heap.insert(4, 15);
        heap.print();
        heap.priority(3, 10);
        heap.print();
    }

    private static void djikstrTest(){
        DirectedWeightedGraph graph = new DirectedWeightedGraph(5, 10);
        graph.addEdge(1, 5, 3);
        graph.addEdge(5, 3, 2);
        graph.addEdge(3, 5, 3);
        graph.addEdge(4, 5, 2);
        graph.addEdge(2, 4, 4);
        graph.addEdge(4, 3, 2);
        graph.addEdge(5, 1, 2);
        graph.addEdge(3, 1, 6);
        graph.addEdge(3, 2, 1);
        graph.addEdge(1, 2, 7);
        graph.printAllShortestPaths(5);
    }
}
