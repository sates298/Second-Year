package pl.swozniak.trees.basetree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;

import static pl.swozniak.trees.basetree.Node.nullNode;

public abstract class AbstractBinaryTree {
    protected Node root;
    protected int size;

    protected Comparator<String> comparator;

    protected long comparisons = 0;
    protected int maxCapacity = 0;
    protected long swapNodes = 0;

    private double avgInsert = 0;
    private int insertAmount = 0;
    private double avgSearch = 0;
    private int searchAmount = 0;
    private double avgDelete = 0;
    private int deleteAmount = 0;



    public AbstractBinaryTree(Comparator<String> c){
        this.root = nullNode;
        this.size = 0;
        this.comparator = c;
    }

    protected void setRoot(Node node){
        this.root = node;
        swapNodes++;
        node.setParent(nullNode);
    }

    public boolean isEmpty(){
        return root == nullNode;
    }

    public void insert(String s){
        long time = System.currentTimeMillis();
        if(s.length() < 1) return;
        char last = s.charAt(s.length() - 1);
        char first = s.charAt(0);
        StringBuilder builder = new StringBuilder();
        if(s.length() > 2) {
            builder.append(s, 1, s.length() - 1);
            if (Character.isLowerCase(first) || Character.isUpperCase(first)) {
                builder.insert(0, first);
            }
            if (Character.isLowerCase(last) || Character.isUpperCase(last)) {
                builder.append(last);
            }
        }else{
            if (Character.isLowerCase(first) || Character.isUpperCase(first)) {
                builder.append(first);
            }
            if (s.length() > 1 && (Character.isLowerCase(last) || Character.isUpperCase(last))) {
                builder.append(last);
            }
        }
        String result = builder.toString();
        if(result.length() > 0) {
            insertValue(result);
            time = System.currentTimeMillis() - time;
            updateAvgInsert(time);
        }
    }
    protected abstract void insertValue(String s);

    public abstract void delete(String s);
    public abstract boolean search(String s);

    public void load(String path){
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line, words[];
            while((line = br.readLine()) != null){
                words = line.split("[ .,;:\"()\\-?!]");
                for(String s: words) {
                    if(s.length() > 0) {
                        insert(s);
                    }
                }
            }
        }catch(IOException e){
            System.err.println("IOException - file doesn't exist" + e);
        }
    }

    public abstract void inOrder();

    private void updateAvgInsert(long time){
        double sum = this.insertAmount*this.avgInsert;
        this.avgInsert = (sum + time)/(++this.insertAmount);
    }

    protected void updateAvgSearch(long time){
        double sum = this.searchAmount*this.avgSearch;
        this.avgSearch = (sum + time)/(++this.searchAmount);
    }

    protected void updateAvgDelete(long time){
       double sum = this.deleteAmount*this.avgDelete;
        this.avgDelete = (sum + time)/(++this.deleteAmount);
    }
    public double getAvgInsert() {
        return avgInsert;
    }

    public double getAvgSearch() {
        return avgSearch;
    }

    public double getAvgDelete() {
        return avgDelete;
    }

    public int getSize(){
        return size;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getSwapNodes() {
        return swapNodes;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}
