package jovic.dragan.pj2.radar.collisions;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class TextCollisionInfo implements Serializable {
    private String description, time, position;
    private List<Integer> IDs;

    public TextCollisionInfo(CollisionInfo info){
        if (info.getX() != -1) {
            description = "Sudar " + info.getNumberOfPlanes() + " aviona";
            position = "(x,y,z)=(" + info.getX() + "," + info.getY() + "," + info.getAltitude() + ")";
            time = LocalDateTime.now().toString();
            IDs = info.getIDs();
        } else {
            description = position = time = "";
            IDs = null;
        }
    }

    public List<Integer> getIDs() {
        return IDs;
    }

    @Override
    public String toString() {
        return description + "\n" + time + "\n" + position;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getPosition() {
        return position;
    }
}
