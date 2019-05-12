package pl.swozniak.studytasks;

import pl.swozniak.StringComparator;
import pl.swozniak.trees.BST;
import pl.swozniak.trees.RBT;
import pl.swozniak.trees.Splay;
import pl.swozniak.trees.basetree.AbstractBinaryTree;


import java.util.Scanner;

public class FirstTask {

    public static final String SAMPLE_PATH = "out/production/list4/sample.txt";
    public static final String ASPELL_WORDLIST_PATH = "out/production/list4/aspell_wordlist.txt";
    public static final String KJB_PATH = "out/production/list4/KJB.txt";
    public static final String LOTR_PATH = "out/production/list4/lotr.txt";
    public static final String BEST_BST_PATH = "out/production/list4/best_BST.txt";

    private Scanner sc;

    public FirstTask() {
        this.sc = new Scanner(System.in);
    }

    public void run(String[] args) {
        if (args.length < 2) {
            System.err.println("Too few arguments");
            return;
        }
        if (!args[0].equals("--type")) {
            System.err.println("wrong argument");
            return;
        }
        AbstractBinaryTree tree;
        switch (args[1]) {
            case "bst":
                tree = new BST(new StringComparator<>());
                break;
            case "rbt":
                tree = new RBT(new StringComparator<>());
                break;
            case "splay":
                tree = new Splay(new StringComparator<>());
                break;
            default:
                System.err.println("Wrong type of tree");
                return;
        }
        int numberOfOperations;
        long oneTime, fullTime = 0;
        while (true) {
            try {
                System.out.println("Enter number of operations: ");
                numberOfOperations = Integer.parseInt(sc.nextLine());
                for (int i = 0; i < numberOfOperations; i++) {
                    try {
                        System.out.println((i + 1) + ".Enter operation: ");
                        String[] op = sc.nextLine().split(" ");

                        switch (op[0]) {
                            case "insert":
                                oneTime = System.currentTimeMillis();
                                tree.insert(op[1]);
                                oneTime = System.currentTimeMillis() - oneTime;
                                System.out.println("Time of insert: "+ oneTime);
                                fullTime += oneTime;
                                break;
                            case "delete":
                                oneTime = System.currentTimeMillis();
                                tree.delete(op[1]);
                                oneTime = System.currentTimeMillis() - oneTime;
                                System.out.println("Time of delete: "+ oneTime);
                                fullTime += oneTime;
                                break;
                            case "search":
                                oneTime = System.currentTimeMillis();
                                tree.search(op[1]);
                                oneTime = System.currentTimeMillis() - oneTime;
                                System.out.println("Time of insert: "+ oneTime);
                                fullTime += oneTime;
                                break;
                            case "load":
                                oneTime = System.currentTimeMillis();
                                String file = op[1];
                                switch (file) {
                                    case "sample.txt":
                                        tree.load(SAMPLE_PATH);
                                        break;
                                    case "KJB.txt":
                                        tree.load(KJB_PATH);
                                        break;
                                    case "lotr.txt":
                                        tree.load(LOTR_PATH);
                                        break;
                                    case "aspell_wordlist.txt":
                                        tree.load(ASPELL_WORDLIST_PATH);
                                        break;
                                    default:
                                        tree.load(file);
                                }
                                oneTime = System.currentTimeMillis() - oneTime;
                                System.out.println("Time of load: "+ oneTime);
                                fullTime += oneTime;
                                break;
                            case "inorder":
                                oneTime = System.currentTimeMillis();
                                tree.inOrder();
                                oneTime = System.currentTimeMillis() - oneTime;
                                System.out.println("Time of inOrder: "+ oneTime);
                                fullTime += oneTime;
                                break;
                            default:
                                System.err.println("Wrong operation");
                                i--;
                        }


                    } catch (NumberFormatException n) {
                        System.err.println("Its not a number");
                        i--;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Too few arguments");
                        i--;
                    }
                }
                System.out.println("Time of algorithms: " + fullTime);
                System.out.println("Max Capacity: " + tree.getMaxCapacity());
                System.out.println("Final size: " + tree.getSize());
                System.out.println("Comparisons: " + tree.getComparisons());
                System.out.println("Nodes modification: " + tree.getSwapNodes());
                break;
            } catch (NumberFormatException n) {
                System.err.println("Its not a number");
            }
        }


    }
}
