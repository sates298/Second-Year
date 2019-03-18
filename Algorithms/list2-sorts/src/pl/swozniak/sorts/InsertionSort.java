package pl.swozniak.sorts;

import java.util.List;
import java.util.function.BiFunction;

public class InsertionSort implements Sort {

    private int swapCounter = 0;
    private int compareCounter = 0;
    private long time;

    @Override
    public void sortList(List<Comparable> toSort, BiFunction<Comparable, Comparable, Integer> func) {
        time = System.currentTimeMillis();

        sortPartOfListInsert(toSort, 0, toSort.size() - 1, func);

        time = System.currentTimeMillis() - time;
        System.err.println("Algorithm time: " + time  + " ms");
        System.err.println("CompareCounter = " + getCompareCounter());
        System.err.println("SwapCounter = " + getSwapCounter());
    }

    public void sortPartOfListInsert(List<Comparable> part,int start, int end, BiFunction<Comparable, Comparable, Integer> func){
        int finalIndex;
        Comparable curr;
        for(int i=start + 1 ; i<=end; i++){
            curr = part.get(i);
            finalIndex = i;
            part.remove(i);
            this.compareCounter++;
            System.err.println("compare in sortPart " + curr + " and " + part.get(finalIndex - 1));
            while(finalIndex > start && func.apply(curr, part.get(finalIndex-1)) > 0){
                this.compareCounter++;
                System.err.println("compare in sortPart " + curr + " and " + part.get(finalIndex - 1));
                finalIndex--;
            }
            this.swapCounter++;
            System.err.println("set current element in index " + finalIndex);
            part.add(finalIndex, curr);
        }
    }


    @Override
    public void sortArray(Comparable[] toSort, BiFunction<Comparable, Comparable, Integer> func) {
        time = System.currentTimeMillis();

        sortPartOfArrayInsert(toSort, 0, toSort.length - 1, func);

        time = System.currentTimeMillis() - time;
        System.err.println("Algorithm time: " + time  + " ms");
        System.err.println("CompareCounter = " + getCompareCounter());
        System.err.println("SwapCounter = " + getSwapCounter());
    }

    public void sortPartOfArrayInsert(Comparable[] part, int start, int end, BiFunction<Comparable, Comparable, Integer> func){
        int finalIndex;
        Comparable curr;
        for(int i=start + 1; i<=end; i++){
            curr = part[i];
            finalIndex = i;
            this.compareCounter++;
            System.err.println("compare in sortPart " + curr + " and " + part[finalIndex - 1]);
            while(finalIndex > start  && func.apply(curr, part[finalIndex-1]) > 0){
                this.compareCounter++;
                this.swapCounter++;
                System.err.println("shift elements in sortPartOfArray");
                part[finalIndex] = part[finalIndex - 1];
                System.err.println("compare in sortPart " + curr + " and " + part[finalIndex - 1]);
                finalIndex--;
            }
            this.swapCounter++;
            System.err.println("set current element in index " + finalIndex);
            part[finalIndex] = curr;
        }
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
