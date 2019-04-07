package pl.swozniak.graph;

import java.util.Random;

public abstract class WeightedGraph {
    protected Node[] nodes;
    protected WeightedEdge[] edges;
    protected WeightedEdge[][] matrixRepresentation;
    protected int nodesNumber;
    protected int edgesNumber;
    protected final boolean isDirected;


    public WeightedGraph(int n, int m, boolean isDirected){
        this.isDirected = isDirected;
        this.nodes = new Node[n];
        this.edges = new WeightedEdge[m];
        this.edgesNumber = 0;
        this.nodesNumber = 0;
        matrixRepresentation = new WeightedEdge[n][n];
        for(int i=0; i<n; i++){
//            for(int j=0; j<n; j++){
//                matrixRepresentation[i][j] = new WeightedEdge(i, j, Double.POSITIVE_INFINITY);
                if(/*i == j &&*/ !isDirected){
                    matrixRepresentation[i][i] = new WeightedEdge(i, i, 0.0);
                }
//            }
        }
    }

    public void fillNodes(){
        for(int i=1; i<=this.nodes.length; i++){
            addNode(i);
        }
    }

    public boolean addNode(Node n){
        if(n.getLabel() > this.nodes.length && this.nodes[n.getLabel() - 1] != null){
            return false;
        }else{
            this.nodes[n.getLabel() - 1] = n;
            this.nodesNumber++;
            return true;
        }
    }

    public boolean addNode(int label){
        return addNode(new Node(label));
    }

    public boolean addEdge(WeightedEdge e){
        if(0 < e.getU() && e.getU() <= this.nodes.length && this.nodes[e.getU() - 1] != null &&
                0 < e.getV() && e.getV() <= this.nodes.length && this.nodes[e.getV() - 1] != null &&
                0 <= e.getW() && this.edgesNumber < this.edges.length){
            if(!isDirected && e.getU() == e.getV()) return false;
            this.edges[this.edgesNumber] = e;
            this.edgesNumber++;
            return true;
        }
        return false;
    }

    public boolean addEdge(int u, int v, double w){
        return addEdge(new WeightedEdge(u, v, w));
    }
    public boolean addEdge(int u, int v){
        return addEdge(new WeightedEdge(u, v, 1.0));
    }

    public boolean deleteNode(int label){
        if(label > this.nodes.length) return false;
        if(this.nodes[label - 1] != null){
            this.nodes[label - 1] = null;
            this.nodesNumber--;
            return true;
        }
        return false;
    }

    public void fillRandomEdges(){
        Random r = new Random(System.currentTimeMillis());
        for(int i=0; i<edges.length; i++){
            int f = r.nextInt(nodesNumber + 1);
            int s = r.nextInt(nodesNumber + 1);
            while(s == f) s = r.nextInt(nodesNumber + 1);
            this.addEdge(f, s, 0.5 + r.nextDouble()*10);
        }
    }
}
