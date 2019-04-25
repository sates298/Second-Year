package pl.swozniak.trees;

import pl.swozniak.trees.basetree.Node;

import java.util.Comparator;

import static pl.swozniak.trees.basetree.Node.nullNode;

public class Splay extends BST {

    private int splays = 0;

    private double avgSplay = 0;
    private int splayAmount = 0;

    public Splay(Comparator<String> comparator) {
        super(comparator);
    }

    public Splay(Comparator<String> comparator, Node root) {
        super(comparator);
        setRoot(root);
    }

    @Override
    public void insertValue(String s) {
        splay(s);
        comparisons++;
        if (this.root == nullNode) {
            super.insertValue(s);
        } else {
            Node added = new Node(s);
            size++;
            if(this.maxCapacity < size){
                this.maxCapacity = size;
            }
            comparisons++;
            if (comparator.compare(s, this.root.getValue()) < 0) {
                added.setLeft(this.root.getLeft());
                swapNodes++;
                this.root.setLeft(added);
                swapNodes++;
            } else {
                added.setRight(this.root.getRight());
                swapNodes++;
                this.root.setRight(added);
                swapNodes++;
            }
        }
    }

    @Override
    public void delete(String s) {
        long time = System.currentTimeMillis();
        splay(s);
        comparisons+=2;
        if (this.root != nullNode && this.root.getValue().equals(s)) {
            Splay left = new Splay(this.comparator, this.root.getLeft());
            Splay right = new Splay(this.comparator, this.root.getRight());
            right.splay(s);
            this.splays += right.splays;
            this.swapNodes += right.swapNodes;
            this.comparisons += right.comparisons;
            comparisons++;
            if (right.root != nullNode) {
                this.setRoot(right.root);
                this.root.setLeft(left.root);
                swapNodes++;
            } else {
                this.setRoot(left.root);
            }
            size--;

            time = System.currentTimeMillis() - time;
            updateAvgDelete(time);
        }
    }

    @Override
    public boolean search(String s) {
        long time = System.currentTimeMillis();
        splay(s);
        comparisons+=2;
        if ((this.root != nullNode) && (this.root.getValue().equals(s))) {
            if(isPatient)System.out.println(s + " -> 1");
            time = System.currentTimeMillis() - time;
            updateAvgSearch(time);
            return true;
        }
        if(isPatient)System.out.println(s + " -> 0");
        time = System.currentTimeMillis() - time;
        updateAvgSearch(time);
        return false;
    }

    private void splay(String s) {
        long time = System.currentTimeMillis();

        splays++;
        Node found = getClosestOne(s);
        comparisons++;
        if (found == nullNode) return;
        comparisons++;
        while (found.getParent() != nullNode) {
            comparisons++;
            comparisons++;
            if (found.getParent().getParent() == nullNode) {
                zig(found);
            } else if ((found.getParent().getLeft() == found &&
                    found.getParent().getParent().getLeft() == found.getParent()) ||
                    (found.getParent().getRight() == found &&
                            found.getParent().getParent().getRight() == found.getParent())) {
                comparisons++;
                zigZig(found);
            }else{
                comparisons++;
                zigZag(found);
            }
        }

        time = System.currentTimeMillis() - time;
        updateAvgSplay(time);
        //setRoot(found);
    }

    private Node getClosestOne(String s) {
        if (s == null) return nullNode;
        Node parent = nullNode;
        Node curr = this.root;
        comparisons++;
        while (curr != nullNode) {
            comparisons++;
            if (comparator.compare(s, curr.getValue()) == 0) {
                return curr;
            } else if (comparator.compare(s, curr.getValue()) < 0) {
                parent = curr;
                curr = curr.getLeft();
                comparisons++;
            } else {
                parent = curr;
                curr = curr.getRight();
                comparisons++;
            }
            comparisons++;
        }
        return parent;
    }

    private void zig(Node child) {
        super.rotation(child);
    }

    private void zigZig(Node child){
        Node parent = child.getParent();
        zig(parent);
        zig(child);
    }

    private void zigZag(Node child){
        zig(child);
        zig(child);
    }

    public int getSplays() {
        return splays;
    }

    private void updateAvgSplay(long time){
        double sum = this.splayAmount*this.avgSplay;
        this.avgSplay = (sum + time)/(++this.splayAmount);
    }

    public double getAvgSplay() {
        return avgSplay;
    }
}
