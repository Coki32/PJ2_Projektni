package jovic.dragan.pj2.aerospace;


import jovic.dragan.pj2.aerospace.equipment.Weapon;
import jovic.dragan.pj2.preferences.WeaponPreferences;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

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
        //drawing color postavlja MilitaryAricraft
    }

    protected void generateRandomWeapons(String[] weapons, int limit) {
        int count = Util.randomBetween(0, limit);
        for (int i = 0; i < limit; i++)
            this.weapons.add(new Weapon(weapons[Util.randomBetween(0, weapons.length - 1)]));
    }

    public void addWeapon(Weapon w) {
        weapons.add(w);
    }

}