package pl.swozniak.trees;

import pl.swozniak.trees.basetree.AbstractBinaryTree;
import pl.swozniak.trees.basetree.Node;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

import static pl.swozniak.trees.basetree.Node.nullNode;

public class BST extends AbstractBinaryTree {

    public BST(Comparator<String> comparator) {
        super(comparator);
    }

    @Override
    public void insertValue(String s) {
        Node add = new Node(s);
        size++;
        if (this.root == nullNode) {
            this.root = add;
            return;
        }
        Node prev;
        Node curr = this.root;
        while (curr != nullNode) {
            prev = curr;
            if (comparator.compare(s, curr.getValue()) < 0) {
                curr = curr.getLeft();
                if (curr == nullNode) {
                    prev.setLeft(add);
                }
            } else {
                curr = curr.getRight();
                if (curr == nullNode) {
                    prev.setRight(add);
                }
            }
        }
    }

    @Override
    public void delete(String s) {
        Node deleted = getNodeByValue(s);
        if (deleted == nullNode) return;
        Node curr;
        if (deleted.getLeft() == nullNode && deleted.getRight() == nullNode) {
            curr = nullNode;
        }else if (deleted.getLeft() == nullNode) {
            curr = deleted.getRight();
        }else if (deleted.getRight() == nullNode) {
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
        if (parent == nullNode) {
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
        if (curr == nullNode) {
            System.out.println(s + " -> 0");
            return false;
        } else {
            System.out.println(s + " -> 1");
            return true;
        }
    }

    private Node getNodeByValue(String s) {
        if (s == null) return nullNode;
        Node curr = this.root;
        String currValue;
        while (curr != nullNode) {
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
        return nullNode;
    }

    @Override
    public void inOrder() {
        Deque<Node> stack = new ArrayDeque<>();
        Node curr = this.root;
        while (!stack.isEmpty() || curr != nullNode) {
            if (curr != nullNode) {
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
        if (n == nullNode) return;
        System.out.println(" in");
        recursiveInOrder(n.getLeft());
        System.out.print(n.getValue() + " ");
        recursiveInOrder(n.getRight());
    }

    private Node findSuccessor(Node n) {
        if(n == nullNode) return nullNode;
        if (n.getRight() != nullNode) {
            return findMin(n.getRight());
        }
        Node parent = n.getParent();
        Node curr = n;
        while (parent != nullNode && parent.getLeft() != curr) {
            curr = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    private Node findMin(Node n) {
        Node curr = n;
        while (curr.getLeft() != nullNode) {
            curr = curr.getLeft();
        }
        return curr;
    }
}
