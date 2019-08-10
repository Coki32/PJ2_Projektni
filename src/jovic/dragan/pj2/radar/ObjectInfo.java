package jovic.dragan.pj2.radar;

import jovic.dragan.pj2.util.Direction;

import java.io.Serializable;

public class ObjectInfo implements Serializable {
    private int x,y,altitude;
    private Direction direction;
    private boolean military,foreign, followed;

    public ObjectInfo(String... fields) {
        x = Integer.parseInt(fields[0]);
        y = Integer.parseInt(fields[1]);
        altitude = Integer.parseInt(fields[2]);
        direction = Direction.valueOf(fields[3].trim());
        if(fields.length>4){
            military = true;
            foreign = Boolean.parseBoolean(fields[4]);
            followed = Boolean.parseBoolean(fields[5]);
        }
        else {
            military = foreign = false;
        }
    }

    @Override
    public String toString() {
        return x+","+y+","+altitude+","+direction+","+military+(foreign ? ",STRANI":",DOMACI");
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
