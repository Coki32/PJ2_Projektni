package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.interfaces.PassengerCarrier;
import jovic.dragan.pj2.personel.Passenger;
import jovic.dragan.pj2.personel.Person;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 */
public class PassengerPlane extends Aeroplane implements PassengerCarrier, Serializable {

    private int numberOfSeats;

    public PassengerPlane(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
        this.setCrew(Stream.generate(new Supplier<Person>() {
            int i = 0;//Samo zbog ovog i...

            @Override
            public Person get() {
                return new Passenger("Putnik", String.valueOf(++i));
            }
        }).limit(Util.randomBetween(1, Constants.PLANE_CREW_LIMIT)).collect(Collectors.toList()));

        drawingColor = Constants.Colors.PASSENGER_PLANE;

        assignRandomModel(modelPreferences.getPlanes());
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