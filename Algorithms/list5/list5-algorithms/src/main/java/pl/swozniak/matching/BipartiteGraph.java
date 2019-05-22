package pl.swozniak.matching;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class BipartiteGraph {
//    private int k;
    private int i;
    private Node[] v1;
    private Node[] v2;
    private int maxMatching;

    public BipartiteGraph(int k, int i) {
        this.maxMatching = 0;
//        this.k = k;
        this.i = i;
        v1 = new Node[(int) Math.pow(2, k)];
        v2 = new Node[v1.length];
        for (int l = 0; l < v1.length; l++) {
            v1[l] = new Node(l, VertexesSet.V1);
            v2[l] = new Node(l, VertexesSet.V2);
        }
        for (int j = 0; j < v1.length; j++) {
            generateNeighbours(j);
        }
    }

    private void generateNeighbours(int u) {
        boolean[] choices = new boolean[v2.length];
        int r;
        Random rand = new Random(System.nanoTime());
        int j = 0;
        while (j < this.i) {
            r = rand.nextInt();
            if (r < 0) r *= -1;
            r = r % v2.length;
            if (!choices[r]) {
                choices[r] = true;
                v1[u].neighbours.add(v2[r]);
                v2[r].neighbours.add(v1[u]);
                j++;
            }
        }
    }

    public void HopcroftsKarp(){
        boolean result;
        do{
            BFSStage();
            result = DFSStage();
        }while(result);
        int matching = 0;
        for(Node m: v1){
            if(m.matched != null){
                matching++;
//                System.out.println(m.value + " matched with " + m.matched.value);
            }
        }
        this.maxMatching = matching;
    }

    private void BFSStage(){
        for(Node m: v1){
            m.distance = -1;
        }
        Queue<Integer> q = new LinkedList<>();
        for(Node m: v1){
            if(m.matched == null){
                q.offer(m.value);
                m.distance = 0;
            }
        }
        while(!q.isEmpty()){
            Node curr = v1[q.poll()];
            for(Node w: curr.neighbours){
                if(w.matched != null && w.matched.distance == -1){
                    w.matched.distance = curr.distance + 1;
                    q.offer(w.matched.value);
                }
            }
        }
    }

    private boolean DFSStage(){
        for (Node n: v1) {
            n.visited = false;
        }
        boolean result = false;
        for(Node n: v1){
            if(n.matched == null){
                if(!result){
                    result = DFS(n);
                }else{
                    DFS(n);
                }
            }
        }
        return result;
    }

    private boolean DFS(Node x){
        x.visited = true;
        for(Node w: x.neighbours){
            if(w.matched == null){
                w.matched = x;
                x.matched = w;
                return true;
            }else{
                Node z = w.matched;
                if(!z.visited && z.distance == x.distance + 1){
                    if(DFS(z)){
                        w.matched = x;
                        x.matched = w;
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void printEdges() {
        for (Node n : v1) {
            System.out.println(n.toString());
        }
    }

    public void printResults(long time){
        long t = System.nanoTime() - time;
        System.out.println("Max matching = " + this.maxMatching);
        System.out.println("Time = " + t);
    }

    private class Node {
        private int value;
        private VertexesSet set;
        private int distance;
        private boolean visited;
        private ArrayList<Node> neighbours;
        private Node matched;

        public Node(int value, VertexesSet set) {
            this.value = value;
            this.set = set;
//            this.distance = -1;
            this.neighbours = new ArrayList<>();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{").append(this.value).append(" [ ");
            for (Node n : this.neighbours) {
                sb.append("{").append(n.value).append("} ");
            }
            sb.append("]}");
            return sb.toString();
        }
    }

    private enum VertexesSet {
        V1, V2
    }

    public static void main(String[] args) {
        BipartiteGraph graph = new BipartiteGraph(3, 2);
        graph.printEdges();
        long time = System.nanoTime();
        graph.HopcroftsKarp();
        graph.printResults(time);
    }
}
