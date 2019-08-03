package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Firefighter;
import jovic.dragan.pj2.util.Direction;

/**
 * 
 */
public class FirefighterPlane extends Aeroplane implements Firefighter {

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