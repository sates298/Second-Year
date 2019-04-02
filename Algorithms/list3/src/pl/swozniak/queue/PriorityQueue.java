package pl.swozniak.queue;


public class PriorityQueue<T extends Comparable> {
    private QueueElement<T>[] heap;
    private int size;
    private T maxPriority;

    public PriorityQueue(int n, T maxPriority) {
        this.heap = new QueueElement[n];
        size = 0;
        this.maxPriority = maxPriority;
    }

    public void insert(int x, T p) {
        if (p.compareTo(maxPriority) < 0) return;
        QueueElement<T> newElement = new QueueElement<>(x, p);
        if (empty()) {
            this.size = 1;
            this.heap[0] = newElement;
        } else if (this.size < this.heap.length) {
            this.size++;
            this.heap[this.size - 1] = newElement;
            shiftUp(this.size - 1);
        } else {
            System.out.println("Too many elements");
        }
    }

    public boolean empty() {
        return this.heap[0] == null;
    }

    public void top() {
        if (empty()) {
            System.out.println();
        } else {
            System.out.println("top value is equal " + this.heap[0].getValue());
        }
    }

    public QueueElement<T> pop() {
        if (empty()) {
            System.out.println();
            return null;
        } else {
            System.out.println("top value is equal " + this.heap[0].getValue());
            return deleteTop();
        }
    }

    public void priority(int x, T p) {
        changePriority(x, p, 0);
    }

    public void print() {
        System.out.print("Heap : [");
        for (int i = 0; i < this.size; i++) {
            System.out.print(this.heap[i].toString() + " ");
        }
        System.out.println("]");
    }

    private void shiftUp(int index) {
        if (this.heap[(index - 1) / 2].getPriority().compareTo(this.heap[index].getPriority()) > 0) {
            QueueElement<T> temp = this.heap[(index - 1) / 2];
            this.heap[(index - 1) / 2] = this.heap[index];
            this.heap[index] = temp;
            shiftUp((index - 1) / 2);
        }
    }

    private void changePriority(int x, T p, int index) {
        if (this.heap[index].getPriority().compareTo(p) > 0) {
            if (this.heap[index].getValue() == x) {
                this.heap[index].setPriority(p);
                shiftUp(index);
            }
        }
        if (this.size > (index + 1) * 2 - 1) {
            changePriority(x, p, (index + 1) * 2 - 1);
            if (this.size > (index + 1) * 2) {
                changePriority(x, p, (index + 1) * 2);
            }
        }
    }

    private QueueElement<T> deleteTop() {
        QueueElement<T> deleted = this.heap[0];
        this.size--;
        this.heap[0] = this.heap[this.size];
        this.heap[size] = null;
        shiftDown(0);
        return deleted;
    }

    private void shiftDown(int index) {
        if ((index + 1) * 2 > this.size - 1) return;
        int compareIndex;
        if ((index + 1) * 2 - 1 > this.size - 1) {
            compareIndex = (index + 1) * 2 - 1;
        } else {
            if (this.heap[(index + 1) * 2 - 1].getPriority().compareTo(this.heap[(index + 1) * 2].getPriority()) < 0) {
                compareIndex = (index + 1) * 2 - 1;
            } else {
                compareIndex = (index + 1) * 2;
            }
        }
        if (this.heap[index].getPriority().compareTo(heap[compareIndex].getPriority()) > 0) {

            QueueElement<T> tmp = this.heap[index];
            this.heap[index] = this.heap[compareIndex];
            this.heap[compareIndex] = tmp;

            shiftDown(compareIndex);
        }
    }

}
