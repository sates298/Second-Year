package pl.swozniak.graph;

public class Node {
    private int label;
    private Node prev;
    private WeightedEdge prevEdge;
    private Double dist;

    public Node(int label) {
        this.label = label;
        this.dist = Double.MAX_VALUE/2;
    }

    public int getLabel() {
        return label;
    }

    public Node getPrev() {
        return prev;
    }

    public Double getDist() {
        return dist;
    }

    public WeightedEdge getPrevEdge() {
        return prevEdge;
    }

    public void setPrevEdge(WeightedEdge prevEdge) {
        this.prevEdge = prevEdge;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setDist(Double dist) {
        this.dist = dist;
    }
}
