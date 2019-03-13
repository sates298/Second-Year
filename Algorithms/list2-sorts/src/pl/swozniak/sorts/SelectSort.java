package pl.swozniak.sorts;


import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class SelectSort implements Sort {

    @Override
    public void sortList(List<Comparable> toSort, BiFunction<Comparable, Comparable, Integer> func) {
        int index;
        for(int i=0; i < toSort.size(); i++){
            index = i;
            for(int j=i; j < toSort.size(); j++){
                if(func.apply(toSort.get(index), toSort.get(j)) < 0){
                    index = j;
                }
            }
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
                if(func.apply(toSort[index], toSort[j]) < 0){
                    index = j;
                }
            }
            tmp = toSort[index];
            toSort[index] = toSort[i];
            toSort[i] = tmp;
        }
    }

}
