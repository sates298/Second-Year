package pl.swozniak.graph;


public class Node {
    private int label;
    private Node prev;
    private WeightedEdge prevEdge;
    private Double dist;

    private boolean visited;
    private int first;
    private int last;
    private int componentLabel;

    public Node(int label) {
        this.label = label;
        this.dist = Double.POSITIVE_INFINITY;
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

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getComponentLabel() {
        return componentLabel;
    }

    public void setComponentLabel(int componentLabel) {
        this.componentLabel = componentLabel;
    }
}
