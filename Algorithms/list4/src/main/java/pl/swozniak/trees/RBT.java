package pl.swozniak.trees;


import pl.swozniak.trees.basetree.Node;

import java.util.Comparator;

import static pl.swozniak.trees.basetree.Node.nullNode;


public class RBT extends BST {

    public RBT(Comparator<String> comparator) {
        super(comparator);
    }

    @Override
    public void insertValue(String s) {
        Node added = new Node(s, Node.Color.RED);
        super.insertNode(added);
        Node curr = added;
        while (curr != nullNode) {
            curr = checkRBTConditionsAfterInsertion(curr);
        }
    }

    private Node checkRBTConditionsAfterInsertion(Node current) {
        if (current.getParent() == nullNode) setRoot(current);
        if (this.root == current) {
            current.setColor(Node.Color.BLACK);
            return nullNode;
        }
        if (current.getColor() == Node.Color.BLACK || current.getParent().getColor() == Node.Color.BLACK) return nullNode;
        if (isFirstCaseAfterInsertion(current)) {
            Node grand = current.getParent().getParent();
            grand.setColor(Node.Color.RED);
            grand.getLeft().setColor(Node.Color.BLACK);
            grand.getRight().setColor(Node.Color.BLACK);
            return grand;
        }
        if (isSecondCaseAfterInsertion(current)) {
            Node next = current.getParent();
            rotation(current);
            current = next;
        }
        //else Third Case:
        Node grand = current.getParent().getParent();
        grand.setColor(Node.Color.RED);
        current.getParent().setColor(Node.Color.BLACK);
        rotation(current.getParent());
        return current.getParent();
    }

    private boolean isFirstCaseAfterInsertion(Node child) {
        Node grand = child.getParent().getParent();
        return grand.getRight().getColor() == Node.Color.RED && grand.getLeft().getColor() == Node.Color.RED;
    }

    private boolean isSecondCaseAfterInsertion(Node child) {
        Node grand = child.getParent().getParent();
        Node uncle;
        if (grand.getRight() == child.getParent()) {
            uncle = grand.getLeft();
            return uncle.getColor() == Node.Color.BLACK && child.getParent().getLeft() == child;
        } else {
            uncle = grand.getRight();
            return uncle.getColor() == Node.Color.BLACK && child.getParent().getRight() == child;
        }
    }

    @Override
    public void delete(String s) {
        super.delete(s);
    }
}
