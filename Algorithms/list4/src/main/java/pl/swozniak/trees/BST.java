package pl.swozniak.trees;

import pl.swozniak.trees.basetree.AbstractBinaryTree;
import pl.swozniak.trees.basetree.Node;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

public class BST extends AbstractBinaryTree {

    public BST(Comparator comparator) {
        super(comparator);
    }

    @Override
    public void insertValue(String s) {
        Node add = new Node(s);
        size++;
        if (this.root == null) {
            this.root = add;
            return;
        }
        Node prev;
        Node curr = this.root;
        while (curr != null) {
            prev = curr;
            if (comparator.compare(s, curr.getValue()) < 0) {
                curr = curr.getLeft();
                if (curr == null) {
                    prev.setLeft(add);
                }
            } else {
                curr = curr.getRight();
                if (curr == null) {
                    prev.setRight(add);
                }
            }
        }
    }

    @Override
    public void delete(String s) {
        Node deleted = getNodeByValue(s);
        if (deleted == null) return;
        Node curr;
        if (deleted.getLeft() == null && deleted.getRight() == null) {
            curr = null;
        }else if (deleted.getLeft() == null) {
            curr = deleted.getRight();
        }else if (deleted.getRight() == null) {
            curr = deleted.getLeft();
        }else{
            curr = findSuccessor(deleted);
            curr.setLeft(deleted.getLeft());
            if(curr.getParent() != deleted){
                curr.getParent().setLeft(curr.getRight());
                curr.setRight(deleted.getRight());
            }
        }

        Node parent = deleted.getParent();
        if (parent == null) {
            this.setRoot(curr);
        }else{
            if(parent.getLeft() == deleted){
                parent.setLeft(curr);
            }else{
                parent.setRight(curr);
            }
        }
        this.size--;
    }

    @Override
    public boolean search(String s) {
        Node curr = getNodeByValue(s);
        if (curr == null) {
            System.out.println(s + " -> 0");
            return false;
        } else {
            System.out.println(s + " -> 1");
            return true;
        }
    }

    protected Node getNodeByValue(String s) {
        if (s == null) return null;
        Node curr = this.root;
        String currValue;
        while (curr != null) {
            currValue = curr.getValue();
            if (comparator.compare(s, currValue) == 0) {
                return curr;
            }
            if (comparator.compare(s, currValue) < 0) {
                curr = curr.getLeft();
            } else {
                curr = curr.getRight();
            }
        }
        return null;
    }

    @Override
    public void inOrder() {
        Deque<Node> stack = new ArrayDeque<>();
        Node curr = this.root;
        while (!stack.isEmpty() || curr != null) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.getLeft();
            } else {
                curr = stack.pop();
                System.out.println(curr.getValue());
                curr = curr.getRight();
            }
        }
        //Recursive version of inOrder function
        /*if (this.root == null){
            System.out.println();
        }else{
            recursiveInOrder(this.root);
        }*/
    }

    // Stack Overflow for file for example aspell_wordlist.txt
    private void recursiveInOrder(Node n) {
        if (n == null) return;
        System.out.println(" in");
        recursiveInOrder(n.getLeft());
        System.out.print(n.getValue() + " ");
        recursiveInOrder(n.getRight());
    }

    protected Node findSuccessor(Node n) {
        if(n == null) return null;
        if (n.getRight() != null) {
            return findMin(n.getRight());
        }
        Node parent = n.getParent();
        Node curr = n;
        while (parent != null && parent.getLeft() != curr) {
            curr = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    protected Node findMin(Node n) {
        if(n == null) return null;
        Node curr = n;
        while (curr.getLeft() != null) {
            curr = curr.getLeft();
        }
        return curr;
    }
}
