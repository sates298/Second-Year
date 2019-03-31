package pl.swozniak.graph;

import pl.swozniak.queue.PriorityQueue;
import pl.swozniak.queue.QueueElement;

import java.util.ArrayList;
import java.util.List;

public class DirectedWeightedGraph {
    private Node[] nodes;
    private DirectedWeightedEdge[] edges;
    private int nodesNumber;
    private int edgesNumber;

    public DirectedWeightedGraph(int n, int m){
        this.nodes = new Node[n];
        for(int i=1; i<=n; i++){
            nodes[i-1] = new Node(i);
        }
        this.edges = new DirectedWeightedEdge[m];
        this.edgesNumber = 0;
        this.nodesNumber = n;
    }

    public boolean addEdge(int u, int v, int w){
        System.out.println(u + " " +edges.length+ " " + edgesNumber);
        if(0 < u && u <= this.nodesNumber &&
                0 < v && v <= this.nodesNumber &&
                0 <= w &&
                this.edgesNumber < this.edges.length){
            this.edges[this.edgesNumber] = new DirectedWeightedEdge(u, v, w);
            this.edgesNumber++;
            return true;
        }
        return false;
    }

    public DirectedWeightedEdge[][] djikstra(int start){
        Node first = nodes[start-1];
        DirectedWeightedEdge[][] results = new DirectedWeightedEdge[nodesNumber][];

        PriorityQueue heap = new PriorityQueue(nodesNumber);

        first.setDist(0);
        for(int i=0; i< nodesNumber; i++) {
            heap.insert(nodes[i].getLabel(), nodes[i].getDist());
        }
        while(!heap.empty()) {
            QueueElement curr = heap.pop();
            for(int i=0; i<edgesNumber; i++){
                if(edges[i].getU() == curr.getValue()){
                    Node u = nodes[edges[i].getU() - 1];
                    Node v = nodes[edges[i].getV() - 1];
                    if(v.getDist() > u.getDist() + edges[i].getW()){
                        v.setDist(u.getDist() + edges[i].getW());
                        v.setPrev(u);
                        v.setPrevEdge(edges[i]);
                        heap.priority(v.getLabel(), v.getDist());
                    }
                }
            }
        }
        for(int i=0; i<nodesNumber; i++){
            results[i] = getPath(first, nodes[i]);
        }
        return results;
    }

    private DirectedWeightedEdge[] getPath(Node start, Node end){
        DirectedWeightedEdge[] result;
        List<DirectedWeightedEdge> temp = new ArrayList<>();
        Node curr = end;
        if(start.equals(end)){
            result = new DirectedWeightedEdge[1];
            result[0] = new DirectedWeightedEdge(start.getLabel(), start.getLabel(), 0);
        }else {
            while (curr.getPrevEdge() != null && curr.getPrev() != null) {
                temp.add(curr.getPrevEdge());
                curr = curr.getPrev();
            }
            result = new DirectedWeightedEdge[temp.size()];
            for(int i=0; i<result.length; i++){
                result[i] = temp.get(temp.size() - i - 1);
            }
        }
        return result;
    }

    public void printAllShortestPaths(int start){
        DirectedWeightedEdge[][] paths = djikstra(start);
        for(Node n: nodes){
            System.out.println("Id: " + n.getLabel() + ", weight: " + n.getDist());
        }
        for (DirectedWeightedEdge[] path: paths) {
            System.err.println();
            for(DirectedWeightedEdge edge: path){
                System.err.print(edge);
            }
        }
    }


}
