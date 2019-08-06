package jovic.dragan.pj2.aerospace.equipment;

import java.io.Serializable;

public class Weapon implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weapon() {
    }

    public Weapon(String name){
        this.name = name;
    }
}