package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Vector3D;

import java.io.Serializable;

/**
 * 
 */
public abstract class UnmannedAircraft extends Aircraft implements Serializable {

    public UnmannedAircraft(){
        super(new Vector3D(), Direction.DOWN);
    }
}