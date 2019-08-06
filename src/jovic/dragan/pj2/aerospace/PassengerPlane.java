package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.PassengerCarrier;
import jovic.dragan.pj2.personel.Person;
import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class PassengerPlane extends Aeroplane implements PassengerCarrier, Serializable {

    private int numberOfSeats;

    public PassengerPlane(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);

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