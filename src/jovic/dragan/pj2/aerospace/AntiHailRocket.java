package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;

public class AntiHailRocket extends Rocket implements Serializable {

    public AntiHailRocket(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
    }


}
