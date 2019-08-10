package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Military;
import jovic.dragan.pj2.aerospace.generators.Spawner;
import jovic.dragan.pj2.aerospace.handlers.CollisionHandler;
import jovic.dragan.pj2.aerospace.handlers.InvasionHandler;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.preferences.PreferenceWatcher;
import jovic.dragan.pj2.preferences.RadarPreferences;
import jovic.dragan.pj2.preferences.SimulatorPreferences;
import jovic.dragan.pj2.radar.RadarExporter;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Pair;
import jovic.dragan.pj2.util.Util;
import jovic.dragan.pj2.util.Watcher;

import java.io.IOException;
import java.nio.file.StandardWatchEventKinds;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.*;
import java.util.logging.Level;

public class Aerospace {

    private Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> map;
    private UpdatingTask timerTask;
    private Timer updatingTimer;
    private boolean flightAllowed = true;
    private SimulatorPreferences preferences;
    private PreferenceWatcher<SimulatorPreferences> watcher;
    private boolean running = false;
//    Spawner spawner = new Spawner(properties, aerospace);
    private Spawner spawner;

    private Watcher collisionWatcher, invasionWatcher;

    public Aerospace(SimulatorPreferences preferences) {
        map = new ConcurrentHashMap<>();
        updatingTimer = new Timer("positionUpdater", false);

        this.preferences = preferences;
        watcher = new PreferenceWatcher<>(preferences, Constants.SIMULATOR_PROPERTIES_FILENAME,SimulatorPreferences::load);
        Util.createFolderIfNotExists(Constants.ALERTS_FOLDER_PATH);
        Util.createFolderIfNotExists(Constants.EVENTS_FOLDER_PATH);
        try {
            collisionWatcher = new Watcher(Constants.ALERTS_FOLDER_PATH, StandardWatchEventKinds.ENTRY_MODIFY);
            invasionWatcher = new Watcher(Constants.EVENTS_FOLDER_PATH, StandardWatchEventKinds.ENTRY_MODIFY);
            collisionWatcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY,new CollisionHandler(this));
            invasionWatcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY,new InvasionHandler(this));
            collisionWatcher.start();
            invasionWatcher.start();
        }
        catch (IOException ex){
            GenericLogger.log(this.getClass(), Level.SEVERE,"Could not register folder watchers for collisions and invasions, those will not be detected",ex);
        }
        timerTask = new UpdatingTask(map,watcher, this);
        watcher.start();
        spawner = new Spawner(preferences,this);
    }

    public synchronized void banFlight() {
        if (flightAllowed) {
            int mapWidth = preferences.getFieldWidth();
            int mapHeight = preferences.getFieldHeight();
            System.out.println("Postavljan zabranu svi izlaze najkraicm putem");
            Integer[] exit = new Integer[4];
            map.values().parallelStream().forEach(yMap -> yMap.values().forEach(q -> q.stream().filter(o->!(o instanceof Military)).forEach(ao -> {
                int x = ao.getX();
                int y = ao.getY();
                exit[0] = mapHeight-y; //Distance to upper edge
                exit[1] = y;//distance to the bottom edge
                exit[2] = x;//distance to left edge
                exit[3] = mapWidth-x;//distance to right edge
                Direction newDirection = Direction.fromInt(Util.minIdx(exit));
                ao.setDirection(newDirection);
            })));
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

    public boolean isFlightAllowed() {
        return flightAllowed;
    }

    public void start() {
        if(!running) {
            updatingTimer.schedule(timerTask, 0, preferences.getSimulatorUpdatePeriod());
            running = true;
        }
    }

    public SimulatorPreferences getPreferences(){
        return preferences;
    }

    public void clearPosition(int x, int y, int altitude){
        if(map.containsKey(x) && map.get(x).containsKey(y))
            map.get(x).get(y).removeIf(ao->ao.getAltitude()==altitude);
    }

    public ConcurrentLinkedDeque<AerospaceObject> getField(int x, int y){
        if (map.containsKey(x) && map.get(x).containsKey(y))
            return map.get(x).get(y);
        else
            return  null;
    }

    public void addAerospaceObject(AerospaceObject object) {
        if(object!=null && (flightAllowed || object instanceof Military )) {
            int x = object.getX(), y = object.getY();
            if (!map.containsKey(x)) {
                map.put(x, new ConcurrentHashMap<>());
                map.get(x).put(y, new ConcurrentLinkedDeque<>());
            } else if (!map.get(x).containsKey(y)) {
                map.get(x).put(y, new ConcurrentLinkedDeque<>());
            }
            if(object instanceof MilitaryAircraft){
                System.out.println("Dodata vojska u prostor, i to "+ (((MilitaryAircraft) object).isForeign() ? "strana":"domaca"));
            }
            map.get(x).get(y).add(object);
        }
    }
}

class UpdatingTask extends TimerTask {

    private Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> map;
    private PreferenceWatcher<SimulatorPreferences> watcher;
    private SimulatorPreferences preferences;
    private Aerospace aerospace;
    private RadarExporter exporter;

    UpdatingTask(Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> map, PreferenceWatcher<SimulatorPreferences> watcher, Aerospace aerospace) {
        this.map = map;
        this.watcher = watcher;
        this.aerospace = aerospace;
        preferences = watcher.getOriginal();
        RadarPreferences radarPreferences = RadarPreferences.load();

        exporter = new RadarExporter(map);
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
        if(!aerospace.isFlightAllowed() && map.values().parallelStream().allMatch(yMap->yMap.values().stream().allMatch(q->q.stream().allMatch(
                ao-> !(ao instanceof Military) || !((MilitaryAircraft)ao).isForeign()))))
            aerospace.resumeFlight();
        long end = System.currentTimeMillis();
    }
}
