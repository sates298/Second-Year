package pl.swozniak;

import java.util.List;

public class ModifiedQuickSort implements Sort {

    @Override
    public List<Integer> sort(List<Integer> toSort, boolean isAsc) {
        if(toSort.size() <= 16){
            return new InsertionSort().sort(toSort, isAsc);
        }
        return null;
    }
}
