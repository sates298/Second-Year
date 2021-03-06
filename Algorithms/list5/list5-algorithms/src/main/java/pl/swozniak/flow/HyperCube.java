package pl.swozniak.flow;

import pl.swozniak.exceptions.IllegalDimensionException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

public class HyperCube {
    private int dimension;
    private int[][] nodes;

    public HyperCube(int k) throws IllegalDimensionException {
        if(k<1 || k>16){
            throw new IllegalDimensionException();
        }
        this.dimension = k;
        int pow = (int)Math.pow(2, k);
        nodes = new int [pow][k];

        for(int i=0; i<pow; i++){
            for(int j=0; j<k; j++){
                if(i < getNeighbour(i, j)){
                    nodes[i][j] = generateEdgeWeight(i, getNeighbour(i, j));
                }
            }
        }

    }

    public int getNeighbour(int node, int bit){
        return node^(1 << bit);
    }

    public int getBit(int u, int v){
        for(int i=0; i<dimension; i++){
            if(v == getNeighbour(u, i)) return i;
        }
        return -1;
    }

    private int getHamming(int value){
        int k =0;
        for(; value != 0; value >>= 1){
            k += value & 1;
        }
        return k;
    }

    private int generateEdgeWeight(int from, int to){
        int uH = getHamming(from), vH = getHamming(to);
        int l = Math.max(uH>dimension-uH?uH:dimension-uH, vH>dimension-vH?vH:dimension-vH);
        int random = new Random().nextInt();
        if(random < 0) random *= -1;
        return 1 + (random % (int)Math.pow(2, l));
    }

    public void printNodes(){
        for(int i=0; i<nodes.length; i++){
            System.out.print(i + ". ");
            for(int j=0; j<dimension; j++){
                System.out.print("["+ nodes[i][j] + "] ");
            }
            System.out.println();
        }
    }

    public int getDimension() {
        return dimension;
    }

    public int[][] getNodes() {
        return nodes;
    }

    private static int edgesNumber(int dimension){
        if(dimension <= 1) return 1;
        return 2*edgesNumber(dimension-1) + (int)Math.pow(2, dimension-1);
    }

    private static void modelToFile(HyperCube cube, String fileName) throws IOException {
        String modelPath = "../list5-model/flow_template.mod";
        String path = "../list5-model/flow_model_" + fileName + ".mod";
        File template = new File(modelPath);
        File model = new File(path);
        int i = 1;
        while (model.isFile()) {
            path = "../list5-model/flow_model_" + fileName + "(" + i + ").mod";
            model = new File(path);
            i++;
        }
        Files.copy(template.toPath(), model.toPath());

        FileWriter file = new FileWriter(path, true);
        BufferedWriter out = new BufferedWriter(file);

        int n = cube.nodes.length;

        out.write("\nparam n := " + n + ";\n");
        out.write("\n param : E: a := ");

        for(int j=0; j<n; j++){
            for(int k=0; k<cube.dimension; k++){
                int weight = cube.nodes[j][k];
                if(weight > 0) {
                    int neighbour = cube.getNeighbour(j, k);
                    out.write("\n   " + j + " " + neighbour + " " + weight);
                }
            }
        }
        out.write(";");
        out.close();
    }

    public static void main(String[] args) {
            String fileName = "";
            int k = 0;
            boolean isGlpk = false;
            try {
                for (int j = 0; j < args.length; j++) {
                    if("--size".equals(args[j]) && k == 0){
                        k = Integer.parseInt(args[++j]);
                    } else if ("--glpk".equals(args[j]) && "".equals(fileName)) {
                        fileName = args[++j];
                        isGlpk = true;
                    }
                }

                long time = System.nanoTime();
                HyperCube cube = new HyperCube(k);
                System.out.println(System.nanoTime() - time);
                EdmondsKarpAlgorithm eka = new EdmondsKarpAlgorithm(0, cube.getNodes().length - 1, cube);
                eka.compute(time);
                eka.printStats();
                if(isGlpk){
                    modelToFile(cube,fileName);
                }
//                cube.printNodes();
            }catch(NumberFormatException | IllegalDimensionException e){
                System.out.println("Wrong size!");
            }catch(IOException e){
                e.printStackTrace();
            }

    }
}
