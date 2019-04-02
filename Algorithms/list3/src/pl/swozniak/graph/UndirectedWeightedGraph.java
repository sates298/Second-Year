package pl.swozniak.graph;

import pl.swozniak.queue.PriorityQueue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UndirectedWeightedGraph {
    private Node[] nodes;
    private WeightedEdge[] edges;
    private int nodesNumber;
    private int edgesNumber;

    public UndirectedWeightedGraph(int n, int m){
        nodes = new Node[n];
        for(int i=1; i<=n; i++){
            nodes[i-1] = new Node(i);
        }
        edges = new WeightedEdge[m];
        nodesNumber = n;
        edgesNumber = 0;
    }

    public boolean addEdge(int u, int v, double w){
        if(0 < u && u <= this.nodesNumber &&
                0 < v && v <= this.nodesNumber &&
                0 <= w &&
                this.edgesNumber < this.edges.length){
            this.edges[this.edgesNumber] = new WeightedEdge(u, v, w);
            this.edgesNumber++;
            return true;
        }
        return false;
    }

    public boolean addEdge(WeightedEdge e){
        if(0 < e.getU() && e.getU() <= this.nodesNumber &&
                0 < e.getV() && e.getV() <= this.nodesNumber &&
                0 <= e.getW() &&
                this.edgesNumber < this.edges.length){
            this.edges[this.edgesNumber] = e;
            this.edgesNumber++;
            return true;
        }
        return false;
    }

    public void PrimAlgorithm(){

    }

    public UndirectedWeightedGraph KruskalAlgorithm(){
        UndirectedWeightedGraph spanningTree = new UndirectedWeightedGraph(nodesNumber, nodesNumber-1);
        PriorityQueue<Double> heap = new PriorityQueue<>(edgesNumber, 0.0);
        for (int i=0; i<edgesNumber; i++) {
            heap.insert(i, this.edges[i].getW());
        }
        ArrayList<Set<Node>> sets = new ArrayList<>();
        for(Node n: this.nodes){
            Set<Node> set = new HashSet<>();
            n.setPrev(n);
            set.add(n);
            sets.add(set);
        }
        while(!heap.empty()){
            WeightedEdge curr = this.edges[heap.pop().getValue()];
            Node u = this.nodes[curr.getU()-1];
            Node v = this.nodes[curr.getV()-1];
            int indexU = findSetIndex(u, sets);
            int indexV = findSetIndex(v, sets);
            Set<Node> uSet = sets.get(indexU);
            Set<Node> vSet = sets.get(indexV);
            if(uSet != vSet){
                spanningTree.addEdge(curr);
                union(indexU, indexV, sets);
            }
        }
        return spanningTree;
    }

    private int findSetIndex(Node u, ArrayList<Set<Node>> array){
        for(int i=0;i< array.size(); i++){
            if(array.get(i).contains(u)){
                return i;
            }
        }
        return -1;
    }

    private Node findRoot(Set<Node> set){
        for(Node n: set){
            if(n.getPrev() == n){
                return n;
            }
        }
        return null;
    }

    private void union(int indexU, int indexV, ArrayList<Set<Node>> array){
        Set<Node> setU = array.get(indexU);
        Set<Node> setV = array.get(indexV);
        Node rootU = findRoot(setU);
        Node rootV = findRoot(setV);
        if(rootU == null || rootV == null){
            return;
        }
        if(setU.size() > setV.size()){
            rootV.setPrev(rootU);
            setU.addAll(setV);
            array.remove(indexV);
        }else{
            rootU.setPrev(rootV);
            setV.addAll(setU);
            array.remove(indexU);
        }
    }
}
