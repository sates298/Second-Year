package pl.swozniak.sorts;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class QuickSort implements Sort {

    @Override
    public void sortList(List<Comparable> toSort, BiFunction<Comparable, Comparable, Integer> func) {
        sortPartOfList(toSort, 0, toSort.size() - 1, func);
    }

    @Override
    public void sortArray(Comparable[] toSort, BiFunction<Comparable, Comparable, Integer> func) {
        sortPartOfArray(toSort, 0, toSort.length - 1, func);
    }

    private void sortPartOfList(List<Comparable> part, int start, int end, BiFunction<Comparable, Comparable, Integer> func) {
        if (part.size() <= 1) return;
        if (start >= end) return;
        Comparable value = part.get(start);

       int point = quickLoopList(part, start, end, value, func);

        sortPartOfList(part, start, point - 1, func);
        sortPartOfList(part, point + 1, end, func);
    }

    private void sortPartOfArray(Comparable[] part, int start, int end, BiFunction<Comparable, Comparable, Integer> func) {
        if (part.length <= 1) return;
        if (start >= end) return;
        Comparable value = part[start];

        int point = quickLoopArray(part, start, end, value, func);

        sortPartOfArray(part, start, point-1, func);
        sortPartOfArray(part, point + 1, end, func);
    }


    public int quickLoopList(List<Comparable> part, int start, int end, Comparable value, BiFunction<Comparable, Comparable, Integer> func){
        int left = start + 1, right = end;

        while (left <= right) {
            while (left <= end && func.apply(value, part.get(left)) < 0) {
                left++;
            }
            while (right > start && func.apply(value, part.get(right)) >= 0) {
                right--;
            }
            if (left < right) {
                Collections.swap(part, left, right);
            }
        }
        left--; // because left was equal right + 1 and now left == right
        Collections.swap(part, start, left);

        return left;
    }

    public int quickLoopArray(Comparable[] part, int start, int end, Comparable value, BiFunction<Comparable, Comparable, Integer> func){
        int left = start + 1, right = end;
        Comparable tmp;
        while (left <= right) {
            while (left <= end && func.apply(value, part[left]) < 0) {
                left++;
            }
            while (right > start && func.apply(value, part[right]) >= 0){
                right--;
            }
            if(left < right){
                tmp = part[left];
                part[left] = part[right];
                part[right] = tmp;
            }
        }
        left--; // --> left == right
        tmp = part[start];
        part[start] = part[left];
        part[left] = tmp;

        return left;
    }


}
