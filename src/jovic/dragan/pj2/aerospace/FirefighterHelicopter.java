package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.interfaces.Firefighter;
import jovic.dragan.pj2.personel.Pilot;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.io.Serializable;

/**
 * 
 */
public class FirefighterHelicopter extends Helicopter implements Firefighter,Serializable {

    private static int ID=100;
    private int waterAmount;
    public FirefighterHelicopter(int x, int y, int altitude, int speed, Direction direction) {
        super(x, y, altitude, speed, direction);
        setModel("PPH-"+ID);
        addCrew(new Pilot("Pilot",String.valueOf(ID)));
        ID+=10;
        drawingColor = Constants.Colors.FIREFIGHTER_HELICOPTER;
        waterAmount = Util.randomBetween(1, 10) * 1000;//1000-10000 l vode
    }

    @Override
    public void extinguishFire() {
        System.out.println("Helikopter "+model+" gasi pozar!");
    }
}