package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Vector3D;

/**
 * 
 */
public abstract class Aeroplane extends Aircraft {

    public Aeroplane(int x, int y, Direction s) {
        super(new Vector3D(x,y,0), s);
    }

    public Aeroplane(int x, int y, int altitude, int speed, Direction direction) {
        super(x, y, altitude, speed, direction);
    }

}