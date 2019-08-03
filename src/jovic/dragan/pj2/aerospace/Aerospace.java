package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.util.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Aerospace {

    private int height, width, maxHeight, heightDivs;
    private Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> map;
    private UpdatingTask timerTask;
    private Timer updatingTimer;
    private boolean flightAllowed = true;

    public Aerospace(int height, int width, int maxHeight, int heightDivs) {
        this.height = height;
        this.width = width;
        this.maxHeight = maxHeight;
        this.heightDivs = heightDivs;
        map = new ConcurrentHashMap<>();
        updatingTimer = new Timer("positionUpdater", false);
        timerTask = new UpdatingTask(map);
        //start();
    }

    public synchronized void banFlight() {
        if (flightAllowed) {
            System.out.println("Postavljan zabranu leta na true");
            timerTask.setPaused(true);
            flightAllowed = false;
            System.out.println("Postavljeno!");
        }
    }

    public synchronized void resumeFlight() {
        if (!flightAllowed) {
            System.out.println("Postavljan zabranu leta na false");
            timerTask.setPaused(false);
            flightAllowed = true;
            System.out.println("Postavljeno");
        }
    }

    public void start() {
        updatingTimer.schedule(timerTask, 0, 1000);
    }

    public void addAerospaceObject(AerospaceObject object) {
        if(object!=null) {
            int x = object.getX(), y = object.getY();
            if (!map.containsKey(x)) {
                map.put(x, new ConcurrentHashMap<>());
                map.get(x).put(y, new ConcurrentLinkedDeque<>());
            } else if (!map.get(x).containsKey(y)) {
                map.get(x).put(y, new ConcurrentLinkedDeque<>());
            }
            if (map.get(x).get(y).stream().anyMatch((ao) -> ao.getAltitude() == object.getAltitude())) {
                System.out.println("Sudar pri dodavanju, malo vjerovatno.... nece se dodati");
                return;
            }
            map.get(x).get(y).add(object);
        }
    }
}

class UpdatingTask extends TimerTask {

    private Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> map;

    private boolean paused = false;

    UpdatingTask(Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> map) {
        this.map = map;
    }

    synchronized void setPaused(boolean value) {
        paused = value;
    }

    @Override
    public synchronized void run() {
        long start = System.currentTimeMillis();
        int count = 0;
        if (!paused) {
            //Iterator<Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> mapsIter = map.values().iterator();
            var mapsIter = map.values().iterator();
            while (mapsIter.hasNext()) {
                //Map<Integer, ConcurrentLinkedDeque<AerospaceObject>> subMap = mapsIter.next();
                var subMap = mapsIter.next();
                //Iterator<ConcurrentLinkedDeque<AerospaceObject>> subMapIter = subMap.values().iterator();
                var subMapIter = subMap.values().iterator();
                while (subMapIter.hasNext()) {
                    //ConcurrentLinkedDeque<AerospaceObject> list = subMapIter.next();
                    var list = subMapIter.next();
                    //Iterator<AerospaceObject> listIter = list.iterator();
                    var listIter = list.iterator();
                    while (listIter.hasNext()) {
                        AerospaceObject ao = listIter.next();
                        int oldX = ao.getX(), oldY = ao.getY();
                        Pair<Integer, Integer> nextPosition = ao.getNextPosition();
                        if (oldX != nextPosition.getFirst() || oldY != nextPosition.getSecond()) {
                            ao.setSkip(true);
                            listIter.remove();
                            count++;
                            if (!map.containsKey(ao.getX()))
                                map.put(ao.getX(), new ConcurrentHashMap<>());
                            if (!map.get(ao.getX()).containsKey(ao.getY()))
                                map.get(ao.getX()).put(ao.getY(), new ConcurrentLinkedDeque<>());
                            map.get(ao.getX()).get(ao.getY()).add(ao);
                        }
                    }
                }
            }
            map.values().parallelStream().forEach((yMap) -> yMap.values().forEach(q -> q.forEach(ao -> ao.setSkip(false))));
            long end = System.currentTimeMillis();
            System.out.println(System.currentTimeMillis() + ": Update gotov za " + (end - start) + "ms (" + count + " aviona)");
            //System.out.println(map);
        } else
            System.out.println("Pauziran let...");
    }
}
