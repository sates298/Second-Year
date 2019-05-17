package pl.swozniak.flows;

import java.util.LinkedList;
import java.util.Queue;

public class EdmondsKarpAlgorithm {
    private int source;
    private int target;
    private int[][] flow;
    private HyperCube graph;

    public EdmondsKarpAlgorithm(int source, int target, HyperCube cube) {
        this.source = source;
        this.target = target;
        flow = new int[cube.getNodes().length][cube.getDimension()];
        graph = cube;
    }

    public void compute(long time) {
        int maxFlow = 0;
        int paths = 0;
        while (true) {
            Integer[] parent = new Integer[graph.getNodes().length];
            Queue<Integer> queue = new LinkedList<>();

            queue.add(source);

            while (!queue.isEmpty()) {
                int curr = queue.poll();

                int[] capacities = graph.getNodes()[curr];
                for (int i = 0; i < graph.getDimension(); i++) {
                    if (capacities[i] == 0) continue;
                    int next = graph.getNeighbour(curr, i);

                    if (parent[next] == null && next != source && capacities[i] > flow[curr][i]) {
                        parent[next] = curr;
                        queue.add(next);
                    }
                }
            }

            if (parent[target] == null) {
                break;
            }

            paths++;

            int pushedFlow = Integer.MAX_VALUE;

            for (Integer t = target, p = parent[t]; p != null; t = p, p = parent[p]) {
                int bit = graph.getBit(p, t);
                pushedFlow = Math.min(pushedFlow, graph.getNodes()[p][bit] - flow[p][bit]);
            }

            for (Integer t = target, p = parent[t]; p != null; t = p, p = parent[p]) {
                int bit = graph.getBit(p, t);
                flow[p][bit] += pushedFlow;
                flow[t][bit] -= pushedFlow;
            }

            maxFlow += pushedFlow;
        }

        System.out.println("Max Flow = " + maxFlow);
        System.err.println("Time: " + (System.currentTimeMillis() - time));
        System.err.println("Paths number = " + paths);
    }
}
