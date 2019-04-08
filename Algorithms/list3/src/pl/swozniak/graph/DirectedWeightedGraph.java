package pl.swozniak.graph;

import pl.swozniak.queue.PriorityQueue;
import pl.swozniak.queue.QueueElement;

import java.util.ArrayList;
import java.util.List;

public class DirectedWeightedGraph extends WeightedGraph {

    private int dfsTime;
    private List<Node> topologicalSort;

    public DirectedWeightedGraph(int n, int m) {
        super(n, m, true);
        topologicalSort = new ArrayList<>();
    }

    public List<Node> getTopologicalSort() {
        return topologicalSort;
    }

    @Override
    public boolean addEdge(WeightedEdge e) {
        boolean result = super.addEdge(e);
        if (result) {
            matrixRepresentation[e.getU() - 1][e.getV() - 1] = e;
        }
        return result;
    }


    public WeightedEdge[][] dijkstra(int start) {
        Node first = nodes[start - 1];
        WeightedEdge[][] results = new WeightedEdge[nodesNumber][];

        PriorityQueue<Double> heap = new PriorityQueue<>(nodesNumber, 0.0);

        first.setDist(0.0);
        for (int i = 0; i < nodesNumber; i++) {
            heap.insert(nodes[i].getLabel(), nodes[i].getDist());
        }
        while (!heap.empty()) {
            QueueElement curr = heap.pop();
            for (int i = 0; i < edgesNumber; i++) {
                if (edges[i].getU() == curr.getValue()) {
                    Node u = nodes[edges[i].getU() - 1];
                    Node v = nodes[edges[i].getV() - 1];
                    if (v.getDist() > u.getDist() + edges[i].getW()) {
                        v.setDist(u.getDist() + edges[i].getW());
                        v.setPrev(u);
                        v.setPrevEdge(edges[i]);
                        heap.priority(v.getLabel(), v.getDist());
                    }

                }
            }
        }
        for (int i = 0; i < nodesNumber; i++) {
            results[i] = getPath(nodes[i]);
        }
        return results;
    }

    private WeightedEdge[] getPath(Node end) {
        WeightedEdge[] result;
        List<WeightedEdge> temp = new ArrayList<>();
        Node curr = end;
        if (curr.getPrev() == null) {
            result = new WeightedEdge[1];
            result[0] = new WeightedEdge(curr.getLabel(), curr.getLabel(), 0);
        } else {
            while (curr.getPrevEdge() != null && curr.getPrev() != null) {
                temp.add(curr.getPrevEdge());
                curr = curr.getPrev();
            }
            result = new WeightedEdge[temp.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = temp.get(temp.size() - i - 1);
            }
        }
        return result;
    }

    public void printAllShortestPaths(int start) {
        long time = System.currentTimeMillis();
        WeightedEdge[][] paths = dijkstra(start);
        for (Node n : nodes) {
            System.out.println("Id: " + n.getLabel() + ", weight: " + n.getDist());
        }
        for (WeightedEdge[] path : paths) {
            for (WeightedEdge edge : path) {
                System.err.print(edge);
            }
            System.err.println();
        }
        System.err.println("Time of dijkstra algorithm: " + (System.currentTimeMillis() - time));
    }

    public Node[][] stronglyConnectedComponents() {
        this.normalDFS();
        this.transpose();
        this.secondDFS();
        Node[][] result = findAllNodesPaths();
        this.transpose();
        return result;
    }

    private void normalDFS() {
        dfsTime = 0;
        for (Node n : this.nodes) {
            n.setVisited(false);
            n.setPrev(null);
            n.setPrevEdge(null);
            n.setLast(-1);
            n.setFirst(-1);
        }
        for (Node n : this.nodes) {
            if (!n.isVisited()) {
                explore(n, false);
            }
        }
    }

    private void secondDFS() {
        Node[] tmp = new Node[nodesNumber];
        for (int i = 0; i < nodesNumber; i++) {
            tmp[i] = this.nodes[i];
        }
        dfsTime = 0;
        for (Node n : this.nodes) {
            n.setVisited(false);
            n.setPrev(null);
            n.setPrevEdge(null);
//            n.setFirst(-1);
        }

        while (nodesNumber > 0) {
            Node max = findMaxLast();
            if (max != null) explore(max, true);
        }

        for (Node n : tmp) this.addNode(n);
    }

