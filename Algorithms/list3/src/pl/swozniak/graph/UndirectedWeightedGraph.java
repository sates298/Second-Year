package pl.swozniak.graph;

import pl.swozniak.queue.PriorityQueue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UndirectedWeightedGraph extends WeightedGraph { ;

    public UndirectedWeightedGraph(int n, int m){
        super(n,m);
    }

    @Override
    public boolean addEdge(int u, int v, double w){
        if(0 < u && u <= this.nodesNumber &&
                0 < v && v <= this.nodesNumber &&
                0 <= w && u != v &&
                this.edgesNumber < this.edges.length){
            this.edges[this.edgesNumber] = new WeightedEdge(u, v, w);
            this.edgesNumber++;
            this.nodes[u-1].addNeighbour(this.nodes[v - 1], false);
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
            this.nodes[e.getU()-1].addNeighbour(this.nodes[e.getV() - 1], false);
            return true;
        }
        return false;
    }

    public UndirectedWeightedGraph PrimAlgorithm(){
        UndirectedWeightedGraph spanningTree = new UndirectedWeightedGraph(nodesNumber, nodesNumber - 1);
        PriorityQueue<Double> heap = new PriorityQueue<>(nodesNumber, 0.0);
        Node[] spanningNodes = spanningTree.getNodes();
        for(int i=1; i<nodesNumber; i++){
            spanningNodes[i] = null;
        }
        heap.insert(this.nodes[0].getLabel(), 0.0);

        return spanningTree;
    }

    public UndirectedWeightedGraph KruskalAlgorithm(){
        UndirectedWeightedGraph spanningTree = new UndirectedWeightedGraph(nodesNumber, nodesNumber-1);
        spanningTree.fillNodes();
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

            spanningTree.print();
            System.out.println("before if");
            if(uSet != vSet){
//                spanningTree.addNode(u);
//                spanningTree.addNode(v);
                spanningTree.addEdge(curr);
                union(indexU, indexV, sets);
            }
            spanningTree.print();
            System.out.println("after if");
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

    public void print(){
        for(WeightedEdge we: edges){
            System.out.print(we);
        }
        System.out.println();
    }
}
