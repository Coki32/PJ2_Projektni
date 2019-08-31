package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.personel.Passenger;
import jovic.dragan.pj2.personel.Person;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.awt.*;
import java.io.Serializable;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 */
public class PassengerHelicopter extends Helicopter implements Serializable {

    public PassengerHelicopter(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
        this.setCrew(Stream.generate(new Supplier<Person>() {
            int i= 0;
            @Override
            public Person get() { return new Passenger("Putnik",String.valueOf(++i)); }
        }).limit(Util.randomBetween(1, Constants.HELICOPTER_CREW_LIMIT)).collect(Collectors.toList()));

        drawingColor = Constants.Colors.PASSENGER_HELICOPTER;

        assignRandomModel(modelPreferences.getHelicopters());
    }
}