package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Firefighter;
import jovic.dragan.pj2.personel.Pilot;
import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * 
 */
public class FirefighterHelicopter extends Helicopter implements Firefighter,Serializable {

    private static int ID=100;

    public FirefighterHelicopter(int x, int y, int altitude, int speed, Direction direction) {
        super(x, y, altitude, speed, direction);
        setModel("PPH-"+ID);
        addCrew(new Pilot("Pilot",String.valueOf(ID)));
        ID+=10;
    }

    @Override
    public void extinguishFire() {
        System.out.println("Helikopter "+model+" gasi pozar!");
    }
}