package jovic.dragan.pj2.radar.collisions;

import jovic.dragan.pj2.radar.ObjectInfo;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class CollisionChecker implements Runnable {
    private Queue<ObjectInfo> infoList;

    public CollisionChecker(Queue<ObjectInfo> infoList) {
        this.infoList = infoList;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        Set<Integer> xS = new HashSet<>(), yS = new HashSet<>(), altS = new HashSet<>();
        for (ObjectInfo info : infoList) {
            xS.add(info.getX());
            yS.add(info.getY());
            altS.add(info.getAltitude());
        }
        for (int x : xS) {
            for (int y : yS) {
                for (int a : altS) {
                    var collided = infoList.stream().filter(ao -> ao.getX() == x && ao.getY() == y && ao.getAltitude() == a)
                            .collect(Collectors.toList());
                    if (collided.size() > 1) {
                        CollisionInfo collisionInfo = new CollisionInfo(x, y, a);
                        for (ObjectInfo info : collided)
                            collisionInfo.addPlane(info.getId());
                        System.out.println("Sudareno " + collisionInfo.getNumberOfPlanes() + " aviona! " + collisionInfo.getSerializible().getPozicija());
                        CollisionLogger.logCollision(collisionInfo);
                    }
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Provjerio sudare za " + (end - start) + "ms - " + infoList.size() + " aviona");
    }
}
