package pl.swozniak;

import pl.swozniak.trees.BST;
import pl.swozniak.trees.RBT;
import pl.swozniak.trees.Splay;
import pl.swozniak.trees.basetree.AbstractBinaryTree;


public class Main {

    public static void main(String[] args) {
        AbstractBinaryTree tree = new Splay(new StringComparator<>());
        //AbstractBinaryTree tree = new RBT(new StringComparator<>());
        //AbstractBinaryTree tree = new BST(new StringComparator<>());
        long time = System.currentTimeMillis();
        tree.load(CommandLineInterface.SAMPLE_PATH);
        tree.inOrder();
        System.out.println(tree.getSize());
        tree.delete("right");
        tree.delete("right");
        tree.delete("three");
        tree.delete("First");
        tree.delete("Five");
        tree.inOrder();
        System.out.println(tree.getSize());
        System.out.println("time tree = " + (System.currentTimeMillis() - time));


    }
}
