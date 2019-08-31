package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Military;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public abstract class MilitaryAircraft extends Aircraft implements Military {

    protected boolean foreign, followed;

    protected List<MilitaryAircraft> followers;
    protected MilitaryAircraft following;

    public MilitaryAircraft(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
        foreign = false;
        followed = false;
        followers = new ArrayList<>();

        drawingColor = Constants.Colors.MILITARY_HOME;//jer je svaki domaci na pocetku
    }

    public void setFollowing(MilitaryAircraft target){
        following = target;
    }

    public MilitaryAircraft getFollowing(){
        return following;
    }

    public void addFollower(MilitaryAircraft follower){
        followers.add(follower);
    }

    public List<MilitaryAircraft> getFollowers(){
        return followers;
    }


    @Override
    public String export() {
        return super.export() + "," + foreign + "," + followed;
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
        drawingColor = foreign ? Constants.Colors.MILITARY_FOREIGN : Constants.Colors.MILITARY_HOME;
    }
}
