package pl.swozniak.sorts;


import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class SelectSort implements Sort {

    private int swapCounter = 0;
    private int compareCounter = 0;

    @Override
    public void sortList(List<Comparable> toSort, BiFunction<Comparable, Comparable, Integer> func) {
        int index;
        for(int i=0; i < toSort.size(); i++){
            index = i;
            for(int j=i; j < toSort.size(); j++){
                this.compareCounter++;
                System.err.println("compare in sort " + toSort.get(index) + " and " + toSort.get(j));
                if(func.apply(toSort.get(index), toSort.get(j)) < 0){
                    index = j;
                }
            }

            this.swapCounter++;
            System.err.println("swap indexes in sort " + index + " and " + i);
            Collections.swap(toSort, index, i);
        }
    }

    @Override
    public void sortArray(Comparable[] toSort, BiFunction<Comparable, Comparable, Integer> func) {
        int index;
        Comparable tmp;
        for(int i=0; i < toSort.length; i++){
            index = i;
            for(int j=i; j < toSort.length; j++){
                this.compareCounter++;
                System.err.println("compare in sort " + toSort[index] + " and " + toSort[j]);
                if(func.apply(toSort[index], toSort[j]) < 0){
                    index = j;
                }
            }
            this.swapCounter++;
            System.err.println("swap indexes in sort " + index + " and " + i);
            tmp = toSort[index];
            toSort[index] = toSort[i];
            toSort[i] = tmp;
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
