package jovic.dragan.pj2.personel;

import jovic.dragan.pj2.personel.belongings.Passport;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class Passenger extends Person implements Serializable {

    /**
     * Default constructor
     */
    public Passenger() {
    }

    /**
     * 
     */
    private Passport passport;

}