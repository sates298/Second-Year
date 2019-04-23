package pl.swozniak;

import pl.swozniak.trees.BST;
import pl.swozniak.trees.RBT;
import pl.swozniak.trees.Splay;
import pl.swozniak.trees.basetree.AbstractBinaryTree;


public class Main {

    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        cli.run(args);
//        String path = CommandLineInterface.ASPELL_WORDLIST_PATH;
//        AbstractBinaryTree splay = new Splay(new StringComparator<>());
//        AbstractBinaryTree rbt = new RBT(new StringComparator<>());
//        AbstractBinaryTree bst = new BST(new StringComparator<>());
//        long time = System.currentTimeMillis();
//        splay.load(path);
//        System.out.println("splays: " + ((Splay) splay).getSplays());
//        System.out.println("size: " + splay.getSize());
//        System.out.println("comparisons: " + splay.getComparisons());
//        System.out.println("swaps: " + splay.getSwapNodes());
//        System.out.println("time tree = " + (System.currentTimeMillis() - time));
//        time = System.currentTimeMillis();
//        rbt.load(path);
//        System.out.println("size: " + rbt.getSize());
//        System.out.println("comparisons: " + rbt.getComparisons());
//        System.out.println("swaps: " + rbt.getSwapNodes());
//        System.out.println("time tree = " + (System.currentTimeMillis() - time));
//
//        time = System.currentTimeMillis();
//        bst.load(path);
//        System.out.println("size: " + bst.getSize());
//        System.out.println("comparisons: " + bst.getComparisons());
//        System.out.println("swaps: " + bst.getSwapNodes());
//        System.out.println("time tree = " + (System.currentTimeMillis() - time));

    }
}
