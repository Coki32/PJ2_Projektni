package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.preferences.PreferenceWatcher;
import jovic.dragan.pj2.preferences.RadarPreferences;
import jovic.dragan.pj2.preferences.SimulatorPreferences;
import jovic.dragan.pj2.radar.RadarExporter;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Pair;
import jovic.dragan.pj2.util.Util;

import java.util.*;
import java.util.Timer;
import java.util.concurrent.*;

public class Aerospace {

    private Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> map;
    private UpdatingTask timerTask;
    private Timer updatingTimer;
    private boolean flightAllowed = true;
    private SimulatorPreferences preferences;
    private PreferenceWatcher<SimulatorPreferences> watcher;

    public Aerospace(SimulatorPreferences preferences) {
        map = new ConcurrentHashMap<>();
        updatingTimer = new Timer("positionUpdater", false);

        this.preferences = preferences;
        watcher = new PreferenceWatcher<>(preferences, Constants.PREFERENCES_FOLDERNAME,SimulatorPreferences::load);

        timerTask = new UpdatingTask(map,watcher);
        watcher.start();
    }

    private int minIdx(int[] ints){
    return 0;
    }

    public synchronized void banFlight() {
        if (flightAllowed) {
            int mapWidth = preferences.getFieldWidth();
            int mapHeight = preferences.getFieldHeight();
            System.out.println("Postavljan zabranu svi izlaze najkraicm putem");
            Integer[] exit = new Integer[4];
//            synchronized (map) {
                map.values().parallelStream().forEach(yMap -> yMap.values().forEach(q -> q.forEach(ao -> {
                    int x = ao.getX();
                    int y = ao.getY();
                    exit[0] = mapHeight-y; //Distance to upper edge
                    exit[1] = y;//distance to the bottom edge
                    exit[2] = x;//distance to left edge
                    exit[3] = mapWidth-x;//distance to right edge
                    Direction newDirection = Direction.fromInt(Util.minIdx(exit));
                    System.out.println(ao+" ide ka " + newDirection);
                    ao.setDirection(newDirection);
                })));
            //}
            flightAllowed = false;
            System.out.println("Postavljeno!");
        }
    }

    public synchronized void resumeFlight() {
        if (!flightAllowed) {
            System.out.println("Postavljan zabranu leta na false");
            flightAllowed = true;
        }
    }

    public void start() {
        //TODO: dodaj u preference update frequency
        updatingTimer.schedule(timerTask, 0, 1000);
    }

    public void addAerospaceObject(AerospaceObject object) {
        if(object!=null && flightAllowed) {
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
        else
            System.out.println("Zabranjen let, ne dodajem trazeni objekt!");
    }
}

class UpdatingTask extends TimerTask {

    private Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> map;
    private PreferenceWatcher<SimulatorPreferences> watcher;
    private SimulatorPreferences preferences;

    private RadarPreferences radarPreferences;
    private PreferenceWatcher<RadarPreferences> radarPreferencesPreferenceWatcher;

    private RadarExporter exporter;

    UpdatingTask(Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> map, PreferenceWatcher<SimulatorPreferences> watcher) {
        this.map = map;
        this.watcher = watcher;
        preferences = watcher.getOriginal();
        this.radarPreferences = RadarPreferences.load();
        this.radarPreferencesPreferenceWatcher = new PreferenceWatcher<>(radarPreferences,Constants.RADAR_PROPERTIES_FULL_NAME, RadarPreferences::load);

        exporter = new RadarExporter(map,radarPreferences.getFileUpdateTime());
        exporter.start();
    }

    private boolean isInsideOfMap(int x, int y, int width, int height){
        return x<=width && y<=height && x>=0 && y>=0;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        int count = 0;
        if (watcher.isChanged()) {
            preferences = watcher.getOriginal();
            watcher.setChanged(false);
            System.out.println("Ucitan novi pref u aerospace!");
        }
        int mapWidth = preferences.getFieldWidth(), mapHeight = preferences.getFieldHeight();
        var mapsIter = map.values().iterator();
        while (mapsIter.hasNext()) {
            var subMap = mapsIter.next();
            var subMapIter = subMap.values().iterator();
            while (subMapIter.hasNext()) {
                var list = subMapIter.next();
                var listIter = list.iterator();
                while (listIter.hasNext()) {
                    AerospaceObject ao = listIter.next();
                    int oldX = ao.getX(), oldY = ao.getY();
                    Pair<Integer, Integer> nextPosition = ao.getNextPosition();
                    if(!isInsideOfMap(nextPosition.getFirst(), nextPosition.getSecond(),mapWidth,mapHeight))
                    {
                        listIter.remove();
                        System.out.println(ao+" izbacen iz simulacije...");
                    }
                    else if (oldX != nextPosition.getFirst() || oldY != nextPosition.getSecond()) {
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
        System.out.println(map);
        long end = System.currentTimeMillis();
        //System.out.println(System.currentTimeMillis() + ": Update gotov za " + (end - start) + "ms (" + count + " aviona)");
    }
}
