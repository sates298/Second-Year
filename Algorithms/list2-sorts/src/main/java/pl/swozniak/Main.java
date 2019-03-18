package pl.swozniak;

import pl.swozniak.sorts.*;
import pl.swozniak.statistics.Statistics;

import java.util.*;
import java.util.function.BiFunction;


public class Main {

    private static Sort sortAlgorithm;
    private static BiFunction<Comparable, Comparable, Integer> comparator = Comparators::descending;
    private static List<Comparable> list = new ArrayList<>();
    private static Comparable[] array;

    private static void sortList() {
        sortAlgorithm.sortList(list, comparator);
    }
    private static void sortArray(){
        sortAlgorithm.sortArray(array, comparator);
    }

    private static boolean checkSortedList(){
        for(int i=0; i<list.size()-1; i++){
            if(comparator.apply(list.get(i), list.get(i+1)) < 0){
                return false;
            }
        }
        return true;
    }

    private static boolean checkSortedArray(){
        for(int i=0; i<array.length-1; i++){
            if(comparator.apply(array[i], array[i+1]) < 0){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        int n, k = 0;
        String sortType = "", fileName = "";
        String[] parameters;
        boolean stat = false;

        try {
            for (int i = 0; i < args.length; i++) {
                if ("--type".equals(args[i])) {
                    sortType = args[i + 1];
                    i++;
                } else if ("--asc".equals(args[i])) {
                    comparator = Comparators::ascending;
                } else if ("--stat".equals(args[i])) {
                    fileName = args[i + 1];
                    k = Integer.parseInt(args[i + 2]);
                    stat = true;
                    i += 2;
                }
            }
        }catch(ArrayIndexOutOfBoundsException | NumberFormatException e){
            e.printStackTrace();
            return;
        }

        switch (sortType) {
            case "select":
                System.out.println("SELECT SORT");
                sortAlgorithm = new SelectSort();
                break;
            case "insert":
                System.out.println("INSERTION SORT");
                sortAlgorithm = new InsertionSort();
                break;
            case "heap":
                System.out.println("HEAP SORT");
                sortAlgorithm = new HeapSort();
                break;
            case "quick":
                System.out.println("QUICK SORT");
                sortAlgorithm = new QuickSort();
                break;
            case "mquick":
                System.out.println("MODIFIED QUICK SORT");
                sortAlgorithm = new ModifiedQuickSort();
                break;
            default:
                System.out.println("Wrong type of sort!");
                return;
        }

        if(stat){
            Statistics stats = new Statistics();
            stats.run(k, fileName, comparator);
        }else {
            try {
                System.out.println("Enter amount of elements to sort: ");
                n = Integer.parseInt(sc.nextLine());
                System.out.println("Enter elements to sort: ");
                String temp = sc.nextLine();
                parameters = temp.split(" ");
                for (int i = 0; i < n && i < parameters.length; i++) {
                    list.add(Integer.parseInt(parameters[i]));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }


            array = new Integer[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }


            System.out.println("Before sort: \n" + list);

            Thread.sleep(1000);
            sortList();
            Thread.sleep(1000);

            if (checkSortedList()) {
                System.out.println("SORTED!");
            } else {
                System.out.println("NOT SORTED!");
            }
            System.out.println("Amount of sorted elements: " + list.size());
            System.out.println("After list sort: \n" + list);


            Thread.sleep(1000);
            sortAlgorithm.resetCounters();
            sortArray();
            Thread.sleep(1000);

            if (checkSortedArray()) {
                System.out.println("SORTED!");
            } else {
                System.out.println("NOT SORTED!");
            }
            System.out.println("Amount of sorted elements: " + array.length);
            System.out.println("After array sort: \n" + Arrays.toString(array));
        }

    }
}
