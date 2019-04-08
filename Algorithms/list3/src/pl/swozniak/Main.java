package pl.swozniak;

import pl.swozniak.graph.DirectedWeightedGraph;
import pl.swozniak.graph.UndirectedWeightedGraph;
import pl.swozniak.input.CommandLineInterface;
import pl.swozniak.queue.PriorityQueue;


public class Main {

    public static void main(String[] args) {
//        heapTest();
//        dijkstraTest();
//        kruskalTest();
//        primTest();
        stronglyConnectedComponentTest();
        CommandLineInterface commandLineInterface = new CommandLineInterface();
//        commandLineInterface.run(args);
    }

    private static void heapTest(){
        PriorityQueue<Integer> heap = new PriorityQueue<>(10, 0);
        heap.insert(3, 0);
        heap.insert(3, 12);
        heap.insert(3, 17);
        heap.insert(3, 9);
        heap.insert(2, 11);
        heap.insert(2, 0);
        heap.print();
        heap.pop();
        heap.print();
        heap.insert(5, 12);
        heap.insert(5, 11);
        heap.insert(4, 15);
        heap.print();
        heap.priority(4, 10);
        heap.print();
    }

    private static void dijkstraTest(){
        int n = 10;
        int m = 100;
        DirectedWeightedGraph graph = new DirectedWeightedGraph(n, m);
        graph.fillNodes();
        graph.fillRandomEdges();
        /*graph.addEdge(1, 5, 3);
        graph.addEdge(5, 3, 21);
        graph.addEdge(3, 5, 32);
        graph.addEdge(4, 5, 2);
        graph.addEdge(2, 4, 4);
        graph.addEdge(4, 3, 23);
        graph.addEdge(5, 1, 2);
        graph.addEdge(3, 1, 24);
        graph.addEdge(3, 2, 37);
        graph.addEdge(1, 2, 7);*/
        graph.printAllShortestPaths(1);
    }

    private static void kruskalTest(){
        int n = 5;
        int m = 15;
        UndirectedWeightedGraph graph = new UndirectedWeightedGraph(n, m);
        graph.fillNodes();
//        graph.fillRandomEdges();
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
        graph.KruskalAlgorithm().print();
    }

    private static void primTest(){
        int n = 8;
        int m = 18;
        UndirectedWeightedGraph graph = new UndirectedWeightedGraph(n, m);
        graph.fillNodes();
//        graph.fillRandomEdges();
        graph.addEdge(1, 5, 3);
        graph.addEdge(5, 3, 2);
        graph.addEdge(3, 5, 3);
        graph.addEdge(4, 5, 2);
        graph.addEdge(6, 7, 4);
        graph.addEdge(4, 3, 2);
        graph.addEdge(5, 7, 2);
        graph.addEdge(3, 1, 6);
        graph.addEdge(6, 2, 1);
        graph.addEdge(1, 8, 7);
        graph.PrimAlgorithm().print();
    }

    private static void stronglyConnectedComponentTest(){
        int n = 8;
        int m = 14;
        DirectedWeightedGraph graph = new DirectedWeightedGraph(n, m);
        graph.fillNodes();
//        graph.fillRandomEdges();
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 8);
        graph.addEdge(4, 3);
        graph.addEdge(3, 7);
        graph.addEdge(8, 8);
        graph.addEdge(7, 8);
        graph.addEdge(2, 5);
        graph.addEdge(5, 1);
        graph.addEdge(5, 6);
        graph.addEdge(6, 7);
        graph.addEdge(7, 6);
        graph.addEdge(2, 6);
        graph.printAllComponents();

        DirectedWeightedGraph graph1 = new DirectedWeightedGraph(6, 6);
        graph1.fillNodes();
        graph1.addEdge(6, 3);
        graph1.addEdge(6, 1);
        graph1.addEdge(5, 1);
        graph1.addEdge(5, 2);
        graph1.addEdge(3, 4);
        graph1.addEdge(4, 2);
        graph1.printAllComponents();
    }

}
