package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;

/**
 * 
 */
public class BomberPlane extends MilitaryPlane implements Serializable {

    public BomberPlane(int x, int y, int altitude, int speed, Direction direction) {
        super(x, y, altitude, speed, direction);
        generateRandomWeapons(weaponPreferences.getBomberWeapons(), weaponPreferences.getBomberCarryLimit());
        assignRandomModel(modelPreferences.getMilitaryBombers());
        //drawing color postavlja MilitaryAricraft
    }

    @Override
    public void attack(int x, int y, int altitude) {
        System.out.println("Bombarder bombarduje (x,y,z)=(" + x+","+y+","+altitude+")");
    }

}