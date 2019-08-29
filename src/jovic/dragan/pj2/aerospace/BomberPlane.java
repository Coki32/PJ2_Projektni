package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.aerospace.equipment.Weapon;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.io.Serializable;

/**
 * 
 */
public class BomberPlane extends MilitaryPlane implements Serializable {

    public BomberPlane(int x, int y, int altitude, int speed, Direction direction) {
        super(x, y, altitude, speed, direction);
        generateRandomWeapons(Util.randomBetween(0,weaponPreferences.getBomberCarryLimit()));
        String[] models = modelPreferences.getMilitaryBombers();
        setModel(models[Util.randomBetween(0,models.length-1)]);
    }

    @Override
    public void attack(int x, int y, int altitude) {
        System.out.println("Bombarder bombarduje (x,y,z)=(" + x+","+y+","+altitude+")");
    }

    @Override
    protected void generateRandomWeapons(int count) {
        String[] source = weaponPreferences.getBomberWeapons();
        for(int i=0; i<count; i++)
            addWeapon(new Weapon(source[Util.randomBetween(0,source.length-1)]));
    }
}