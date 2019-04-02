package pl.swozniak.graph;

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

    public void PrimAlgorithm(){

    }

    public void KruskalAlgorithm(){

    }
}
