package pl.swozniak.trees;

import pl.swozniak.trees.basetree.Node;

import java.util.Comparator;

public class Splay extends BST {
    public Splay(Comparator comparator) {
        super(comparator);
    }

    public Splay(Comparator comparator, Node root) {
        super(comparator);
        setRoot(root);
    }

    @Override
    public void insertValue(String s) {
        splay(s);
        if (this.root == null) {
            super.insertValue(s);
        } else {
            Node add = new Node(s);
            size++;
            if (comparator.compare(s, this.root.getValue()) < 0) {
                add.setLeft(this.root.getLeft());
                this.root.setLeft(add);
            } else {
                add.setRight(this.root.getRight());
                this.root.setRight(add);
            }
        }
    }

    @Override
    public void delete(String s) {
        splay(s);
        if (this.root != null && this.root.getValue().equals(s)) {
            Splay left = new Splay(this.comparator, this.root.getLeft());
            Splay right = new Splay(this.comparator, this.root.getRight());
            right.splay(s);
            if (right.root != null) {
                this.setRoot(right.root);
                this.root.setLeft(left.root);
            } else {
                this.setRoot(left.root);
            }
            size--;
        }
    }

    @Override
    public boolean search(String s) {
        splay(s);
        if ((this.root != null) && (this.root.getValue().equals(s))) {
            System.out.println(s + " -> 1");
            return true;
        }
        System.out.println(s + " -> 0");
        return false;
    }

    private void splay(String s) {
        Node found = getClosestOne(s);
        if (found == null) return;

        while (found.getParent() != null) {
            if (found.getParent().getParent() == null) {
                zig(found);
            } else if ((found.getParent().getLeft() == found &&
                    found.getParent().getParent().getLeft() == found.getParent()) ||
                    (found.getParent().getRight() == found &&
                            found.getParent().getParent().getRight() == found.getParent())) {
                zigZig(found);
            }else{
                zigZag(found);
            }
        }

        setRoot(found);
    }

    private Node getClosestOne(String s) {
        if (s == null) return null;
        Node parent = null;
        Node curr = this.root;
        while (curr != null) {
            if (comparator.compare(s, curr.getValue()) == 0) {
                return curr;
            } else if (comparator.compare(s, curr.getValue()) < 0) {
                parent = curr;
                curr = curr.getLeft();
            } else {
                parent = curr;
                curr = curr.getRight();
            }
        }
        return parent;
    }

    private void zig(Node child) {
        Node parent = child.getParent();
        if (parent != null) {
            //set new parent for child
            if(parent.getParent() != null) {
                if(parent.getParent().getRight() == parent){
                    parent.getParent().setRight(child);
                }else{
                    parent.getParent().setLeft(child);
                }
            }else{
                child.setParent(null);
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

    private void zigZig(Node child){
        Node parent = child.getParent();
        zig(parent);
        zig(child);
    }

    private void zigZag(Node child){
        zig(child);
        zig(child);
    }
}
