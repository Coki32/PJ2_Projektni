package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Military;
import jovic.dragan.pj2.util.Direction;


public abstract class MilitaryAircraft extends Aircraft implements Military {

    protected boolean foreign, followed;

    public MilitaryAircraft(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
        foreign = false;
        followed = false;
    }

    @Override
    public String export() {
        return super.export()+","+foreign+","+followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public boolean isFollowed() {
        return followed;
    }

    public boolean isForeign() {
        return foreign;
    }

    public void setForeign(boolean foreign) {
        this.foreign = foreign;
    }
}
