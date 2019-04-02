package pl.swozniak.queue;

public class QueueElement<T extends Comparable> {
    private int value;
    private T priority;


    public QueueElement(int value, T priority) {
        this.priority = priority;
        this.value = value;
    }

    public T getPriority() {
        return priority;
    }

    public void setPriority(T priority) {
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
