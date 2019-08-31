package jovic.dragan.pj2.aerospace;


import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Pair;
import jovic.dragan.pj2.util.Vector2D;

import java.awt.*;
import java.io.Serializable;

/**
 * 
 */
public abstract class AerospaceObject implements Serializable {

    private static int ID = 1;
    private Integer speed;
    private Vector2D directionVector;
    private Direction direction;
    private int id;

    //za export boje
    protected Color drawingColor;

    //altitude je bolje, preimenuj kasnije
    private int altitude, x, y;
    private boolean skip;
    private int ticks;


    public AerospaceObject(int x, int y, int altitude, int speed, Direction dir){
        ticks=0;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.altitude = altitude;
        this.direction = dir;
        this.directionVector = dir.getDirectionVector();
        drawingColor = new Color(77, 142, 179);//default boja
        id = ID++;
    }

    public Direction getDirection(){
        return direction;
    }

    public void setDirection(Direction dir){
        direction = dir;
        directionVector = dir.getDirectionVector();
    }

    public String export(){
        return id + "," + x + "," + y + "," + altitude + "," + direction + "," + drawingColor.getRGB();
    }

    public int getId() {
        return id;
    }

    public Pair<Integer, Integer> getNextPosition() {
        if (skip) {
            skip = false;
            return new Pair<>(x, y);
        }
        int newX = x, newY = y;
        ticks++;
        if (ticks % speed == 0) {
            newX += directionVector.getX();
            newY += directionVector.getY();
            ticks = 0;
        }
        return new Pair<>(newX, newY);
    }

    @Override
    public String toString() {
        return "(" + id+", "+x+","+y+"," + direction+")";
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAltitude() {
        return altitude;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setSkip(boolean value) {
        skip = value;
    }

}