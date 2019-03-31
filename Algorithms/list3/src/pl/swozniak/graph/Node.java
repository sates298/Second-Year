package pl.swozniak.graph;

public class Node {
    private int label;
    private Node prev;
    private DirectedWeightedEdge prevEdge;
    private int dist;

    public Node(int label) {
        this.label = label;
        this.dist = Integer.MAX_VALUE/2;
    }

    public int getLabel() {
        return label;
    }

    public Node getPrev() {
        return prev;
    }

    public int getDist() {
        return dist;
    }

    public DirectedWeightedEdge getPrevEdge() {
        return prevEdge;
    }

    public void setPrevEdge(DirectedWeightedEdge prevEdge) {
        this.prevEdge = prevEdge;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }
}
