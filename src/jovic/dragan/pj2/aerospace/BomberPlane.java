package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.aerospace.equipment.Weapon;
import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;
import java.util.Random;

/**
 * 
 */
public class BomberPlane extends MilitaryPlane implements Serializable {

    public BomberPlane(int x, int y, Direction s) {
        super(x,y,s);
    }

    public BomberPlane(int x, int y, int altitude, int speed, Direction direction) {
        super(x, y, altitude, speed, direction);
        generateRandomWeapons(WeaponType.BOMBER,new Random().nextInt(weaponPreferences.getBomberCarryLimit()));
    }

}