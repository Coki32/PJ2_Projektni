package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.PassengerCarrier;
import jovic.dragan.pj2.personel.Person;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * 
 */
public class PassengerPlane extends Aeroplane implements PassengerCarrier, Serializable {

    private int numberOfSeats;

    public PassengerPlane(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
        String[] models = modelPreferences.getPlanes();
        setModel(models[Util.randomBetween(0,models.length-1)]);
    }

    @Override
    public String export() {
        return super.export() + "," + Color.green.getRGB();
    }

    @Override
    public List<Person> getPassengers() {
        return this.getCrew();
    }

    @Override
    public int getNumberOfSeats() {
        return this.numberOfSeats;
    }

}