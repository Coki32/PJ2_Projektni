package jovic.dragan.pj2.aerospace.equipment;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class Cargo implements Serializable {
    private int weight;
    private String name;

    public Cargo(int weight, String name) {
        this.weight = weight;
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}