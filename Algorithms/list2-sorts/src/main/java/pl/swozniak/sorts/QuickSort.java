package pl.swozniak.sorts;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class QuickSort implements Sort {

    private int swapCounter = 0;
    private int compareCounter = 0;
    private long time;

    @Override
    public void sortList(List<Comparable> toSort, BiFunction<Comparable, Comparable, Integer> func) {
        time = System.currentTimeMillis();
        sortPartOfList(toSort, 0, toSort.size() - 1, func);
        time = System.currentTimeMillis() - time;
        System.err.println("Algorithm time: " + time  + " ms");
        System.err.println("CompareCounter = " + getCompareCounter());
        System.err.println("SwapCounter = " + getSwapCounter());
    }

    private void sortPartOfList(List<Comparable> part, int start, int end, BiFunction<Comparable, Comparable, Integer> func) {
        if (part.size() <= 1) return;
        if (start >= end) return;
        //Comparable value = part.get(start);

        int point = quickLoopList(part, start, end, start, func);


        sortPartOfList(part, start, point - 1, func);
        sortPartOfList(part, point + 1, end, func);
    }


    public int quickLoopList(List<Comparable> part, int start, int end, int indexValue, BiFunction<Comparable, Comparable, Integer> func){
        int left = start + 1, right = end;

        Comparable value = part.get(indexValue);
        Collections.swap(part, start, indexValue);

        while (left <= right) {

            this.compareCounter++;
            System.err.println("compare in quickLoop " + value + " and " + part.get(left));
            while (left <= end && func.apply(value, part.get(left)) < 0) {
                this.compareCounter++;
                System.err.println("compare in quickLoop " + value + " and " + part.get(left));
                left++;

            }
            this.compareCounter++;
            System.err.println("compare in quickLoop " + value + " and " + part.get(right));
            while (right > start && func.apply(value, part.get(right)) >= 0) {
                this.compareCounter++;
                System.err.println("compare in quickLoop " + value + " and " + part.get(right));
                right--;

            }
            if (left < right) {
                this.swapCounter+=2;
                System.err.println("swap indexes in quickLoop " + left +" and " + right);
                Collections.swap(part, left, right);
            }
        }
        left--; // because left was equal right + 1 and now left == right
        this.swapCounter+=2;
        System.err.println("swap indexes in quickLoop " + left + " and " + start);
        Collections.swap(part, left, start);

        return left;
    }


    @Override
    public void sortArray(Comparable[] toSort, BiFunction<Comparable, Comparable, Integer> func) {
        time = System.currentTimeMillis();

        sortPartOfArray(toSort, 0, toSort.length - 1, func);

        time = System.currentTimeMillis() - time;
        System.err.println("Algorithm time: " + time  + " ms");
        System.err.println("CompareCounter = " + getCompareCounter());
        System.err.println("SwapCounter = " + getSwapCounter());
    }

    private void sortPartOfArray(Comparable[] part, int start, int end, BiFunction<Comparable, Comparable, Integer> func) {
        if (part.length <= 1) return;
        if (start >= end) return;

        int point = quickLoopArray(part, start, end, start, func);

        sortPartOfArray(part, start, point-1, func);
        sortPartOfArray(part, point + 1, end, func);
    }


    public int quickLoopArray(Comparable[] part, int start, int end, int indexValue, BiFunction<Comparable, Comparable, Integer> func){
        int left = start+1, right = end;
        Comparable value = part[indexValue];
        Comparable tmp;
        this.swapCounter += 2;
        tmp = part[start];
        part[start] = part[indexValue];
        part[indexValue] = tmp;

        while (left <= right) {
            this.compareCounter++;
            System.err.println("compare in quickLoop " + value + " and " + part[left]);
            while (left <= end && func.apply(value, part[left]) <= 0) {
                this.compareCounter++;
                System.err.println("compare in quickLoop " + value + " and " + part[left]);
                left++;

            }
            this.compareCounter++;
            System.err.println("compare in quickLoop " + value + " and " + part[right]);
            while (right > start && func.apply(value, part[right]) > 0){
                this.compareCounter++;
                System.err.println("compare in quickLoop " + value + " and " + part[right]);
                right--;

            }
            if(left < right){
                this.swapCounter+=2;
                System.err.println("swap indexes in quickLoop " + left +" and " + right);
                tmp = part[left];
                part[left] = part[right];
                part[right] = tmp;
            }
        }
        left--; // --> left == right
        this.swapCounter+=2;
        System.err.println("swap indexes in quickLoop " + left + " and " + indexValue);
        tmp = part[start];
        part[start] = part[left];
        part[left] = tmp;

        return left;
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
