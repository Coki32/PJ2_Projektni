package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;

/**
 * 
 */
public class PassengerHelicopter extends Helicopter implements Serializable {

    public PassengerHelicopter(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);

    }
}