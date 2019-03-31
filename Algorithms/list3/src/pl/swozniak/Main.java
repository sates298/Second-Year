package pl.swozniak;

import pl.swozniak.queue.PriorityQueue;

public class Main {

    public static void main(String[] args) {
        PriorityQueue heap = new PriorityQueue(4);
        heap.insert(3, 5);
        heap.insert(2, 4);
        heap.insert(12, 5);
        heap.insert(7, 2);
        heap.insert(1, 0);
        heap.print();
        heap.pop();
        heap.print();
        heap.insert(5, 3);
        heap.print();
    }
}
