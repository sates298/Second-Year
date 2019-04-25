package pl.swozniak.studytasks;

import pl.swozniak.trees.BST;
import pl.swozniak.trees.Splay;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SecondTask {

    private List<String> words;
    private BST tree;

    public SecondTask(BST tree, String path) {
        this.words = readFile(path);
        this.tree = tree;
    }

    private List<String> readFile(String path) {
        List<String> result = new LinkedList<>();
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line, tmp[];
            while ((line = br.readLine()) != null) {
                tmp = line.split("[ .,;:\"()\\-?!]");
                for (String s : tmp) {
                    if(s.length() > 0){
                        result.add(s);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File doesnt exist.");
        } catch (IOException e) {
            System.err.println("Error with buffered reader");
        }

        return result;
    }

    public void setWords(List<String> words){
        this.words = words;
    }

    public List<String> permutation(){
        Collections.shuffle(words);
        return words;
    }

    private void insertAll() {
        for (String s : words) {
            tree.insert(s);
        }
    }

    private void searchAll() {
        for (String s : words) {
            tree.search(s);
        }
    }

    private void deleteAll() {
        for (String s : words) {
            tree.delete(s);
        }
    }

    public void run() {
        long time = System.currentTimeMillis();
        tree.setPatient(false);
        insertAll();
        searchAll();
        deleteAll();
        time = System.currentTimeMillis() - time;
        System.out.println("Time: " + time);
        System.out.println("Comparisons: " + tree.getComparisons());
        System.out.println("Nodes modifications: " + tree.getSwapNodes());
        System.out.println("Max capacity: " + tree.getMaxCapacity());
        System.out.println("Average insert: " + tree.getAvgInsert());
        System.out.println("Average search: " + tree.getAvgSearch());
        System.out.println("Average delete: " + tree.getAvgDelete());
        if(tree instanceof Splay) System.out.println("Average splay: " + ((Splay) tree).getAvgSplay());
        System.out.println();
    }

}
