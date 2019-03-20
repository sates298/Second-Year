package pl.swozniak.sorts;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class HeapSort implements Sort {

    private int swapCounter = 0;
    private int compareCounter = 0;
    private long time;

    @Override
    public void sortList(List<Comparable> toSort, BiFunction<Comparable, Comparable, Integer> func) {
        time = System.currentTimeMillis();

        //build heap
        for (int i = 1; i < toSort.size(); i++) {
            shiftListUp(toSort, i, func);
        }

        for (int i = toSort.size() - 1; i > 0; i--) {
            deleteListTop(toSort, i, func);
        }

        time = System.currentTimeMillis() - time;
        System.err.println("Algorithm time: " + time  + " ms");
        System.err.println("CompareCounter = " + getCompareCounter());
        System.err.println("SwapCounter = " + getSwapCounter());
    }

    private void shiftListUp(List<Comparable> heap, int index, BiFunction<Comparable, Comparable, Integer> func) {
        this.compareCounter++;
        System.err.println("compare in shiftUp " + heap.get(index) + " and " + heap.get(index/2));
        if (func.apply(heap.get(index), heap.get(index / 2)) < 0) {
            this.swapCounter+=2;
            System.err.println("swap indexes in shiftUp " + index + " and " + index/2);
            Collections.swap(heap, index, index / 2);
            shiftListUp(heap, index / 2, func);
        }
    }

    private void shiftListDown(List<Comparable> heap, int index, int endHeap, BiFunction<Comparable, Comparable, Integer> func) {
        if (index * 2 > endHeap) {
            return;
        }
        int compareIndex;
        if (index * 2 + 1 > endHeap) {
            compareIndex = index * 2;
        } else {
            compareIndex = matchElement(heap.get(index * 2), heap.get(index * 2 + 1),
                    index * 2, index * 2 + 1, func);
        }
        this.compareCounter++;
        System.err.println("compare in shiftDown " + heap.get(index) + " and " + heap.get(compareIndex));
        if (func.apply(heap.get(index), heap.get(compareIndex)) > 0) {
            this.swapCounter+=2;
            System.err.println("swap indexes " + index +" and " + compareIndex);
            Collections.swap(heap, index, compareIndex);
            shiftListDown(heap, compareIndex, endHeap, func);
        }
    }

    private void deleteListTop(List<Comparable> heap, int endHeap, BiFunction<Comparable, Comparable, Integer> func) {
        this.swapCounter+=2;
        System.err.println("swap indexes in deleteTop 0 and " + endHeap);
        Collections.swap(heap, 0, endHeap);
        shiftListDown(heap, 0, endHeap - 1, func);
    }

    @Override
    public void sortArray(Comparable[] toSort, BiFunction<Comparable, Comparable, Integer> func) {
        System.err.println("heap");
        time = System.currentTimeMillis();

        for (int i = 1; i<toSort.length; i++){
            shiftArrayUp(toSort, i, func);
        }

        for (int i = toSort.length - 1; i> 0; i--){
            deleteArrayTop(toSort, i, func);
        }

        time = System.currentTimeMillis() - time;
        //System.err.println("Algorithm time: " + time  + " ms");
        //System.err.println("CompareCounter = " + getCompareCounter());
        //System.err.println("SwapCounter = " + getSwapCounter());
    }

    private void shiftArrayUp(Comparable[] heap, int index, BiFunction<Comparable, Comparable, Integer> func){
        this.compareCounter++;
        //System.err.println("compare in shiftUp " + heap[index] + " and " + heap[index/2]);
        if (func.apply(heap[index], heap[index / 2]) < 0) {
            this.swapCounter+=2;
            //System.err.println("swap indexes in shiftUp " + index + " and " + index/2);
            Comparable tmp = heap[index];
            heap[index] = heap[index/2];
            heap[index/2] = tmp;

            shiftArrayUp(heap, index / 2, func);
        }
    }

    private void shiftArrayDown(Comparable[] heap, int index, int endHeap, BiFunction<Comparable, Comparable, Integer> func){
        if (index * 2 > endHeap) {
            return;
        }
        int compareIndex;
        if (index * 2 + 1 > endHeap) {
            compareIndex = index * 2;
        } else {
            compareIndex = matchElement(heap[index * 2], heap[index * 2 + 1],
                    index * 2, index * 2 + 1, func);
        }
        this.compareCounter++;
        //System.err.println("compare in shiftDown " + heap[index] + " and " + heap[compareIndex]);
        if (func.apply(heap[index], heap[compareIndex]) > 0) {
            this.swapCounter+=2;
            //System.err.println("swap indexes " + index +" and " + compareIndex);
            Comparable tmp = heap[index];
            heap[index] = heap[compareIndex];
            heap[compareIndex] = tmp;

            shiftArrayDown(heap, compareIndex, endHeap, func);
        }
    }

    private void deleteArrayTop(Comparable[] heap, int endHeap, BiFunction<Comparable, Comparable, Integer> func) {
        this.swapCounter+=2;
        //System.err.println("swap indexes in deleteTop 0 and " + endHeap);
        Comparable tmp = heap[0];
        heap[0] = heap[endHeap];
        heap[endHeap] = tmp;

        shiftArrayDown(heap, 0, endHeap - 1, func);
    }

    private int matchElement(Comparable a, Comparable b, int indexA, int indexB, BiFunction<Comparable, Comparable, Integer> func) {
        this.compareCounter++;
        //System.err.println("compare in matchElement " + a + " and " + b);
        if (func.apply(a, b) > 0) return indexB;
        return indexA;
    }

    @Override
    public int getSwapCounter() {
        return swapCounter;
    }

    @Override
    public void resetCounters() {
        this.swapCounter = 0;
        this.compareCounter = 0;
    }

    @Override
    public int getCompareCounter() {
        return compareCounter;
    }

    @Override
    public long getTime() {
        return time;
    }

}
