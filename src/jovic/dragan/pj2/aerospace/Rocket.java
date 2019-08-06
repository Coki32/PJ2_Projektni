package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Vector3D;

import java.io.Serializable;

/**
 * 
 */
public abstract class Rocket extends AerospaceObject implements Serializable {

    private int range;

    public Rocket(){
        super(new Vector3D(), Direction.UP);
    }
}