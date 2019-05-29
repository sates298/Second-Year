package pl.swozniak.matching;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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

    public void HopcroftsKarp() {
        boolean result;
        do {
            BFSStage();
            result = DFSStage();
        } while (result);
        int matching = 0;
        for (Node m : v1) {
            if (m.matched != null) {
                matching++;
//                System.out.println(m.value + " matched with " + m.matched.value);
            }
        }
        this.maxMatching = matching;
    }

    private void BFSStage() {
        for (Node m : v1) {
            m.distance = -1;
        }
        Queue<Integer> q = new LinkedList<>();
        for (Node m : v1) {
            if (m.matched == null) {
                q.offer(m.value);
                m.distance = 0;
            }
        }
        while (!q.isEmpty()) {
            Node curr = v1[q.poll()];
            for (Node w : curr.neighbours) {
                if (w.matched != null && w.matched.distance == -1) {
                    w.matched.distance = curr.distance + 1;
                    q.offer(w.matched.value);
                }
            }
        }
    }

    private boolean DFSStage() {
        for (Node n : v1) {
            n.visited = false;
        }
        boolean result = false;
        for (Node n : v1) {
            if (n.matched == null) {
                if (!result) {
                    result = DFS(n);
                } else {
                    DFS(n);
                }
            }
        }
        return result;
    }

    private boolean DFS(Node x) {
        x.visited = true;
        for (Node w : x.neighbours) {
            if (w.matched == null) {
                w.matched = x;
                x.matched = w;
                return true;
            } else {
                Node z = w.matched;
                if (!z.visited && z.distance == x.distance + 1) {
                    if (DFS(z)) {
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

    public void printResults(long time) {
        long t = System.nanoTime() - time;
        System.out.println("Max matching = " + this.maxMatching);
        System.out.println("Time = " + t);
    }

    public int getMaxMatching() {
        return maxMatching;
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


    private static void modelToFile(BipartiteGraph graph, String fileName) throws IOException {
        String modelPath = "../list5-model/matches_templates.mod";
        String path = "../list5-model/matches_model_" + fileName + ".mod";
        File template = new File(modelPath);
        File model = new File(path);
        int i = 1;
        while (model.isFile()) {
            path = "../list5-model/matches_model_" + fileName + "(" + i + ").mod";
            model = new File(path);
            i++;
        }
        Files.copy(template.toPath(), model.toPath());

        FileWriter file = new FileWriter(path, true);
        BufferedWriter out = new BufferedWriter(file);

        int v1 = graph.v1.length;
        int n = 2 + v1 * 2;

        out.write("\nparam n := " + n + ";\n");
        out.write("\n param : E: a := \n");
        for (int j = 0; j < v1; j++) {
            out.write("    0 " + (j + 1) + " 1\n");
        }
        for (Node v : graph.v1) {
            for (Node u : v.neighbours) {
                out.write("    " + (v.value + 1) + " " + (u.value + v1 + 1) + " 1\n");
            }
        }
        for (int j = 0; j < v1; j++) {
            out.write("    " + (j + v1 + 1) + " " + (n - 1) + " 1");
            if(j == v1 - 1){
                out.write(";");
            }else{
                out.write("\n");
            }
        }

        out.close();
    }

    public static void main(String[] args) {
        int k = 0, i = 0;
        String fileName = "";
        boolean isGlpk = false;
        try {
            for (int j = 0; j < args.length; j++) {
                if ("--size".equals(args[j]) && k == 0) {
                    k = Integer.parseInt(args[++j]);
                } else if ("--degree".equals(args[j]) && i == 0) {
                    i = Integer.parseInt(args[++j]);
                } else if ("--glpk".equals(args[j]) && "".equals(fileName)) {
                    fileName = args[++j];
                    isGlpk = true;
                }
            }
            long time = System.nanoTime();
            BipartiteGraph graph = new BipartiteGraph(k, i);
//            graph.printEdges();

            graph.HopcroftsKarp();
            graph.printResults(time);
            if (isGlpk) {
                modelToFile(graph, fileName);
            }
        } catch (IOException en) {
            en.printStackTrace();
        } catch (Exception e) {
            System.err.println("something goes wrong");
        }
    }
}
