package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;

/**
 * 
 */
public class FirefighterHelicopter extends Helicopter implements Serializable {

    public FirefighterHelicopter(int x, int y, int altitude, int speed, Direction direction) {
        super(x, y, altitude, speed, direction);
    }

}