package pl.swozniak;

import pl.swozniak.trees.BST;
import pl.swozniak.trees.Splay;
import pl.swozniak.trees.basetree.AbstractBinaryTree;


public class Main {

    public static void main(String[] args) {
        AbstractBinaryTree splayTree = new Splay(new StringComparator());
        AbstractBinaryTree bstTree = new BST(new StringComparator());
        long time = System.currentTimeMillis();
        //splayTree.load(CommandLineInterface.KJB_PATH);
        //splayTree.inOrder();
        //System.out.println(tree.getSize());
        /*tree.delete("right");
        tree.delete("right");
        tree.delete("three");
        tree.delete("First");
        tree.delete("Five");*/
        //tree.inOrder();
        //System.out.println(splayTree.getSize());
        //System.out.println("time splay = " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        bstTree.load(CommandLineInterface.KJB_PATH);
        bstTree.inOrder();
        //System.out.println(tree.getSize());
        /*tree.delete("right");
        tree.delete("right");
        tree.delete("three");
        tree.delete("First");
        tree.delete("Five");*/
        //tree.inOrder();
        System.out.println(bstTree.getSize());
        System.out.println("time bst = " + (System.currentTimeMillis() - time));

    }
}
