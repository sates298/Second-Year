package pl.swozniak.sorts;

import java.util.List;
import java.util.function.BiFunction;

public class InsertionSort implements Sort {

    @Override
    public void sortList(List<Comparable> toSort, BiFunction<Comparable, Comparable, Integer> func) {
        int finalIndex;
        Comparable curr;
        for(int i=1; i<toSort.size(); i++){
            curr = toSort.get(i);
            finalIndex = i;
            while(finalIndex > 0 && func.apply(curr, toSort.get(finalIndex-1)) > 0){
                finalIndex--;
            }
            toSort.remove(i);
            toSort.add(finalIndex, curr);
        }
    }


    @Override
    public void sortArray(Comparable[] toSort, BiFunction<Comparable, Comparable, Integer> func) {
        int finalIndex;
        Comparable curr;
        for(int i=1; i<toSort.length; i++){
            curr = toSort[i];
            finalIndex = i;
            while(finalIndex > 0  && func.apply(curr, toSort[finalIndex-1]) > 0){
                toSort[finalIndex] = toSort[finalIndex - 1];
                finalIndex--;
            }
            toSort[finalIndex] = curr;
        }
    }
}
