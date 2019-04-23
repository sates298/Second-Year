package pl.swozniak.trees.basetree;


import java.util.Objects;

public class Node {
    private String value;
    private Node parent;
    private Node left;
    private Node right;

    private Color color;

    public final static Node nullNode;
    static{
        nullNode = new Node("", Node.Color.BLACK);
        nullNode.setRight(nullNode);
        nullNode.setLeft(nullNode);
        nullNode.setParent(nullNode);
    }

    public Node(String value){
        this.value = value;
        this.parent = nullNode;
        this.left = nullNode;
        this.right = nullNode;
        this.color = Color.NONE;
    }

    public Node(String value, Color color){
        this(value);
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
        if(left != nullNode) left.setParent(this);
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
        if(right != nullNode) right.setParent(this);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public enum Color{
        BLACK, RED, NONE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(value, node.value) &&
                Objects.equals(parent, node.parent) &&
                Objects.equals(left, node.left) &&
                Objects.equals(right, node.right) &&
                color == node.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, parent, left, right, color);
    }
}
