package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Military;
import jovic.dragan.pj2.aerospace.equipment.Weapon;
import jovic.dragan.pj2.preferences.WeaponPreferences;
import jovic.dragan.pj2.util.Direction;

import java.util.List;
import java.util.Random;

public abstract class MilitaryAircraft extends Aircraft implements Military {

    protected boolean foreign;

    public MilitaryAircraft(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
    }

    @Override
    public String export() {
        return super.export()+","+foreign;
    }


    public boolean isForeign() {
        return foreign;
    }

    public void setForeign(boolean foreign) {
        this.foreign = foreign;
    }
}
