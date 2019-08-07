package jovic.dragan.pj2.personel;

import jovic.dragan.pj2.personel.belongings.FlyingLicence;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class Pilot extends Person implements Serializable {


    private FlyingLicence licence;

    public Pilot(String name, String lastName) {
        super(name,lastName);
        licence = new FlyingLicence(this);
    }

}