package jovic.dragan.pj2.personel.belongings;

import jovic.dragan.pj2.personel.Person;

import java.io.Serializable;

public class Passport implements Serializable {

    private int ID;
    private Person owner;
    private static int count = 0;

    public Passport(Person owner) {
        this.owner = owner;
        this.ID = ++count;
    }

}