package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Transporter;
import jovic.dragan.pj2.aerospace.equipment.Cargo;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class TransportHelicopter extends Helicopter implements Serializable, Transporter {

    private List<Cargo> cargo;
    private int maxCargoWeight, currentCargoWeight = 0;

    public TransportHelicopter(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
        maxCargoWeight = Util.randomBetween(1, 3) * 1000;
        cargo = new ArrayList<>();
        drawingColor = Constants.Colors.TRANSPORT_HELICOPTER;

    }

    @Override
    public void addCargo(Cargo c) {
        if (currentCargoWeight + c.getWeight() < maxCargoWeight)
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