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


    public PassengerPlane() {
        super(0,0, Direction.UP);
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