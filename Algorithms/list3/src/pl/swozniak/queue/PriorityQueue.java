package pl.swozniak.queue;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PriorityQueue<T extends Comparable> {
    private QueueElement<T>[] heap;
    private int size;
    private T maxPriority;
    private Map<Integer, ArrayList<Integer>> mapping;

    public PriorityQueue(int n, T maxPriority) {
        this.heap = new QueueElement[n];
        this.mapping = new HashMap<>();
        size = 0;
        this.maxPriority = maxPriority;
    }

    public void insert(int x, T p) {
        if (p.compareTo(maxPriority) < 0) return;
        QueueElement<T> newElement = new QueueElement<>(x, p);
        if (empty()) {
            this.size = 1;
            this.heap[0] = newElement;

            ArrayList<Integer> list = new ArrayList<>();
            list.add(0);
            this.mapping.put(newElement.getValue(), list);

        } else if (this.size < this.heap.length) {
            this.size++;
            this.heap[this.size - 1] = newElement;
            if (mapping.get(newElement.getValue()) == null) {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(this.size - 1);
                this.mapping.put(newElement.getValue(), list);
            }else{
                this.mapping.get(newElement.getValue()).add(this.size - 1);
            }
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

    public boolean priority(int x, T p) {
        boolean result = false;
        for(Integer i: this.mapping.get(x)){
            QueueElement curr = this.heap[i];
            if(curr.getPriority().compareTo(p) > 0){
                result = true;
                curr.setPriority(p);
                shiftUp(i);
            }
        }
        return result;
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

            int i = this.mapping.get(temp.getValue()).indexOf((index - 1) / 2);
            this.mapping.get(temp.getValue()).remove(i);
            this.mapping.get(temp.getValue()).add(i, index);

            i = this.mapping.get(this.heap[(index - 1) / 2].getValue()).indexOf(index);
            this.mapping.get(this.heap[(index - 1) / 2].getValue()).remove(i);
            this.mapping.get(this.heap[(index - 1) / 2].getValue()).add(i, (index - 1) / 2);

            shiftUp((index - 1) / 2);
        }
    }

    /*private void changePriority(int x, T p, int index) {
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
    }*/

    private QueueElement<T> deleteTop() {
        QueueElement<T> deleted = this.heap[0];
        this.size--;
        this.heap[0] = this.heap[this.size];
        this.heap[size] = null;

        this.mapping.get(deleted.getValue()).remove(new Integer(0));
        if(!empty()) {
            int i = this.mapping.get(this.heap[0].getValue()).indexOf(this.size);
            this.mapping.get(this.heap[0].getValue()).remove(i);
            this.mapping.get(this.heap[0].getValue()).add(i, 0);
        }

        shiftDown(0);
        return deleted;
    }

    private void shiftDown(int index) {
        if ((index + 1) * 2 - 1 > this.size - 1) return;
        int compareIndex;
        if ((index + 1) * 2 < this.size) {
            if (this.heap[(index + 1) * 2 - 1].getPriority().compareTo(this.heap[(index + 1) * 2].getPriority()) < 0) {
                compareIndex = (index + 1) * 2 - 1;
            } else {
                compareIndex = (index + 1) * 2;
            }
        } else {
            compareIndex = (index + 1) * 2 - 1;
        }
        if (this.heap[index].getPriority().compareTo(heap[compareIndex].getPriority()) > 0) {

            QueueElement<T> tmp = this.heap[index];
            this.heap[index] = this.heap[compareIndex];
            this.heap[compareIndex] = tmp;

            this.mapping.get(tmp.getValue()).remove(new Integer(index));
            this.mapping.get(tmp.getValue()).add(compareIndex);

            this.mapping.get(this.heap[index].getValue()).remove(new Integer(compareIndex));
            this.mapping.get(this.heap[index].getValue()).add(index);
            shiftDown(compareIndex);
        }
    }

}
