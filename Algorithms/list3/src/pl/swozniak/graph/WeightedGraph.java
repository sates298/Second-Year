package pl.swozniak.graph;

public abstract class WeightedGraph {
    protected Node[] nodes;
    protected WeightedEdge[] edges;
    protected int nodesNumber;
    protected int edgesNumber;
    protected final boolean isDirected;


    public WeightedGraph(int n, int m, boolean isDirected){
        this.isDirected = isDirected;
        this.nodes = new Node[n];
        this.edges = new WeightedEdge[m];
        this.edgesNumber = 0;
        this.nodesNumber = 0;
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
            this.nodes[e.getU()-1].addNeighbour(this.nodes[e.getV() - 1], isDirected);
            return true;
        }
        return false;
    }

    public boolean addEdge(int u, int v, double w){
        return addEdge(new WeightedEdge(u, v, w));
    }
}
