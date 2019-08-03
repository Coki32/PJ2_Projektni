package jovic.dragan.pj2.Interfaces;

import jovic.dragan.pj2.personel.Person;
import java.util.*;

public interface PassengerCarrier {
    List<Person> getPassengers();
    int getNumberOfSeats();
}