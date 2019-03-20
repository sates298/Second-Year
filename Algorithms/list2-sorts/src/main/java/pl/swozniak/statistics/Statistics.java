package pl.swozniak.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.swozniak.sorts.*;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;

public class Statistics {

    private Random rand;
    private InsertionSort insert;
    private Sort select;
    private Sort heap;
    private Sort quick;
    private Sort mquick;

    public Statistics() {
        rand = new Random();
        insert = new InsertionSort();
        select = new SelectSort();
        heap = new HeapSort();
        quick = new QuickSort();
        mquick = new ModifiedQuickSort();
    }

    public void run(int k, String fileName, BiFunction<Comparable, Comparable, Integer> func) throws IOException, InterruptedException {
        Comparable[] root;
        int n;
        File file = new File(fileName);
        ObjectMapper mapper = new ObjectMapper();
        JsonAll all = new JsonAll();
        all.all = new ArrayList<>();
        for(int i=1; i<=100; i++){
            for(int j=0; j<k; j++){
                n = i*100;
                root = generateArray(n);
                sortAllTypes(root, func);
                saveToFile(n, file, mapper, all);
                resetCounters();
            }
        }
        mapper.writeValue(file, all);
    }

    private Comparable[] generateArray(int n){
        Comparable[] array = new Comparable[n];
        for(int i=0; i<n; i++){
            array[i] = rand.nextInt(1000);
        }
        return array;
    }

    private void sortAllTypes(Comparable[] array, BiFunction<Comparable, Comparable, Integer> func) throws InterruptedException {
        //Thread insertionThread = new Thread(() -> {
            Comparable[] toInsert = Arrays.copyOf(array,array.length);
            insert.sortArray(toInsert, func);
        //});
        //Thread selectThread = new Thread(() -> {
            Comparable[] toSelect = Arrays.copyOf(array, array.length);
            select.sortArray(toSelect, func);
        //});
        //Thread heapThread = new Thread(() -> {
            Comparable[] toHeap = Arrays.copyOf(array, array.length);
            heap.sortArray(toHeap, func);
        //});
        //Thread quickThread = new Thread(() -> {
            Comparable[] toQuick = Arrays.copyOf(array, array.length);
            quick.sortArray(toQuick, func);
        //});
        //Thread mquickThread = new Thread(() -> {
            Comparable[] toMquick = Arrays.copyOf(array, array.length);
            mquick.sortArray(toMquick, func);
        //});

        /*insertionThread.run();
        selectThread.run();
        heapThread.run();
        quickThread.run();
        mquickThread.run();

       insertionThread.join();
       selectThread.join();
       heapThread.join();
       quickThread.join();
       mquickThread.join();*/
    }

    private void saveToFile(int n,File file, ObjectMapper mapper, JsonAll all) throws IOException {
        JsonStatistics jsonStat = new JsonStatistics();
        jsonStat.n = n;
        jsonStat.sorts = new ArrayList<>();
        JsonSortStatistics jsonInsert = new JsonSortStatistics();
        jsonInsert.name = "insert";
        jsonInsert.compares = insert.getCompareCounter();
        jsonInsert.swaps = insert.getSwapCounter();
        jsonInsert.time = insert.getTime();
        jsonStat.sorts.add(jsonInsert);

        JsonSortStatistics jsonSelect = new JsonSortStatistics();
        jsonSelect.name = "select";
        jsonSelect.compares = select.getCompareCounter();
        jsonSelect.swaps = select.getSwapCounter();
        jsonSelect.time = select.getTime();
        jsonStat.sorts.add(jsonSelect);

        JsonSortStatistics jsonHeap = new JsonSortStatistics();
        jsonHeap.name = "heap";
        jsonHeap.compares = heap.getCompareCounter();
        jsonHeap.swaps = heap.getSwapCounter();
        jsonHeap.time = heap.getTime();
        jsonStat.sorts.add(jsonHeap);

        JsonSortStatistics jsonQuick = new JsonSortStatistics();
        jsonQuick.name = "quick";
        jsonQuick.compares = quick.getCompareCounter();
        jsonQuick.swaps = quick.getSwapCounter();
        jsonQuick.time = quick.getTime();
        jsonStat.sorts.add(jsonQuick);

        JsonSortStatistics jsonMquick = new JsonSortStatistics();
        jsonMquick.name = "mquick";
        jsonMquick.compares = mquick.getCompareCounter();
        jsonMquick.swaps = mquick.getSwapCounter();
        jsonMquick.time = mquick.getTime();
        jsonStat.sorts.add(jsonMquick);

        all.all.add(jsonStat);
        mapper.writeValue(file, all);

    }


    private void resetCounters(){
        insert.resetCounters();
        select.resetCounters();
        heap.resetCounters();
        quick.resetCounters();
        mquick.resetCounters();
    }

}
