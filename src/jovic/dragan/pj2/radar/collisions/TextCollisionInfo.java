package jovic.dragan.pj2.radar.collisions;

import java.io.Serializable;
import java.time.LocalDateTime;

class TextCollisionInfo implements Serializable {
    private String opis, vrijeme, pozicija;
    public TextCollisionInfo(CollisionInfo info){
        opis = "Sudar " + info.getNumberOfPlanes()+ " aviona";
        pozicija = "(x,y,z)=("+info.getX()+","+info.getY()+","+info.getAltitude()+")";
        vrijeme = LocalDateTime.now().toString();
    }
}
