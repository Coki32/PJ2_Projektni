package jovic.dragan.pj2.radar.collisions;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TextCollisionInfo implements Serializable {
    private String opis, vrijeme, pozicija;
    public TextCollisionInfo(CollisionInfo info){
        opis = "Sudar " + info.getNumberOfPlanes()+ " aviona";
        pozicija = "(x,y,z)=("+info.getX()+","+info.getY()+","+info.getAltitude()+")";
        vrijeme = LocalDateTime.now().toString();
    }

    @Override
    public String toString() {
        return opis+"\n"+vrijeme+"\n"+pozicija;
    }

    public String getOpis() {
        return opis;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public String getPozicija() {
        return pozicija;
    }
}
