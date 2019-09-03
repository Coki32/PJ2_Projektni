package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;

/**
 * 
 */
public class FighterPlane extends MilitaryPlane implements Serializable {

    public FighterPlane(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
        generateRandomWeapons(weaponPreferences.getPlaneWeapons(), weaponPreferences.getPlaneCarryLimit());
        assignRandomModel(modelPreferences.getMilitaryPlanes());
        //drawing color postavlja MilitaryAricraft
    }

    @Override
    public void attack(int x, int y, int altitude) {
        System.out.println((foreign ? "Strani" : "Domaci") + " vojni avion napada (x,y,z)=(" + x + "," + y + "," + altitude + ")");
    }

    //Poziva se kad se domaca letjelica doda u aerospace da se ispise koga prati
    public void follow(Aircraft target) {
        System.out.println(this+" prati "+target);
    }

}