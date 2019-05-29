package pl.swozniak.tests.matching;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.swozniak.matching.BipartiteGraph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
    public static void main(String[] args) throws IOException {
        int repeats = 100;
        long time, fullTime = System.currentTimeMillis();
        String fileName = "../list5-stats/matching" + repeats + ".json";
        File file = new File(fileName);
        ObjectMapper mapper = new ObjectMapper();
        List<JsonMatching> all = new ArrayList<>();
        JsonMatching jsonMatching;
        BipartiteGraph graph;
        for(int k=3; k <= 10; k++){
            for(int i=1; i<=k; i++){
                for(int r=0; r<repeats; r++){
                    graph = new BipartiteGraph(k, i);
                    time = System.nanoTime();
                    graph.HopcroftsKarp();
                    time = System.nanoTime() - time;
                    jsonMatching = new JsonMatching();
                    jsonMatching.k = k;
                    jsonMatching.i = i;
                    jsonMatching.reapeted = r;
                    jsonMatching.time = time;
                    jsonMatching.maxMatching = graph.getMaxMatching();
                    all.add(jsonMatching);
                }
            }
        }
        JsonAll jsonAll = new JsonAll();
        jsonAll.all = all;
        mapper.writeValue(file, jsonAll);
        System.out.println(System.currentTimeMillis() - fullTime);
    }
}
