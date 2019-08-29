package jovic.dragan.pj2.personel.belongings;

import jovic.dragan.pj2.personel.Pilot;

import java.io.Serializable;

/**
 * 
 */
public class FlyingLicence implements Serializable {

    private int ID;
    private static int count = 0;
    private Pilot owner;

    public FlyingLicence(Pilot owner) {
        this.owner = owner;
        ID = ++count;
    }

}