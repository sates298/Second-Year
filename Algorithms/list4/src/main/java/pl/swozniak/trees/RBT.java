package pl.swozniak.trees;


import com.sun.org.apache.regexp.internal.REDebugCompiler;
import pl.swozniak.trees.basetree.Node;

import java.util.Comparator;
import java.util.concurrent.BlockingDeque;

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
        while (curr != nullNode) {
            curr = checkRBTConditionsAfterInsertion(curr);
        }
    }

    private Node checkRBTConditionsAfterInsertion(Node current) {
        if (this.root == current) {
            current.setColor(Node.Color.BLACK);
            return nullNode;
        }
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
        Node deleted = super.getNodeByValue(s);
        if (deleted == nullNode) return;
        Node curr, replacement = nullNode, parentReplacement = nullNode;
        if (deleted.getLeft() == nullNode && deleted.getRight() == nullNode) {
            curr = nullNode;
        } else if (deleted.getLeft() == nullNode) {
            curr = deleted.getRight();
            if (deleted.getColor() == Node.Color.BLACK) {
                curr.setColor(Node.Color.BLACK);
            }
        } else if (deleted.getRight() == nullNode) {
            curr = deleted.getLeft();
            if (deleted.getColor() == Node.Color.BLACK) {
                curr.setColor(Node.Color.BLACK);
            }
        } else {
            curr = findSuccessor(deleted);

            curr.setLeft(deleted.getLeft());
            if (curr.getParent() != deleted) {
                if (curr.getColor() == Node.Color.BLACK) {
                    parentReplacement = curr.getParent();
                    replacement = curr.getRight();
                }
                curr.getParent().setLeft(curr.getRight());
                curr.setRight(deleted.getRight());
            }else{
                if(curr.getColor() == Node.Color.BLACK){
                    parentReplacement = curr;
                    replacement = curr.getRight();
                }
            }

        }

        super.setParentAfterDeletion(deleted, curr);

        fixAfterDeletion(replacement, parentReplacement);
        this.size--;
    }

    private void fixAfterDeletion(Node replacement, Node parent) {
        while(parent != nullNode && replacement.getColor() == Node.Color.BLACK){
            replacement = checkRBTConditionsAfterDeletion(parent, replacement);
            parent = replacement.getParent();
        }
    }

    private Node checkRBTConditionsAfterDeletion(Node parent, Node child) {
        if (parent == nullNode) {
            setRoot(child);
            return child;
        }

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
        return brother.getColor() == Node.Color.RED;
    }

    private boolean isSecondCaseAfterDeletion(Node brother) {
        return brother.getColor() == Node.Color.BLACK &&
                brother.getRight().getColor() == Node.Color.BLACK &&
                brother.getLeft().getColor() == Node.Color.BLACK;
    }

    private boolean isThirdCaseAfterDeletion(Node inner, Node outer) {
        return inner.getColor() == Node.Color.RED && outer.getColor() == Node.Color.BLACK;
    }

}