    private void explore(Node u, boolean isSecond) {
        u.setVisited(true);
        u.setFirst(++dfsTime);
        for (WeightedEdge e : this.matrixRepresentation[u.getLabel() - 1]) {
            if (e != null) {
                Node v = this.nodes[e.getV() - 1];
                if (e.getW() < Double.POSITIVE_INFINITY && v != null && !v.isVisited()) {
                    explore(v, isSecond);
                    v.setPrev(u);
                    v.setPrevEdge(e);
                }
            }
        }
        u.setLast(++dfsTime);
        if (isSecond) {
            deleteNode(u.getLabel());
        } else{
            this.topologicalSort.add(u);
            //u.setLast(++dfsTime);
        }
    }

    private void transpose() {
        for (int i = 0; i < this.edgesNumber; i++) {
            WeightedEdge e = this.edges[i];
            int u = e.getU();
            int v = e.getV();
            e.setU(v);
            e.setV(u);
            if (matrixRepresentation[u - 1][v - 1] == e) matrixRepresentation[u - 1][v - 1] = null;
            matrixRepresentation[v - 1][u - 1] = e;
        }
    }

    private Node findMaxLast() {
        Node max;
        int j = 0;
        while (j < this.nodes.length && this.nodes[j] == null) j++;
        if (j >= this.nodes.length) return null;
        max = this.nodes[j];
        if (max.getLast() == -1) return max;
        for (int i = 0; i < this.nodes.length; i++) {
            if (this.nodes[i] != null) {
                if (max.getLast() < this.nodes[i].getLast()) {
                    max = this.nodes[i];
                }
            }
        }
        return max;
    }

    private Node findMaxFirst() {
        Node max;
        int j = 0;
        while (j < this.nodes.length && this.nodes[j] == null) j++;
        if (j >= this.nodes.length) return null;
        max = this.nodes[j];
        if (max.getFirst() == -1) return max;
        for (int i = 0; i < this.nodes.length; i++) {
            if (this.nodes[i] != null) {
                if (max.getFirst() < this.nodes[i].getFirst()) {
                    max = this.nodes[i];
                }
            }
        }
        return max;
    }

    private Node[][] findAllNodesPaths() {
        Node[] tmp = new Node[nodesNumber];
        for (int i = 0; i < nodesNumber; i++) {
            this.nodes[i].setVisited(false);
            tmp[i] = this.nodes[i];
        }
        ArrayList<Node[]> array = new ArrayList<>();
        while (this.nodesNumber > 0) {
            Node n = findMaxFirst();
            array.add(this.getNodesPath(n));
        }

        for (Node n : tmp) this.addNode(n);
        Node[][] result = new Node[array.size()][];
        for (int i = 0; i < array.size(); i++) {
            result[array.size() - 1 - i] = array.get(i);
        }
        return result;
    }

    private Node[] getNodesPath(Node n) {
        Node[] result;
        ArrayList<Node> tmp = new ArrayList<>();
        Node curr = n;
        while (curr != null) {
            tmp.add(curr);
            this.deleteNode(curr.getLabel());
            curr = curr.getPrev();
        }
        result = new Node[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            result[i] = tmp.get(i);
        }

        return result;
    }

    private Node[][] getSortedComponents(Node[][] components) {
        DirectedWeightedGraph dag = new DirectedWeightedGraph(components.length, this.edgesNumber);
        dag.fillNodes();
        for (int i = 0; i < components.length; i++) {
            for (Node n : components[i]) {
                for (int j = 0; j < components.length; j++) {
                    if (i != j) {
                        for (Node u : components[j]) {
                            if (matrixRepresentation[n.getLabel() - 1][u.getLabel() - 1] != null) {
                                dag.addEdge(i+1, j+1);
                            }
                        }
                    }
                }
            }
        }
        dag.normalDFS();
        List<Node> sorted = dag.getTopologicalSort();
        Node[][] sortedNodes = new Node[sorted.size()][];
        for(int i=0; i<sorted.size(); i++){
            sortedNodes[i] = components[sorted.get(sorted.size() - 1 - i).getLabel() - 1];
        }
        return sortedNodes;
    }

    public void printAllComponents() {
        long time = System.currentTimeMillis();
        Node[][] components = getSortedComponents(stronglyConnectedComponents());
        for (Node[] comp : components) {
            System.out.print("[");
            for (Node n : comp) {
                System.out.print(n.getLabel() + " ");
            }
            System.out.println("]");
        }
        System.err.println("Time of getting strongly connected components: " + (System.currentTimeMillis() - time));
    }

}
