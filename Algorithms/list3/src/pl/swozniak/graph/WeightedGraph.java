package pl.swozniak.graph;

public abstract class WeightedGraph {
    protected Node[] nodes;
    protected WeightedEdge[] edges;
    protected int nodesNumber;
    protected int edgesNumber;

    public WeightedGraph(int n, int m){
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

    public boolean addNode(int label){
        if(label > this.nodes.length && this.nodes[label - 1] != null){
            return false;
        }else{
            this.nodes[label - 1] = new Node(label);
            this.nodesNumber++;
            return true;
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
    public abstract boolean addEdge(int u, int v, double p);
    public abstract boolean addEdge(WeightedEdge e);

    public Node[] getNodes(){
        return this.nodes;
    }
}
