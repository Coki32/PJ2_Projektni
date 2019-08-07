package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Transporter;
import jovic.dragan.pj2.aerospace.equipment.Cargo;
import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;

/**
 * 
 */
public abstract class TransportPlane extends Aeroplane implements Transporter, Serializable {

    public TransportPlane(int x, int y, int altitude, int speed, Direction direction) {
        super(x,y,altitude,speed,direction);
    }

    private Cargo cargo;

    private Integer MaxCargoWeight;

}