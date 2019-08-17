package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;

import java.awt.*;
import java.io.Serializable;

/**
 * 
 */
public class TransportHelicopter extends Helicopter implements Serializable {

    public TransportHelicopter(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
    }

    @Override
    public String export() {
        return super.export() + "," + Color.MAGENTA.getRGB();
    }
}