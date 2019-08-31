package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Military;
import jovic.dragan.pj2.util.Direction;

import java.awt.*;
import java.io.Serializable;

public class MilitaryRocket extends Rocket implements Serializable, Military {

    @Override
    public void attack(int x, int y, int altitude) {
        System.out.println("Raketa napada (x,y,z)=(" + x+","+y+","+altitude+")");
    }

    public MilitaryRocket(int x, int y, int altitude, int speed, Direction direction){
        super(x,y,altitude,speed,direction);
        //drawing color postavlja raketa
    }



}
