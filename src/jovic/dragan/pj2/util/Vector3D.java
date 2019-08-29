package jovic.dragan.pj2.util;

/**
 * 
 */
public class Vector3D {


    public Vector3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3D(){

    }

    public void add(Vector2D other){
        this.x+=other.getX();
        this.y+=other.getY();
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

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    private int x,y,z;


}