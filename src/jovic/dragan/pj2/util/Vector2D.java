package jovic.dragan.pj2.util;

import java.io.Serializable;

public class Vector2D implements Serializable {
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this(0,0);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Vector2D))//nema potrebe za null jer ovo pokriva i to
            return false;
        else
            return ((Vector2D) obj).x==x && ((Vector2D) obj).y==y;
    }

    @Override
    public String toString() {
        return "(X,Y)=("+x+","+y+")";
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

    private int x,y;

}