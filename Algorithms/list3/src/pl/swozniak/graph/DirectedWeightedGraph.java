package pl.swozniak.graph;

import pl.swozniak.queue.PriorityQueue;
import pl.swozniak.queue.QueueElement;

import java.util.ArrayList;
import java.util.List;

public class DirectedWeightedGraph extends WeightedGraph {

    private int dfsTime;

    public DirectedWeightedGraph(int n, int m) {
        super(n, m, true);
    }

    @Override
    public boolean addEdge(WeightedEdge e) {
        boolean result = super.addEdge(e);
        if (result) {
            matrixRepresentation[e.getU() - 1][e.getV() - 1] = e;
        }
        return result;
    }

    public WeightedEdge[][] dijkstra(int start) {
        Node first = nodes[start - 1];
        WeightedEdge[][] results = new WeightedEdge[nodesNumber][];

        PriorityQueue<Double> heap = new PriorityQueue<>(nodesNumber, 0.0);

        first.setDist(0.0);
        for (int i = 0; i < nodesNumber; i++) {
            heap.insert(nodes[i].getLabel(), nodes[i].getDist());
        }
        while (!heap.empty()) {
            QueueElement curr = heap.pop();
            for (int i = 0; i < edgesNumber; i++) {
                if (edges[i].getU() == curr.getValue()) {
                    Node u = nodes[edges[i].getU() - 1];
                    Node v = nodes[edges[i].getV() - 1];
                    if (v.getDist() > u.getDist() + edges[i].getW()) {
                        v.setDist(u.getDist() + edges[i].getW());
                        v.setPrev(u);
                        v.setPrevEdge(edges[i]);
                        heap.priority(v.getLabel(), v.getDist());
                    }

                }
            }
        }
        for (int i = 0; i < nodesNumber; i++) {
            results[i] = getPath(nodes[i]);
        }
        return results;
    }

    private WeightedEdge[] getPath(Node end) {
        WeightedEdge[] result;
        List<WeightedEdge> temp = new ArrayList<>();
        Node curr = end;
        if (curr.getPrev() == null) {
            result = new WeightedEdge[1];
            result[0] = new WeightedEdge(curr.getLabel(), curr.getLabel(), 0);
        } else {
            while (curr.getPrevEdge() != null && curr.getPrev() != null) {
                temp.add(curr.getPrevEdge());
                curr = curr.getPrev();
            }
            result = new WeightedEdge[temp.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = temp.get(temp.size() - i - 1);
            }
        }
        return result;
    }

    public void printAllShortestPaths(int start) {
        long time = System.currentTimeMillis();
        WeightedEdge[][] paths = dijkstra(start);
        for (Node n : nodes) {
            System.out.println("Id: " + n.getLabel() + ", weight: " + n.getDist());
        }
        for (WeightedEdge[] path : paths) {
            for (WeightedEdge edge : path) {
                System.err.print(edge);
            }
            System.err.println();
        }
        System.err.println("time: " + (System.currentTimeMillis() - time));
    }

    public void dFS(int start) {
        dfsTime = 0;
        for(Node n: this.nodes){
            n.setVisited(false);
            n.setPrev(null);
            n.setPrevEdge(null);
            n.setComponentLabel(0);
        }

        explore(this.nodes[start - 1], start);
        for(Node n: this.nodes){
            if (!n.isVisited()){
                explore(n, n.getLabel());
            }
        }
    }

    private void explore(Node u, int componentLabel) {
        u.setVisited(true);
        u.setComponentLabel(componentLabel);
        u.setFirst(++dfsTime);
        for(WeightedEdge e: this.matrixRepresentation[u.getLabel() - 1]){
            if( e != null) {
                Node v = this.nodes[e.getV() - 1];
                if (e.getW() < Double.POSITIVE_INFINITY && !v.isVisited()) {
                    explore(v, componentLabel);
                    v.setPrev(u);
                    v.setPrevEdge(e);
                }
            }
        }
        u.setLast(++dfsTime);
    }

    private void transpose(){
        for(WeightedEdge e: this.edges){
            int u = e.getU();
            int v = e.getV();
            e.setU(v);
            e.setV(u);
        }
    }


}
