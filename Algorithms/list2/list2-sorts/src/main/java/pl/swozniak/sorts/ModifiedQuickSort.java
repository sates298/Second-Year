package pl.swozniak.sorts;


import java.util.List;
import java.util.function.BiFunction;

public class ModifiedQuickSort implements Sort {

    private InsertionSort insert;
    private QuickSort quick;
    private int swapCounter = 0;
    private int compareCounter = 0;
    private long time;

    public ModifiedQuickSort(){
        insert = new InsertionSort();
        quick = new QuickSort();
    }

    @Override
    public void sortList(List<Comparable> toSort, BiFunction<Comparable, Comparable, Integer> func) {
        time = System.currentTimeMillis();

        sortPartOfListModified(toSort, 0, toSort.size() - 1, func);

        time = System.currentTimeMillis() - time;
        System.err.println("Algorithm time: " + time  + " ms");
        System.err.println("CompareCounter = " + getCompareCounter());
        System.err.println("SwapCounter = " + getSwapCounter());
    }

    private void sortPartOfListModified(List<Comparable> part, int start, int end, BiFunction<Comparable, Comparable, Integer> func){
        if(end - start < 16){
            this.insert.sortPartOfListInsert(part,start, end, func);
            this.swapCounter += this.insert.getSwapCounter();
            this.compareCounter += this.insert.getCompareCounter();
            this.insert.resetCounters();
            return;
        }
        if(start >= end) return;
        int valueInd = findMedian(part.get(start),start, part.get((end - start)/2),
                (end-start)/2, part.get(end), end);

        int point = this.quick.quickLoopList(part, start,end,valueInd,func);
        this.swapCounter += this.quick.getSwapCounter();
        this.compareCounter += this.quick.getCompareCounter();
        this.quick.resetCounters();

        sortPartOfListModified(part, start, point -1, func);
        sortPartOfListModified(part, point + 1, end,func);
    }

    @Override
    public void sortArray(Comparable[] toSort, BiFunction<Comparable, Comparable, Integer> func) {
        //System.err.println("mquick");
        time = System.currentTimeMillis();

        sortPartOfArrayModified(toSort,0, toSort.length - 1, func);

        time = System.currentTimeMillis() - time;
        //System.err.println("Algorithm time: " + time  + " ms");
        //System.err.println("CompareCounter = " + getCompareCounter());
        //System.err.println("SwapCounter = " + getSwapCounter());
    }

    private void sortPartOfArrayModified(Comparable[] part, int start, int end, BiFunction<Comparable, Comparable, Integer> func){
        if(end - start < 16){
            this.insert.sortPartOfArrayInsert(part,start,end, func);
            this.swapCounter += this.insert.getSwapCounter();
            this.compareCounter += this.insert.getCompareCounter();
            this.insert.resetCounters();
            return;
        }
        if(start >= end) return;
       int value = findMedian(part[start],start, part[(end - start)/2],(end - start)/2, part[end], end);

        int point = this.quick.quickLoopArray(part, start, end, value, func);
        this.swapCounter += this.quick.getSwapCounter();
        this.compareCounter += this.quick.getCompareCounter();
        this.quick.resetCounters();

        sortPartOfArrayModified(part, start, point - 1, func);
        sortPartOfArrayModified(part, point + 1, end, func);
    }

    private int findMedian(Comparable a,int indexA, Comparable b,int indexB, Comparable c, int indexC){
        //System.err.println("comparisons to find median from " +a+", " + b + " and " + c);
        this.compareCounter++;
        if(a.compareTo(b) <= 0) {
            this.compareCounter++;
            if (c.compareTo(a) <= 0) return indexA;
            this.compareCounter++;
            if (c.compareTo(b) <= 0) return indexC;
            return indexB;
        }
        this.compareCounter++;
        if (b.compareTo(c) <= 0) {
            this.compareCounter++;
            if (c.compareTo(a) <= 0) return indexC;
            return indexA;
        }
        return indexB;
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
