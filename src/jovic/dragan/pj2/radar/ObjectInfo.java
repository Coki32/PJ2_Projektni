package jovic.dragan.pj2.radar;

import java.io.Serializable;

public class ObjectInfo implements Serializable {
    private int x,y,altitude;
    private boolean military,foreign;

    public ObjectInfo(String... fields) {
        x = Integer.parseInt(fields[0]);
        y = Integer.parseInt(fields[1]);
        altitude = Integer.parseInt(fields[2]);
        if(fields.length>3){
            military = true;
            foreign = Boolean.parseBoolean(fields[3]);
        }
        else {
            military = foreign = false;
        }
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
