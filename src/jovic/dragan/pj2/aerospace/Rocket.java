package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Vector3D;

import java.io.Serializable;

/**
 * 
 */
public abstract class Rocket extends AerospaceObject implements Serializable {
//Sta tacno raketa ima? Nista posebno, nema neku posebnu akciju..
    private int range;

    public Rocket(int x, int y, int altitiude, int speed, Direction direction){
        super(x,y,altitiude,speed,direction);
    }


}