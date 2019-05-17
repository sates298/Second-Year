package pl.swozniak.tests.flow;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.swozniak.exceptions.IllegalDimensionException;
import pl.swozniak.flow.EdmondsKarpAlgorithm;
import pl.swozniak.flow.HyperCube;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
    public static void main(String[] args) throws IllegalDimensionException, IOException {
        int repeats = 100;
        long fullTime = System.currentTimeMillis();
        String fileName = "../list5-stats/flow" + repeats + ".json";
        File file = new File(fileName);
        ObjectMapper mapper = new ObjectMapper();
        List<JsonFlow> all = new ArrayList<>();
        JsonFlow jsonFlow;
        HyperCube cube;
        EdmondsKarpAlgorithm eka;
        long time;
        for(int k=1; k<=16; k++){
            for(int i=0; i<repeats; i++){
                time = System.nanoTime();
                cube = new HyperCube(k);
                eka = new EdmondsKarpAlgorithm(0, cube.getNodes().length - 1, cube);
                eka.compute(time);
                jsonFlow = new JsonFlow();
                jsonFlow.k = k;
                jsonFlow.time = eka.getTime();
                jsonFlow.maxFlow = eka.getMaxFlow();
                jsonFlow.paths = eka.getPaths();
                all.add(jsonFlow);
            }
        }
        JsonAll jsonAll = new JsonAll();
        jsonAll.all = all;
        mapper.writeValue(file, jsonAll);
        System.out.println("Full time = " + (System.currentTimeMillis() - fullTime));
    }
}
