package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.aerospace.equipment.Weapon;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;
import jovic.dragan.pj2.util.Vector3D;

import java.io.Serializable;

/**
 * 
 */
public class BomberPlane extends MilitaryPlane implements Serializable {

    public BomberPlane(int x, int y, int altitude, int speed, Direction direction) {
        super(x, y, altitude, speed, direction);
        generateRandomWeapons(Util.randomBetween(0,weaponPreferences.getBomberCarryLimit()));
    }

    @Override
    public void attack(Vector3D position) {
        System.out.println("Bombarder bombarduje " + position);
    }

    @Override
    protected void generateRandomWeapons(int count) {
        String[] source = weaponPreferences.getBomberWeapons();
        for(int i=0; i<count; i++)
            addWeapon(new Weapon(source[Util.randomBetween(0,source.length-1)]));
    }
}