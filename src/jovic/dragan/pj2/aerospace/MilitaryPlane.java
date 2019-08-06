package jovic.dragan.pj2.aerospace;


import jovic.dragan.pj2.aerospace.equipment.Weapon;
import jovic.dragan.pj2.preferences.WeaponPreferences;
import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public abstract class MilitaryPlane extends MilitaryAircraft implements Serializable {

    private List<Weapon> weapons;
    protected static WeaponPreferences weaponPreferences = WeaponPreferences.load();
    protected enum WeaponType{BOMBER, PLANE}

    public MilitaryPlane(int x, int y, int altitude, int speed, Direction direction) {
        super(x, y, altitude, speed, direction);
        weapons = new ArrayList<>();
    }

    protected abstract void generateRandomWeapons(int count);

    public void addWeapon(Weapon w) {
        weapons.add(w);
    }

}