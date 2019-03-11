package pl.swozniak;

import java.util.*;

public class Main {

    private static Sort sortAlgorithm;
    private static List<Integer> list = new ArrayList<>();

    private static void sort(boolean isAsc) {
        list = sortAlgorithm.sort(list, isAsc);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = 0, k = 0;
        String sortType = "", fileName = "";
        String[] parameters;
        boolean isAsc = true, stat = false;

        try {
            for (int i = 0; i < args.length; i++) {
                if ("--type".equals(args[i])) {
                    sortType = args[i + 1];
                    i++;
                } else if ("--desc".equals(args[i])) {
                    isAsc = false;
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
            //opening file and prepare to write
        }


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
        System.out.println("Before sort: \n" + list);
        sort(isAsc);
        System.out.println("After sort: \n" + list);

    }
}
