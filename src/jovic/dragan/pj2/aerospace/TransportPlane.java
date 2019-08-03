package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Transporter;
import jovic.dragan.pj2.aerospace.equipment.Cargo;
import jovic.dragan.pj2.util.Direction;

/**
 * 
 */
public abstract class TransportPlane extends Aeroplane implements Transporter {

    public TransportPlane() {
        super(0,0, Direction.UP);
    }

    private Cargo cargo;

    private Integer MaxCargoWeight;

}