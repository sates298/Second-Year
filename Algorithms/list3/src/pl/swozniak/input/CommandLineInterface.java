package pl.swozniak.input;

import pl.swozniak.graph.DirectedWeightedGraph;
import pl.swozniak.graph.UndirectedWeightedGraph;
import pl.swozniak.graph.WeightedEdge;
import pl.swozniak.graph.WeightedGraph;
import pl.swozniak.queue.PriorityQueue;

import java.util.Scanner;

public class CommandLineInterface {
    private Scanner sc;

    public CommandLineInterface() {
        this.sc = new Scanner(System.in);
    }

    public void run(String[] args) {
        String line;
        int choose;
        System.out.println("1. Priority Queue (heap) interface");
        System.out.println("2. Djikstra algorithm interface");
        System.out.println("3. Spanning tree interface");
        System.out.println("4. Strongly connected component interface");
        System.out.println("Choose interface: ");
        line = this.sc.nextLine();
        try {
            choose = Integer.parseInt(line);
            try {
                switch (choose) {
                    case 1:
                        this.runHeapInterface();
                        break;
                    case 2:
                        this.runDijkstraInterface();
                        break;
                    case 3:
                        try {
                            char alg = args[0].charAt(1);
                            if(alg == 'p' || alg == 'k') {
                                this.runSpanningTreeInterface(alg);
                            }else{
                                throw new ArrayIndexOutOfBoundsException();
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("you have to restart program with '-k' or '-p' argument");
                        }
                        return;
                    case 4:
                        this.runStronglyConnectedComponentInterface();
                        break;
                    default:
                        System.out.println("WRONG CHOOSE!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong argument!");
            }

        } catch (NumberFormatException e) {
            System.out.println("ITS NOT INTEGER!");
        }
    }

    private void runHeapInterface() throws NumberFormatException {
        int m, x, p;
        String mS, line;
        String[] arrayLine;
        System.out.println("Enter m: ");
        mS = this.sc.nextLine();
        m = Integer.parseInt(mS);
        if (m < 1) throw new NumberFormatException();
        PriorityQueue<Integer> heap = new PriorityQueue(m, 0);
        for (int i = 0; i < m; i++) {
            System.out.println((i + 1) + ". Write operation: ");
            line = this.sc.nextLine();
            arrayLine = line.split(" ");
            try {
                switch (arrayLine[0]) {
                    case "insert":
                        x = Integer.parseInt(arrayLine[1]);
                        p = Integer.parseInt(arrayLine[2]);
                        heap.insert(x, p);
                        break;
                    case "empty":
                        System.out.println(heap.empty());
                        break;
                    case "top":
                        heap.top();
                        break;
                    case "pop":
                        heap.pop();
                        break;
                    case "priority":
                        x = Integer.parseInt(arrayLine[1]);
                        p = Integer.parseInt(arrayLine[2]);
                        heap.priority(x, p);
                        break;
                    case "print":
                        heap.print();
                        break;
                    default:
                        System.out.println("Wrong operation!");
                        i--;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Too few arguments!");
                i--;
            } catch (NumberFormatException n) {
                System.out.println("Integer arguments expected!");
            }
        }

    }

    private void runDijkstraInterface() throws NumberFormatException {
        String line;
        int n, m, start = 0;
        System.out.println("Enter number of nodes: ");
        line = sc.nextLine();
        n = Integer.parseInt(line);
        System.out.println("Enter number of edges: ");
        line = sc.nextLine();
        m = Integer.parseInt(line);
        DirectedWeightedGraph graph = new DirectedWeightedGraph(n, m);
        addMWeightedEdgesToGraph(m, graph);
        /*for (int i = 0; i < m; i++) {
            System.out.println((i + 1) + ". Enter new edge (u, v, w)");
            line = sc.nextLine();
            edgeS = line.split(" ");
            try {
                u = Integer.parseInt(edgeS[0]);
                v = Integer.parseInt(edgeS[1]);
                w = Integer.parseInt(edgeS[2]);
                if (!graph.addEdge(u, v, w)) {
                    throw new Exception();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Too few arguments");
                i--;
            } catch (NumberFormatException e) {
                System.out.println("Integer expected");
                i--;
            } catch (Exception e) {
                System.out.println("Can't add that edge");
                i--;
            }
        }*/
        while (start <= 0 || start > n) {
            System.out.println("Enter start node: ");
            line = sc.nextLine();
            try {
                start = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Integer between 1 and n expected ");
            }
        }
        System.out.println("Score: ");
        graph.printAllShortestPaths(start);
    }

    private void runSpanningTreeInterface(char alg) {
        String line;
        int n, m;
        System.out.println("Enter number of nodes: ");
        line = sc.nextLine();
        n = Integer.parseInt(line);
        System.out.println("Enter number of edges: ");
        line = sc.nextLine();
        m = Integer.parseInt(line);
        UndirectedWeightedGraph graph = new UndirectedWeightedGraph(n, m);
        addMWeightedEdgesToGraph(m, graph);
        /*for (int i = 0; i < m; i++) {
            System.out.println((i + 1) + ". Enter new edge (u, v, w)");
            line = sc.nextLine();
            edgeS = line.split(" ");
            try {
                u = Integer.parseInt(edgeS[0]);
                v = Integer.parseInt(edgeS[1]);
                w = Integer.parseInt(edgeS[2]);
                if (!graph.addEdge(u, v, w)) {
                    throw new Exception();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Too few arguments");
                i--;
            } catch (NumberFormatException e) {
                System.out.println("Integer expected");
                i--;
            } catch (Exception e) {
                System.out.println("Can't add that edge");
                i--;
            }
        }*/
        if(alg == 'p'){
            graph.primAlgorithm().print();
        }else if(alg == 'k'){
            graph.kruskalAlgorithm().print();
        }else{
            System.out.println("Wrong argument of algorithm");
        }
        System.out.println("Spanning tree " + alg);
    }

    private void runStronglyConnectedComponentInterface() {
        String line, edgeS[];
        int n, m, u, v;
        System.out.println("Enter number of nodes: ");
        line = sc.nextLine();
        n = Integer.parseInt(line);
        System.out.println("Enter number of edges: ");
        line = sc.nextLine();
        m = Integer.parseInt(line);
        DirectedWeightedGraph graph = new DirectedWeightedGraph(n,m);
        graph.fillNodes();
        for (int i = 0; i < m; i++) {
            System.out.println((i + 1) + ". Enter new edge (u, v)");
            line = sc.nextLine();
            edgeS = line.split(" ");
            try {
                u = Integer.parseInt(edgeS[0]);
                v = Integer.parseInt(edgeS[1]);
                if (!graph.addEdge(u, v)) {
                    throw new Exception();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Too few arguments");
                i--;
            } catch (NumberFormatException e) {
                System.out.println("Integer expected");
                i--;
            } catch (Exception e) {
                System.out.println("Can't add that edge");
                i--;
            }
        }
        graph.printAllComponents();
        System.out.println("Strongly Connected Component");
    }

    private void addMWeightedEdgesToGraph(int m, WeightedGraph graph){
        graph.fillNodes();
        String line, edgeS[];
        int u, v, w;
        for (int i = 0; i < m; i++) {
            System.out.println((i + 1) + ". Enter new edge (u, v, w)");
            line = sc.nextLine();
            edgeS = line.split(" ");
            try {
                u = Integer.parseInt(edgeS[0]);
                v = Integer.parseInt(edgeS[1]);
                w = Integer.parseInt(edgeS[2]);
                if (!graph.addEdge(u, v, w)) {
                    throw new Exception();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Too few arguments");
                i--;
            } catch (NumberFormatException e) {
                System.out.println("Integer expected");
                i--;
            } catch (Exception e) {
                System.out.println("Can't add that edge");
                i--;
            }
        }
    }
}
