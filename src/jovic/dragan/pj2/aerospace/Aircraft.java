package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.personel.Person;
import jovic.dragan.pj2.preferences.ModelPreferences;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public abstract class Aircraft extends AerospaceObject implements Serializable {

    protected String model;
    private Map<String,String> characteristics;
    private List<Person> crew;
    private int maxCrew;

    protected static ModelPreferences modelPreferences = ModelPreferences.load();

    public Aircraft(int x, int y, int altitude, int speed, Direction direction) {
        super(x, y, altitude, speed, direction);
        characteristics = new HashMap<>();
        crew = new ArrayList<>();
    }

    protected void assignRandomModel(String[] models) {
        setModel(models[Util.randomBetween(0, models.length - 1)]);
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Map<String, String> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Map<String, String> characteristics) {
        this.characteristics = characteristics;
    }

    public List<Person> getCrew() {
        return crew;
    }

    public void setCrew(List<Person> crew) {
        this.crew = crew;
    }

    public boolean addCrew(Person person) {
        return crew.add(person);
    }

    public boolean addCharacteristic(String name, String value){
        if(characteristics.containsKey(name))
            return false;
        else
            characteristics.put(name, value);
        return true;
    }

}