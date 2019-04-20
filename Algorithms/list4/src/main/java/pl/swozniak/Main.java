package pl.swozniak;

import pl.swozniak.trees.BST;
import pl.swozniak.trees.basetree.AbstractBinaryTree;


public class Main {

    public static void main(String[] args) {
        AbstractBinaryTree tree = new BST(new StringComparator());

        tree.load(CommandLineInterface.LOTR_PATH);

        //tree.search("Amon");
        tree.inOrder();
        /*System.out.println(tree.getSize());
        tree.delete("right");
        tree.delete("right");
        tree.delete("three");
        tree.delete("First");
        tree.delete("Five");
        tree.inOrder();*/
        System.out.println(tree.getSize());

    }
}
