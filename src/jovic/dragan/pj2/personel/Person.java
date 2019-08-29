package jovic.dragan.pj2.personel;

import java.io.Serializable;

/**
 * 
 */
public class Person implements Serializable {

    public Person(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    private String name;
    public String lastName;

}