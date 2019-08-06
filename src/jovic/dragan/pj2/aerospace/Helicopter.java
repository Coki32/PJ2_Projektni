package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Vector3D;

import java.io.Serializable;

/**
 * 
 */
public abstract class Helicopter extends Aircraft implements Serializable {

    /**
     * Default constructor
     */
    public Helicopter() {
        super(new Vector3D(), Direction.DOWN);
    }

}