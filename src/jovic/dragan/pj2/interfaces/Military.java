package jovic.dragan.pj2.interfaces;

/**
 * 
 */
public interface Military {

    default void attack(int x, int y, int altitude){
        System.out.println("Vojska napada (x,y,z)=("+x+","+y+","+altitude+")");
    }

}