package pl.swozniak.graph;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int label;
    private Node prev;
    private WeightedEdge prevEdge;
    private Double dist;
    private List<Node> neighbours;
    private boolean visited;

    public Node(int label) {
        this.label = label;
        this.dist = Double.MAX_VALUE;
        this.neighbours = new ArrayList<>();
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

    public List<Node> getNeighbours(){
        return this.neighbours;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }


    public void addNeighbour(Node n, boolean isDirected){
        if (!this.neighbours.contains(n)) {
            this.neighbours.add(n);
            if(!isDirected) n.addNeighbour(this, false);
        }
    }

}
