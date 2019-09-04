package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.interfaces.Reconnaissance;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;

/**
 * 
 */
public class UnmannedAircraft extends Aircraft implements Serializable, Reconnaissance {

    public UnmannedAircraft(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
        drawingColor = Constants.Colors.UNMANNED_AIRCRAFT;
    }

    @Override
    public void snimajTeren() {
        System.out.println("Bespilotna letjelica snima teren");
    }
}