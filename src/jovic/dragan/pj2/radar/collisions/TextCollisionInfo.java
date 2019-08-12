package jovic.dragan.pj2.radar.collisions;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class TextCollisionInfo implements Serializable {
    private String opis, vrijeme, pozicija;
    private List<Integer> IDs;

    public TextCollisionInfo(CollisionInfo info){
        if (info.getX() != -1) {
            opis = "Sudar " + info.getNumberOfPlanes() + " aviona";
            pozicija = "(x,y,z)=(" + info.getX() + "," + info.getY() + "," + info.getAltitude() + ")";
            vrijeme = LocalDateTime.now().toString();
            IDs = info.getIDs();
        } else {
            opis = pozicija = vrijeme = "";
            IDs = null;
        }
    }

    public List<Integer> getIDs() {
        return IDs;
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
