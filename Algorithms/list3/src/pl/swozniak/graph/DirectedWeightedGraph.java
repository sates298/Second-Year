package pl.swozniak.graph;

import pl.swozniak.queue.PriorityQueue;
import pl.swozniak.queue.QueueElement;

import java.util.ArrayList;
import java.util.List;

public class DirectedWeightedGraph extends WeightedGraph{

    public DirectedWeightedGraph(int n, int m){
        super(n, m);
    }

    @Override
    public boolean addEdge(int u, int v, double w){
         if(0 < u && u <= this.nodesNumber &&
                0 < v && v <= this.nodesNumber &&
                0 <= w &&
                this.edgesNumber < this.edges.length){
            this.edges[this.edgesNumber] = new WeightedEdge(u, v, w);
            this.edgesNumber++;
            this.nodes[u -1].addNeighbour(this.nodes[v-1], true);
            return true;
        }
        return false;
    }

    public boolean addEdge(WeightedEdge e){
        if(0 < e.getU() && e.getU() <= this.nodesNumber &&
                0 < e.getV() && e.getV() <= this.nodesNumber &&
                0 <= e.getW() && e.getU() != e.getV() &&
                this.edgesNumber < this.edges.length){
            this.edges[this.edgesNumber] = e;
            this.edgesNumber++;
            this.nodes[e.getU()-1].addNeighbour(this.nodes[e.getV() - 1], true);
            return true;
        }
        return false;
    }

    public WeightedEdge[][] dijkstra(int start){
        Node first = nodes[start-1];
        WeightedEdge[][] results = new WeightedEdge[nodesNumber][];

        PriorityQueue<Double> heap = new PriorityQueue<>(nodesNumber, 0.0);

        first.setDist(0.0);
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

    private WeightedEdge[] getPath(Node start, Node end){
        WeightedEdge[] result;
        List<WeightedEdge> temp = new ArrayList<>();
        Node curr = end;
        if(start.equals(end)){
            result = new WeightedEdge[1];
            result[0] = new WeightedEdge(start.getLabel(), start.getLabel(), 0);
        }else {
            while (curr.getPrevEdge() != null && curr.getPrev() != null) {
                temp.add(curr.getPrevEdge());
                curr = curr.getPrev();
            }
            result = new WeightedEdge[temp.size()];
            for(int i=0; i<result.length; i++){
                result[i] = temp.get(temp.size() - i - 1);
            }
        }
        return result;
    }

    public void printAllShortestPaths(int start){
        long time = System.currentTimeMillis();
        WeightedEdge[][] paths = dijkstra(start);
        for(Node n: nodes){
            System.out.println("Id: " + n.getLabel() + ", weight: " + n.getDist());
        }
        for (WeightedEdge[] path: paths) {
            for(WeightedEdge edge: path){
                System.err.print(edge);
            }
            System.err.println();
        }
        System.err.println("time: " + (System.currentTimeMillis() - time));
    }


}
