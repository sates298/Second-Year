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
        fixAfterInsertion(added);
    }

    private void fixAfterInsertion(Node curr) {
        comparisons++;
        while (curr != nullNode) {
            comparisons++;
            curr = checkRBTConditionsAfterInsertion(curr);
        }
    }

    private Node checkRBTConditionsAfterInsertion(Node current) {
        comparisons++;
        if (this.root == current) {
            current.setColor(Node.Color.BLACK);
            return nullNode;
        }
        comparisons+=2;
        if (current.getColor() == Node.Color.BLACK || current.getParent().getColor() == Node.Color.BLACK)
            return nullNode;
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
        return nullNode;
    }

    private boolean isFirstCaseAfterInsertion(Node child) {
        Node grand = child.getParent().getParent();
        comparisons+=2;
        return grand.getRight().getColor() == Node.Color.RED && grand.getLeft().getColor() == Node.Color.RED;
    }

    private boolean isSecondCaseAfterInsertion(Node child) {
        Node grand = child.getParent().getParent();
        Node uncle;
        comparisons++;
        if (grand.getRight() == child.getParent()) {
            uncle = grand.getLeft();
            comparisons+=2;
            return uncle.getColor() == Node.Color.BLACK && child.getParent().getLeft() == child;
        } else {
            uncle = grand.getRight();
            comparisons+=2;
            return uncle.getColor() == Node.Color.BLACK && child.getParent().getRight() == child;
        }
    }

    @Override
    public void delete(String s) {
        long time = System.currentTimeMillis();

        Node deleted = super.getNodeByValue(s);
        comparisons++;
        if (deleted == nullNode) return;
        Node curr, replacement = nullNode, parentReplacement = nullNode;
        comparisons+=2;
        if (deleted.getLeft() == nullNode && deleted.getRight() == nullNode) {
            curr = nullNode;
        } else if (deleted.getLeft() == nullNode) {
            curr = deleted.getRight();
            comparisons++;
            comparisons++;
            if (deleted.getColor() == Node.Color.BLACK) {
                curr.setColor(Node.Color.BLACK);
            }
        } else if (deleted.getRight() == nullNode) {
            comparisons+=2;
            curr = deleted.getLeft();
            comparisons++;
            if (deleted.getColor() == Node.Color.BLACK) {
                curr.setColor(Node.Color.BLACK);
            }
        } else {
            comparisons+=2;
            curr = findSuccessor(deleted);

            curr.setLeft(deleted.getLeft());
            swapNodes++;
            comparisons++;
            if (curr.getParent() != deleted) {
                comparisons++;
                if (curr.getColor() == Node.Color.BLACK) {
                    parentReplacement = curr.getParent();
                    replacement = curr.getRight();
                }
                curr.getParent().setLeft(curr.getRight());
                swapNodes++;
                curr.setRight(deleted.getRight());
                swapNodes++;
            }else{
                comparisons++;
                if(curr.getColor() == Node.Color.BLACK){
                    parentReplacement = curr;
                    replacement = curr.getRight();
                }
            }

        }

        super.setParentAfterDeletion(deleted, curr);

        fixAfterDeletion(replacement, parentReplacement);
        this.size--;
        time = System.currentTimeMillis() - time;
        updateAvgDelete(time);
    }

    private void fixAfterDeletion(Node replacement, Node parent) {
        comparisons+=2;
        while(parent != nullNode && replacement.getColor() == Node.Color.BLACK){
            replacement = checkRBTConditionsAfterDeletion(parent, replacement);
            parent = replacement.getParent();
            comparisons+=2;
        }
    }

    private Node checkRBTConditionsAfterDeletion(Node parent, Node child) {
        comparisons++;
        if (parent == nullNode) {
            setRoot(child);
            return child;
        }

        comparisons++;
        Node brother = parent.getRight() == child ? parent.getLeft() : parent.getRight();
        if (isFirstCaseAfterDeletion(brother)) {
            parent.setColor(Node.Color.RED);
            brother.setColor(Node.Color.BLACK);
            rotation(brother);

            //update brother
            brother = parent.getRight() == child ? parent.getLeft() : parent.getRight();
        }
        if (isSecondCaseAfterDeletion(brother)) {
            brother.setColor(Node.Color.RED);
            return parent;
        }

        Node innerChild, outerChild;
        comparisons++;
        if (brother.getParent().getRight() == brother) {
            outerChild = brother.getRight();
            innerChild = brother.getLeft();
        } else {
            outerChild = brother.getLeft();
            innerChild = brother.getRight();
        }

        if (isThirdCaseAfterDeletion(innerChild, outerChild)) {
            brother.setColor(Node.Color.RED);
            innerChild.setColor(Node.Color.BLACK);
            rotation(innerChild);

            //update brother and his outer child
            brother = innerChild;
            comparisons++;
            if (brother.getParent().getRight() == brother) {
                outerChild = brother.getRight();
            } else {
                outerChild = brother.getLeft();
            }
        }
        outerChild.setColor(Node.Color.BLACK);
        brother.setColor(parent.getColor());
        parent.setColor(Node.Color.BLACK);
        rotation(brother);

        return nullNode;
    }

    private boolean isFirstCaseAfterDeletion(Node brother) {
        comparisons++;
        return brother.getColor() == Node.Color.RED;
    }

    private boolean isSecondCaseAfterDeletion(Node brother) {
        comparisons+=3;
        return brother.getColor() == Node.Color.BLACK &&
                brother.getRight().getColor() == Node.Color.BLACK &&
                brother.getLeft().getColor() == Node.Color.BLACK;
    }

    private boolean isThirdCaseAfterDeletion(Node inner, Node outer) {
        comparisons+=2;
        return inner.getColor() == Node.Color.RED && outer.getColor() == Node.Color.BLACK;
    }

}
