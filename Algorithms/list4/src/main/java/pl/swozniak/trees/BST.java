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
        Node added = new Node(s);
        insertNode(added);
    }

    void insertNode(Node added){
        size++;
        if (this.root == nullNode) {
            setRoot(added);
            return;
        }
        Node prev;
        Node curr = this.root;
        while (curr != nullNode) {
            prev = curr;
            if (comparator.compare(added.getValue(), curr.getValue()) < 0) {
                curr = curr.getLeft();
                if (curr == nullNode) {
                    prev.setLeft(added);
                }
            } else {
                curr = curr.getRight();
                if (curr == nullNode) {
                    prev.setRight(added);
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

        setParentAfterDeletion(deleted, curr);
        this.size--;
    }

    void setParentAfterDeletion(Node deleted, Node replacement){
        Node parent = deleted.getParent();
        if (parent == nullNode) {
            this.setRoot(replacement);
        }else{
            if(parent.getLeft() == deleted){
                parent.setLeft(replacement);
            }else{
                parent.setRight(replacement);
            }
        }
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

    Node getNodeByValue(String s) {
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
                System.out.println(curr.getValue() + " \"" + curr.getColor() + "\""); // + curr.getLeft().getValue() + " " + curr.getRight().getValue());
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

    Node findSuccessor(Node n) {
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

    protected void rotation(Node child) {
        Node parent = child.getParent();
        if (parent != nullNode) {
            //set new parent for child
            if(parent.getParent() != nullNode) {
                if(parent.getParent().getRight() == parent){
                    parent.getParent().setRight(child);
                }else{
                    parent.getParent().setLeft(child);
                }
            }else{
                setRoot(child);
            }

            //rotation parent and child
            if (parent.getLeft() == child) {
                parent.setLeft(child.getRight());
                child.setRight(parent);
            } else {
                parent.setRight(child.getLeft());
                child.setLeft(parent);
            }
        }
    }
}
