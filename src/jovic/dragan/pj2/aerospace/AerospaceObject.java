package jovic.dragan.pj2.aerospace;


import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Pair;
import jovic.dragan.pj2.util.Vector2D;
import jovic.dragan.pj2.util.Vector3D;

import java.io.Serializable;

/**
 * 
 */
public abstract class AerospaceObject implements Serializable {

    private static int ID = 1;
    private String name;
    private Integer speed;
    private Vector2D direction;

    //altitude je bolje, preimenuj kasnije
    private int altitude, x, y;

    private boolean skip;
    private int ticks;

    public AerospaceObject(Vector3D position, Vector2D direction) {
        ticks = 0;
        speed = 1;
        skip = false;
        x=position.getX();
        y=position.getY();
        altitude =position.getZ();
        this.direction = direction;
        name = "avion"+ID;
        ID++;
    }

    public AerospaceObject(int x, int y, int altitude, int speed, Direction dir){
        ticks=0;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.altitude = altitude;
        this.direction = dir.getDirectionVector();
        name = "Avion"+ID;
        ID++;
    }

    public Vector2D getDirection(){
        return direction;
    }

    public void setDirection(Direction dir){
        direction = dir.getDirectionVector();
    }

    public AerospaceObject(Vector3D position, Direction side) {
        this(position, side.getDirectionVector());
    }

    public String export(){
        return "("+x+","+y+","+altitude+")";
    }

    public Pair<Integer, Integer> getNextPosition() {
        if (skip) {
            skip = false;
            return new Pair<>(x, y);
        }
        ticks++;
        if (ticks % speed == 0) {
            x += direction.getX();
            y += direction.getY();
            ticks = 0;
        }
        return new Pair<>(x, y);
    }

    @Override
    public String toString() {
        return "(" + name+", "+x+","+y+"," + direction+")";
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