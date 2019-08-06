package jovic.dragan.pj2.aerospace;


import jovic.dragan.pj2.Interfaces.Military;
import jovic.dragan.pj2.aerospace.equipment.Weapon;
import jovic.dragan.pj2.preferences.WeaponPreferences;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Vector3D;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 */
public abstract class MilitaryPlane extends Aeroplane implements Military, Serializable {

    private List<Weapon> weapons;
    protected static WeaponPreferences weaponPreferences = WeaponPreferences.load();
    private boolean foreign;
    protected enum WeaponType{BOMBER, PLANE}

    public MilitaryPlane(int x, int y, Direction s) {
        super(x,y,s);
    }

    public MilitaryPlane(int x, int y, int altitude, int speed, Direction direction) {
        super(x, y, altitude, speed, direction);
        weapons = new ArrayList<>();
    }


    @Override
    public String export() {
        return super.export()+"(military, foreign:"+foreign+")";
    }

    protected void generateRandomWeapons(WeaponType type, int count){
        Random random = new Random();
        String[] source = type==WeaponType.BOMBER ? weaponPreferences.getBomberWeapons() : weaponPreferences.getPlaneWeapons();
        for(int i=0; i<count; i++)
            addWeapon(new Weapon(source[random.nextInt(source.length)]));
    }

    public void addWeapon(Weapon w) {
        weapons.add(w);
    }

    @Override
    public void attack(Vector3D position) {

    }

    public boolean isForeign() {
        return foreign;
    }

    public void setForeign(boolean foreign) {
        this.foreign = foreign;
    }

}