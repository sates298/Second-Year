package pl.swozniak.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.swozniak.sorts.*;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Random;
import java.util.function.BiFunction;

public class Statistics {

    private Random rand;
    private Sort insert;
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

    public void run(int k, String fileName, BiFunction<Comparable, Comparable, Integer> func) throws FileNotFoundException {
        Comparable[] root;
        int n;
        File file = new File(fileName);
        ObjectMapper mapper = new ObjectMapper();
        for(int i=1; i<100; i++){
            for(int j=0; j<k; j++){
                n = i*100;
                root = generateArray(n);
                sortAllTypes(root, func);
                saveToFile(n, file, mapper);
            }
        }
    }

    private Comparable[] generateArray(int n){
        Comparable[] array = new Comparable[n];
        for(int i=0; i<n; i++){
            array[i] = rand.nextInt(1000);
        }
        return array;
    }

    private void sortAllTypes(Comparable[] array, BiFunction<Comparable, Comparable, Integer> func){
        Comparable[] toInsert = array.clone();
        insert.sortArray(toInsert,func);
        Comparable[] toSelect = array.clone();
        select.sortArray(toSelect, func);
        Comparable[] toHeap = array.clone();
        heap.sortArray(toHeap, func);
        Comparable[] toQuick = array.clone();
        quick.sortArray(toQuick, func);
        Comparable[] toMQuick = array.clone();
        mquick.sortArray(toMQuick, func);
    }

    private void saveToFile(int n, File file, ObjectMapper mapper){

    }

}
