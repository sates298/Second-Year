package pl.swozniak.trees;

import pl.swozniak.trees.basetree.AbstractBinaryTree;
import pl.swozniak.trees.basetree.Node;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

import static pl.swozniak.trees.basetree.Node.nullNode;

public class BST extends AbstractBinaryTree {

    protected boolean isPatient;

    public BST(Comparator<String> comparator) {
        super(comparator);
        isPatient = true;
    }

    @Override
    public void insertValue(String s) {
        Node added = new Node(s);
        insertNode(added);
    }

    public void setPatient(boolean patient) {
        isPatient = patient;
    }

    void insertNode(Node added){
        size++;
        if(this.maxCapacity < size){
            this.maxCapacity = size;
        }
        comparisons++;
        if (this.root == nullNode) {
            setRoot(added);
            return;
        }
        Node prev;
        Node curr = this.root;
        comparisons++;
        while (curr != nullNode) {
            comparisons++;
            prev = curr;
            comparisons++;
            if (comparator.compare(added.getValue(), curr.getValue()) < 0) {
                curr = curr.getLeft();
                comparisons++;
                if (curr == nullNode) {
                    prev.setLeft(added);
                    swapNodes++;
                }
            } else {
                curr = curr.getRight();
                comparisons++;
                if (curr == nullNode) {
                    prev.setRight(added);
                    swapNodes++;
                }
            }
        }
    }

    @Override
    public void delete(String s) {
        long time = System.currentTimeMillis();

        Node deleted = getNodeByValue(s);
        comparisons++;
        if (deleted == nullNode) return;
        Node curr;
        comparisons+=2;
        if (deleted.getLeft() == nullNode && deleted.getRight() == nullNode) {
            curr = nullNode;
        }else if (deleted.getLeft() == nullNode) {
            comparisons++;
            curr = deleted.getRight();
        }else if (deleted.getRight() == nullNode) {
            comparisons+=2;
            curr = deleted.getLeft();
        }else{
            comparisons+=2;
            curr = findSuccessor(deleted);
            curr.setLeft(deleted.getLeft());
            swapNodes++;
            comparisons++;
            if(curr.getParent() != deleted){
                curr.getParent().setLeft(curr.getRight());
                swapNodes++;
                curr.setRight(deleted.getRight());
                swapNodes++;
            }
        }

        setParentAfterDeletion(deleted, curr);
        this.size--;

        time = System.currentTimeMillis() - time;
        updateAvgDelete(time);
    }

    void setParentAfterDeletion(Node deleted, Node replacement){
        Node parent = deleted.getParent();
        comparisons++;
        if (parent == nullNode) {
            this.setRoot(replacement);
        }else{
            comparisons++;
            if(parent.getLeft() == deleted){
                parent.setLeft(replacement);
                swapNodes++;
            }else{
                parent.setRight(replacement);
                swapNodes++;
            }
        }
    }

    @Override
    public boolean search(String s) {
        long time = System.currentTimeMillis();
        Node curr = getNodeByValue(s);
        comparisons++;
        if (curr == nullNode) {
            if(isPatient)System.out.println(s + " -> 0");
            time = System.currentTimeMillis() - time;
            updateAvgSearch(time);
            return false;
        } else {
            if(isPatient)System.out.println(s + " -> 1");
            time = System.currentTimeMillis() - time;
            updateAvgSearch(time);
            return true;
        }
    }

    Node getNodeByValue(String s) {
        if (s == null) return nullNode;
        Node curr = this.root;
        String currValue;
        comparisons++;
        while (curr != nullNode) {
            comparisons++;
            currValue = curr.getValue();
            comparisons++;
            if (comparator.compare(s, currValue) == 0) {
                return curr;
            }
            comparisons++;
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
        if(isEmpty()){
            System.out.println();
            return;
        }
        Deque<Node> stack = new ArrayDeque<>();
        Node curr = this.root;
        comparisons++;
        while (!stack.isEmpty() || curr != nullNode) {
            comparisons++;
            comparisons++;
            if (curr != nullNode) {
                stack.push(curr);
                curr = curr.getLeft();
            } else {
                curr = stack.pop();
                System.out.println(curr.getValue()); // + " \"" + curr.getColor() + "\"" + curr.getLeft().getValue() + " " + curr.getRight().getValue());
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
        comparisons++;
        if (n == nullNode) return;
        recursiveInOrder(n.getLeft());
        System.out.println(n.getValue());
        recursiveInOrder(n.getRight());
    }

    Node findSuccessor(Node n) {
        comparisons++;
        if(n == nullNode) return nullNode;
        comparisons++;
        if (n.getRight() != nullNode) {
            return findMin(n.getRight());
        }
        Node parent = n.getParent();
        Node curr = n;
        comparisons += 2;
        while (parent != nullNode && parent.getLeft() != curr) {
            this.comparisons += 2;
            curr = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    private Node findMin(Node n) {
        Node curr = n;
        comparisons++;
        while (curr.getLeft() != nullNode) {
            comparisons++;
            curr = curr.getLeft();
        }
        return curr;
    }

    protected void rotation(Node child) {
        Node parent = child.getParent();
        comparisons++;
        if (parent != nullNode) {
            comparisons++;
            //set new parent for child
            if(parent.getParent() != nullNode) {
                comparisons++;
                if(parent.getParent().getRight() == parent){
                    parent.getParent().setRight(child);
                    swapNodes++;
                }else{
                    parent.getParent().setLeft(child);
                    swapNodes++;
                }
            }else{
                setRoot(child);
            }
            comparisons++;
            //rotation parent and child
            if (parent.getLeft() == child) {
                parent.setLeft(child.getRight());
                swapNodes++;
                child.setRight(parent);
                swapNodes++;
            } else {
                parent.setRight(child.getLeft());
                swapNodes++;
                child.setLeft(parent);
                swapNodes++;
            }
        }

    }
}
