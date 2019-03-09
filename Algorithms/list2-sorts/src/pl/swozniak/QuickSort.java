package pl.swozniak;


import java.util.Collections;
import java.util.List;

public class QuickSort implements Sort {

    @Override
    public List<Integer> sort(List<Integer> toSort, boolean isAsc) {
        sort(toSort,0, toSort.size()-1, isAsc);
        return toSort;

    }

    private void sort(List<Integer> part, int start, int end, boolean isAsc){
        if(part.size() <= 1) return;
        if(start >= end) return;
        int point;

        if(isAsc) point = divideListAsc(part, start, end);
        else point = divideListDesc(part, start, end);

        sort(part, start, point - 1, isAsc);
        sort(part, point + 1, end, isAsc);

    }

    private int divideListAsc(List<Integer> list, int start, int end){
        int value = list.get(start);
        int left = start + 1;
        int right = end;

        while(left <= right){
            while(left <= end && value > list.get(left)){
                left++;
            }
            while (right > start && value <= list.get(right)){
                right--;
            }
            if(left < right){
                Collections.swap(list, left, right);
            }
        }
        left--; // because left == right + 1, but now left == right
        Collections.swap(list, start, left); //left == point == right
        return left;
    }

    private int divideListDesc(List<Integer> list, int start, int end){
        int value = list.get(start);
        int left = start + 1;
        int right = end;

        while(left <= right){
            while(left <= end && value < list.get(left)){
                left++;
            }
            while (right > start && value >= list.get(right)){
                right--;
            }
            if(left < right){
                Collections.swap(list, left, right);
            }
        }
        left--; // because left == right + 1, but now left == right
        Collections.swap(list, start, left); //left == point == right
        return left;
    }

}
