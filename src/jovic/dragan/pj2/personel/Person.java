package jovic.dragan.pj2.personel;

import com.sun.source.doctree.SerialDataTree;

import java.io.Serializable;
import java.util.*;

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