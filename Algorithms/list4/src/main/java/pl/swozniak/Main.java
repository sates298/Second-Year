package pl.swozniak;

import pl.swozniak.studytasks.FirstTask;
import pl.swozniak.studytasks.SecondTask;
import pl.swozniak.trees.BST;
import pl.swozniak.trees.RBT;
import pl.swozniak.trees.Splay;

import java.util.List;


public class Main {

    public static void main(String[] args) {
//        CommandLineInterface cli = new CommandLineInterface();
//        cli.run(args);
        StringComparator<String> comparator = new StringComparator<>();
        BST bst = new BST(comparator);
        BST splay = new Splay(comparator);
        BST rbt = new RBT(comparator);
        String path = FirstTask.SAMPLE_PATH;
        boolean isPermute = false;
        SecondTask taskBST = new SecondTask(bst, path);
        SecondTask taskSplay = new SecondTask(splay, path);
        SecondTask taskRBT = new SecondTask(rbt, path);
        if(isPermute){
            List<String> tmp = taskBST.permutation();
            taskSplay.setWords(tmp);
            taskRBT.setWords(tmp);
        }
        System.out.println("BST");
        taskBST.run();
        System.out.println("SPLAY");
        taskSplay.run();
        System.out.println("RBT");
        taskRBT.run();

    }
}
