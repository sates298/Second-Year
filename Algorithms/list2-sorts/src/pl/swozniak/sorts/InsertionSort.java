package pl.swozniak.sorts;

import java.util.List;
import java.util.function.BiFunction;

public class InsertionSort implements Sort {

    private int swapCounter = 0;
    private int compareCounter = 0;

    @Override
    public void sortList(List<Comparable> toSort, BiFunction<Comparable, Comparable, Integer> func) {
       sortPartOfList(toSort, 0, toSort.size() - 1, func);
    }

    public void sortPartOfList(List<Comparable> part,int start, int end, BiFunction<Comparable, Comparable, Integer> func){
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
                finalIndex--;
                System.err.println("compare in sortPart " + curr + " and " + part.get(finalIndex - 1));
            }
            this.swapCounter++;
            System.err.println("set current element in index " + finalIndex);
            part.add(finalIndex, curr);
        }
    }


    @Override
    public void sortArray(Comparable[] toSort, BiFunction<Comparable, Comparable, Integer> func) {
        sortPartOfArray(toSort, 0, toSort.length - 1, func);
    }

    public void sortPartOfArray(Comparable[] part, int start, int end, BiFunction<Comparable, Comparable, Integer> func){
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
                finalIndex--;
                System.err.println("compare in sortPart " + curr + " and " + part[finalIndex - 1]);
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

}
