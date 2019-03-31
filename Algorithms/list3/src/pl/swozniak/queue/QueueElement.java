package pl.swozniak.queue;

public class QueueElement {
    private int value;
    private int priority;


    public QueueElement(int value, int priority) {
        this.priority = priority;
        this.value = value;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(" + value +
                ", " + priority + ')';
    }
}
