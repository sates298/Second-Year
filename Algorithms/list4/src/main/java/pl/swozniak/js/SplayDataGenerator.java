package pl.swozniak.js;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class SplayDataGenerator {
    public static void main(String[] args) throws IOException {
        PrintStream printStream = new PrintStream(new FileOutputStream("src/main/resources/best_SPLAY.txt"));
        for (int i = 0; i < 2_000_00; i++) {
            String word = "";
            if (i % 10 > 5) {
                word = "a";
            } else if (i % 4 == 3 || i% 4 == 2) {
                word = "b";
            /*} else if (i % 4 == 2) {
                word = "c";*/
            } else if (i % 4 == 1) {
                word = "d";
            } else {
                word = "e";
            }
            printStream.println(word);
        }
    }
}
