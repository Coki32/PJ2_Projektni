package jovic.dragan.pj2.radar;

import jovic.dragan.pj2.util.Direction;

import java.awt.*;
import java.io.Serializable;

public class ObjectInfo implements Serializable {

    private int x,y,altitude, id;
    private Direction direction;
    private boolean military,foreign, followed;
    private Color drawingColor;

    public ObjectInfo(String... fields) {
        id = Integer.parseInt(fields[0]);
        x = Integer.parseInt(fields[1]);
        y = Integer.parseInt(fields[2]);
        altitude = Integer.parseInt(fields[3]);
        direction = Direction.valueOf(fields[4].trim());
        drawingColor = new Color(Integer.parseInt(fields[5].trim()));
        if (fields.length > 6) {
            military = true;
            foreign = Boolean.parseBoolean(fields[6]);
            followed = Boolean.parseBoolean(fields[7]);
        }
        else {
            military = foreign = false;
        }
    }

    public Color getDrawingColor() {
        return drawingColor;
    }

    public String toCsv(){
        return id + "," + x + "," + y + "," + altitude + "," + direction + "," + drawingColor.getRGB() + "," + foreign + "," + followed;
    }

    @Override
    public String toString() {
        return id + "," + x + "," + y + "," + altitude + "," + direction + "," + drawingColor.getRGB() + "," + military + (foreign ? ",STRANI" : ",DOMACI");
    }

    public int getId() {
        return id;
    }

    public boolean isFollowed() {
        return followed;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAltitude() {
        return altitude;
    }

    public boolean isMilitary() {
        return military;
    }

    public boolean isForeign() {
        return foreign;
    }
}
