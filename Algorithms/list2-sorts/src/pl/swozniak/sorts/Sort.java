package pl.swozniak.sorts;


import java.util.List;
import java.util.function.BiFunction;

public interface Sort {
    void sortList(List<Comparable> toSort, BiFunction<Comparable, Comparable, Integer> func);
    void sortArray(Comparable[] toSort, BiFunction<Comparable, Comparable, Integer> func);
    int getSwapCounter();
    void resetCounters();
    int getCompareCounter();
    long getTime();
}
