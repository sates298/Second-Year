package pl.swozniak;


public class Comparators {

    public static int ascending(Comparable fst, Comparable snd){
        if(fst.compareTo(snd) < 0 ){
            return 1;
        }
        if(fst.compareTo(snd) > 0){
            return -1;
        }
        return 0;
    }

    public static int descending(Comparable fst, Comparable snd){
        if(fst.compareTo(snd) > 0 ){
            return 1;
        }
        if(fst.compareTo(snd) < 0){
            return -1;
        }
        return 0;
    }
}
