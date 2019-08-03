package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Vector3D;

/**
 * 
 */
public abstract class Rocket extends AerospaceObject {

    private int range;

    public Rocket(){
        super(new Vector3D(), Direction.UP);
    }
}