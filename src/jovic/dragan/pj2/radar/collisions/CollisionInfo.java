package jovic.dragan.pj2.radar.collisions;

import java.util.ArrayList;
import java.util.List;

public class CollisionInfo  {

    private int x, y, altitude, numberOfPlanes;
    private List<Integer> IDs;
    public CollisionInfo(int x, int y, int altitude) {
        this.x = x;
        this.y = y;
        this.altitude = altitude;
        this.numberOfPlanes = 0;
        IDs = new ArrayList<>();
    }


    public TextCollisionInfo getSerializible(){
        return new TextCollisionInfo(this);
    }

    public void addPlane(int id) {
        numberOfPlanes++;
        IDs.add(id);
    }

    public List<Integer> getIDs() {
        return IDs;
    }

    public int getNumberOfPlanes(){
        return numberOfPlanes;
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
}
