package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Vector3D;

import java.io.Serializable;

/**
 * 
 */
public abstract class UnmannedAircraft extends Aircraft implements Serializable {

    public UnmannedAircraft(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
    }
}