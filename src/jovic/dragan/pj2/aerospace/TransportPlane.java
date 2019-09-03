package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.aerospace.equipment.Cargo;
import jovic.dragan.pj2.interfaces.Transporter;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class TransportPlane extends Aeroplane implements Transporter, Serializable {

    private List<Cargo> cargo;
    private int maxCargoWeight, currentCargoWeight = 0;

    public TransportPlane(int x, int y, int altitude, int speed, Direction direction) {
        super(x,y,altitude,speed,direction);
        maxCargoWeight = Util.randomBetween(1, 10) * 1000;
        cargo = new ArrayList<>();
        drawingColor = Constants.Colors.TRANSPORT_PLANE;
    }

    @Override
    public void addCargo(Cargo c) {
        if (c.getWeight() + currentCargoWeight <= maxCargoWeight)
            cargo.add(c);
    }

    @Override
    public int getCurrentCargoWeight() {
        return currentCargoWeight;
    }

    @Override
    public int getMaximumCargoWeight() {
        return maxCargoWeight;
    }
}