package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;
import java.util.Random;

/**
 * 
 */
public class FighterPlane extends MilitaryPlane implements Serializable {

    public FighterPlane() {
        super(0,0, Direction.UP);
    }

    public FighterPlane(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
        generateRandomWeapons(WeaponType.PLANE,new Random().nextInt(weaponPreferences.getPlaneCarryLimit()));
    }

    public void follow(Aircraft target) {
        // TODO implement here
    }

}