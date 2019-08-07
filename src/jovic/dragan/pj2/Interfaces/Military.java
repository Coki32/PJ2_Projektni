package jovic.dragan.pj2.Interfaces;

import jovic.dragan.pj2.util.Vector3D;

import java.util.*;

/**
 * 
 */
public interface Military {

    default void attack(int x, int y, int altitude){
        System.out.println("Vojska napada (x,y,z)=("+x+","+y+","+altitude+")");
    }

}