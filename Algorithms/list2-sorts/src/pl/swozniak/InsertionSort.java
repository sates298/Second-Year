package pl.swozniak;


import java.util.ArrayList;
import java.util.List;

public class InsertionSort implements Sort {
    @Override
    public List<Integer> sort(List<Integer> toSort, boolean isAsc) {
        List<Integer> sorted = new ArrayList<>();
        int curr, finalIndex;
        if(isAsc) {
            for (Integer i : toSort) {
                curr = i;
                finalIndex = 0;
                while (finalIndex < sorted.size() && curr >= sorted.get(finalIndex)) {
                    finalIndex++;
                }
                sorted.add(finalIndex, curr);
            }
        }else {

        }

        return sorted;
    }
}
