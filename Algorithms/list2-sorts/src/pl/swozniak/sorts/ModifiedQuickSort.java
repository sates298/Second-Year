package pl.swozniak.sorts;


import java.util.List;
import java.util.function.BiFunction;

public class ModifiedQuickSort implements Sort {

    private InsertionSort insert;
    private QuickSort quick;

    public ModifiedQuickSort(){
        insert = new InsertionSort();
        quick = new QuickSort();
    }

    @Override
    public void sortList(List<Comparable> toSort, BiFunction<Comparable, Comparable, Integer> func) {
        sortPartOfList(toSort, 0, toSort.size() - 1, func);
    }

    @Override
    public void sortArray(Comparable[] toSort, BiFunction<Comparable, Comparable, Integer> func) {
        sortPartOfArray(toSort,0, toSort.length - 1, func);
    }

    private void sortPartOfList(List<Comparable> part, int start, int end, BiFunction<Comparable, Comparable, Integer> func){
        if(part.size() <= 16){
            this.insert.sortList(part, func);
            return;
        }
        if(start >= end) return;
        Comparable value = findMedian(part.get(start), part.get(part.size()/2), part.get(end));

        int point = this.quick.quickLoopList(part, start,end,value,func);

        sortPartOfList(part, start, point -1, func);
        sortPartOfList(part, point + 1, end,func);
    }

    private void sortPartOfArray(Comparable[] part, int start, int end, BiFunction<Comparable, Comparable, Integer> func){
        if(part.length <= 16){
            this.insert.sortArray(part, func);
            return;
        }
        if(start >= end) return;
        Comparable value = findMedian(part[start], part[part.length/2], part[end]);

        int point = this.quick.quickLoopArray(part, start, end, value, func);

        sortPartOfArray(part, start, point - 1, func);
        sortPartOfArray(part, point + 1, end, func);
    }

    private Comparable findMedian(Comparable a, Comparable b, Comparable c){
        if(a.compareTo(b) <= 0) {
            if (c.compareTo(a) <= 0) return a;
            if (c.compareTo(b) <= 0) return c;
        }
        if (b.compareTo(c) <= 0) {
            if (c.compareTo(a) <= 0) return c;
            return a;
        }
        return b;
    }
}
