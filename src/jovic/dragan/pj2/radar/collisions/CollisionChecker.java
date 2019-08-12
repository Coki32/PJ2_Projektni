package jovic.dragan.pj2.radar.collisions;

import jovic.dragan.pj2.radar.ObjectInfo;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CollisionChecker implements Runnable {
    private static ReentrantLock lock = new ReentrantLock();
    private Queue<ObjectInfo> infoList;

    public CollisionChecker(Queue<ObjectInfo> infoList) {
        this.infoList = infoList;
    }

    private boolean isCollide(ObjectInfo left, ObjectInfo right) {
        return left.getX() == right.getX() && left.getY() == right.getY() && left.getAltitude() == right.getAltitude();
    }

    @Override
    public void run() {
        lock.lock();
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
                    var sudareni = infoList.stream().filter(ao -> ao.getX() == x && ao.getY() == y && ao.getAltitude() == a)
                            .collect(Collectors.toList());
                    if (sudareni.size() > 1) {
                        CollisionInfo collisionInfo = new CollisionInfo(x, y, a);
                        for (ObjectInfo info : sudareni)
                            collisionInfo.addPlane(info.getId());
                        System.out.println("Sudareno " + collisionInfo.getNumberOfPlanes() + " aviona! " + collisionInfo.getSerializible().getPozicija());
                        CollisionLogger.logCollision(collisionInfo);
                    }
                }
            }
        }
        long end = System.currentTimeMillis();
        //System.out.println("Provjerio sudare za " + (end-start)+"ms");
        lock.unlock();
        /*Set<Integer> provjerenePozicije = new HashSet<>();
        if (infoList.size() > 1) {
            var i = infoList.iterator();
            while (i.hasNext()) {
                ObjectInfo ith = i.next();
                i.remove();
                var j = infoList.iterator();
                CollisionInfo info = new CollisionInfo(ith.getX(), ith.getY(), ith.getAltitude());
                info.addPlane(ith.getId());
                while (j.hasNext()) {
                    ObjectInfo jth = j.next();
                    boolean collisionCourse = ith.getDirection() == jth.getDirection().opposite();
                    boolean crashOnUpdateBoth = ith.getNextPosition().equals(jth.getNextPosition());
                    boolean crashOnUpdateOne = ith.getPosition().equals(jth.getNextPosition()) || ith.getNextPosition().equals(jth.getPosition());
                    boolean matchingAltitudes = ith.getAltitude() == jth.getAltitude();
                    if (isCollide(ith, jth) || (collisionCourse && (crashOnUpdateBoth || crashOnUpdateOne) && matchingAltitudes)) {
                        j.remove();
                        info.addPlane(jth.getId());
                    }
                }
                if (info.getNumberOfPlanes() >= 2) {
                    System.out.println(info.getSerializible());
                    CollisionLogger.logCollision(info);
                }
            }

        }*/
    }
}
