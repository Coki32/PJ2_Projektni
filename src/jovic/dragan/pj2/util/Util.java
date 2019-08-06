package jovic.dragan.pj2.util;

public class Util {
    public static  <T extends Comparable<T>>  int  minIdx(T[] array){
        if(array==null)
            return -1;
        int minIdx = 0;
        for(int i=1; i<array.length;i++)
            if(array[i].compareTo(array[minIdx]) < 0)
                minIdx = i;
        return  minIdx;
    }
}
