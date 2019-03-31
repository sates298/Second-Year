package pl.swozniak;

import pl.swozniak.input.CommandLineInterface;
import pl.swozniak.queue.PriorityQueue;

public class Main {

    public static void main(String[] args) {
       /* PriorityQueue heap = new PriorityQueue(10);
        heap.insert(3, 17);
        heap.insert(3, 12);
        heap.insert(3, 9);
        heap.insert(2, 10);
        heap.insert(2, 0);
        heap.insert(5, 12);
        heap.insert(5, 11);
        heap.insert(4, 15);
        heap.print();
        heap.priority(3, 10);
        heap.print();*/
        CommandLineInterface commandLineInterface = new CommandLineInterface();
        commandLineInterface.run(args);
    }
}
