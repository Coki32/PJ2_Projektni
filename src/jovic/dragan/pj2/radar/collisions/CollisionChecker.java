package jovic.dragan.pj2.radar.collisions;

import jovic.dragan.pj2.radar.ObjectInfo;

import java.util.List;

public class CollisionChecker implements Runnable {

    private List<ObjectInfo> infoList;

    public CollisionChecker(List<ObjectInfo> infoList){
        this.infoList = infoList;
    }

    private boolean isCollide(ObjectInfo left, ObjectInfo right){
        return left.getX()==right.getX() && left.getY()==right.getY() && left.getAltitude()==right.getAltitude();
    }

    @Override
    public void run() {
        if (infoList.size() > 1) {
            var i = infoList.iterator();
            while (i.hasNext()) {
                ObjectInfo ith = i.next();
                i.remove();
                var j = infoList.iterator();
                CollisionInfo info = new CollisionInfo(ith.getX(), ith.getY(), ith.getAltitude());
                info.increaseCount();
                while (j.hasNext()) {
                    ObjectInfo jth = j.next();
                    if (isCollide(ith, jth)) {
                        j.remove();
                        info.increaseCount();
                    }
                }
                if (info.getNumberOfPlanes() >= 2) {
                    CollisionLogger.logCollision(info);
                }
            }
        }
    }
}
