package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.interfaces.Firefighter;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.io.Serializable;

/**
 * 
 */
public class FirefighterPlane extends Aeroplane implements Firefighter, Serializable {

    private int waterCapacity;
    private static int ID = 1000;

    public FirefighterPlane(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
        waterCapacity = Util.randomBetween(1000, 5000);
        setModel("PPA-"+ID);
        ID+=100;
        drawingColor = Constants.Colors.FIREFIGHTER_PLANE;
    }

    public void extinguishFire() {
        System.out.println("PPA gasi pozar!");
    }

}