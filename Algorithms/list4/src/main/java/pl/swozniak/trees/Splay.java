package pl.swozniak.trees;

import pl.swozniak.trees.basetree.Node;

import java.util.Comparator;

import static pl.swozniak.trees.basetree.Node.nullNode;

public class Splay extends BST {
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
        if (this.root == nullNode) {
            super.insertValue(s);
        } else {
            Node added = new Node(s);
            size++;
            if (comparator.compare(s, this.root.getValue()) < 0) {
                added.setLeft(this.root.getLeft());
                this.root.setLeft(added);
            } else {
                added.setRight(this.root.getRight());
                this.root.setRight(added);
            }
        }
    }

    @Override
    public void delete(String s) {
        splay(s);
        if (this.root != nullNode && this.root.getValue().equals(s)) {
            Splay left = new Splay(this.comparator, this.root.getLeft());
            Splay right = new Splay(this.comparator, this.root.getRight());
            right.splay(s);
            if (right.root != nullNode) {
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
        if ((this.root != nullNode) && (this.root.getValue().equals(s))) {
            System.out.println(s + " -> 1");
            return true;
        }
        System.out.println(s + " -> 0");
        return false;
    }

    private void splay(String s) {
        Node found = getClosestOne(s);
        if (found == nullNode) return;

        while (found.getParent() != nullNode) {
            if (found.getParent().getParent() == nullNode) {
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
        Node parent = nullNode;
        Node curr = this.root;
        while (curr != nullNode) {
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
}
