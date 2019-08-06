package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Firefighter;
import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;

/**
 * 
 */
public class FirefighterPlane extends Aeroplane implements Firefighter, Serializable {

    /**
     * Default constructor
     */
    public FirefighterPlane() {
        super(0,0, Direction.UP);
    }

    /**
     * 
     */
    private Integer WaterCapacity;

    /**
     * 
     */
    public void extinguishFire() {
        // TODO implement here
    }

}