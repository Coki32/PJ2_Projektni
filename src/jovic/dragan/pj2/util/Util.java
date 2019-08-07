package jovic.dragan.pj2.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Util {

    private static Random random = new Random();

    public static  <T extends Comparable<T>>  int  minIdx(T[] array){
        if(array==null)
            return -1;
        int minIdx = 0;
        for(int i=1; i<array.length;i++)
            if(array[i].compareTo(array[minIdx]) < 0)
                minIdx = i;
        return  minIdx;
    }

    public static void createFolderIfNotExists(String path){
        if(!Paths.get(path).toFile().exists()) {
            Paths.get(path).toFile().mkdir();
        }
    }

    public Path pathResolver(String folder, Path path){
        return (Paths.get(folder).resolve(path));
    }

    /**
     * @param min minimum number the generator may return
     * @param max maximum number the generator may return, inclusive
     * @return random number in range [min, max]
     */
    public static int randomBetween(int min, int max){
        return random.nextInt(max-min+1)+min;
    }

}
