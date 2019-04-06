package pl.swozniak.graph;

import pl.swozniak.queue.PriorityQueue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UndirectedWeightedGraph extends WeightedGraph {

    public UndirectedWeightedGraph(int n, int m){
        super(n,m, false);
    }

    @Override
    public boolean addEdge(WeightedEdge e){
        boolean result = super.addEdge(e);
        if(result){
            matrixRepresentation[e.getU() - 1][e.getV() - 1] = e;
            matrixRepresentation[e.getV() - 1][e.getU() - 1] = e;
        }
        return result;
    }


    public UndirectedWeightedGraph PrimAlgorithm(){
        UndirectedWeightedGraph spanningTree = new UndirectedWeightedGraph(nodesNumber, nodesNumber - 1);
        PriorityQueue<Double> heap = new PriorityQueue<>(nodesNumber, 0.0);
        spanningTree.addNode(this.nodes[0]);
        this.nodes[0].setPrev(this.nodes[0]);
        this.nodes[0].setDist(0.0);
        for(Node n: this.nodes){
            if(n.getLabel() > 1) n.setDist(Double.POSITIVE_INFINITY);
            heap.insert(n.getLabel(), n.getDist());
        }

        while(!heap.empty()){
            Node curr = this.nodes[heap.pop().getValue() - 1];
            spanningTree.addNode(curr);
            spanningTree.addEdge(matrixRepresentation[curr.getLabel() - 1][curr.getPrev().getLabel() - 1]);
            for(Node n: this.nodes){
                if(n.getLabel() != curr.getLabel()) {
                    WeightedEdge tmp = matrixRepresentation[n.getLabel() - 1][curr.getLabel() - 1];
                    if (tmp != null && heap.priority(n.getLabel(), tmp.getW())) {
                        n.setPrev(curr);
                    }
                }
            }
        }
        return spanningTree;
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
                spanningTree.addNode(u);
                spanningTree.addNode(v);
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

    public void print(){
        double sum = 0;
        for(WeightedEdge we: edges){
            if(we.getU() > we.getV()) {
                System.out.println("(" + we.getV() + " --{" + we.getW() + "}-- " + we.getU() + ")");
            }else System.out.println(we);
            sum += we.getW();
        }
        System.out.println("Weight of MST = " + sum);
    }
}
