package jovic.dragan.pj2.radar;

import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Pair;

import java.io.Serializable;

public class ObjectInfo implements Serializable {
    private int x,y,altitude, id;
    private Direction direction;
    private boolean military,foreign, followed;

    public ObjectInfo(String... fields) {
        id = Integer.parseInt(fields[0]);
        x = Integer.parseInt(fields[1]);
        y = Integer.parseInt(fields[2]);
        altitude = Integer.parseInt(fields[3]);
        direction = Direction.valueOf(fields[4].trim());
        if(fields.length>5){
            military = true;
            foreign = Boolean.parseBoolean(fields[5]);
            followed = Boolean.parseBoolean(fields[6]);
        }
        else {
            military = foreign = false;
        }
    }

    public Pair<Integer, Integer> getPosition() {
        return new Pair<Integer, Integer>(x, y);
    }

    public Pair<Integer, Integer> getNextPosition() {
        return new Pair<Integer, Integer>(x + direction.getDirectionVector().getX(), y + direction.getDirectionVector().getY());
    }

    public String toCsv(){
        return id+","+x+","+y+","+altitude+","+direction+","+foreign+","+followed;
    }

    @Override
    public String toString() {
        return id+","+x+","+y+","+altitude+","+direction+","+military+(foreign ? ",STRANI":",DOMACI");
    }

    public int getId() {
        return id;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public boolean isMilitary() {
        return military;
    }

    public void setMilitary(boolean military) {
        this.military = military;
    }

    public boolean isForeign() {
        return foreign;
    }

    public void setForeign(boolean foreign) {
        this.foreign = foreign;
    }
}
