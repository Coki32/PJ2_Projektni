package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Vector3D;

/**
 * 
 */
public abstract class UnmannedAircraft extends Aircraft {

    public UnmannedAircraft(){
        super(new Vector3D(), Direction.DOWN);
    }
}