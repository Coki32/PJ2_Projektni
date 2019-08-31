package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Military;
import jovic.dragan.pj2.aerospace.generators.Spawner;
import jovic.dragan.pj2.aerospace.handlers.CollisionHandler;
import jovic.dragan.pj2.aerospace.handlers.InvasionHandler;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.preferences.SimulatorPreferences;
import jovic.dragan.pj2.radar.RadarExporter;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Pair;
import jovic.dragan.pj2.util.Util;
import jovic.dragan.pj2.util.Watcher;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;

public class Aerospace {

    private Map<Integer, Map<Integer, Queue<AerospaceObject>>> map;
    private UpdatingRunnable updatingRunnable;
    private Thread updatingThread;
    private boolean flightAllowed = true, guiFlightAllowed = true;
    private SimulatorPreferences preferences;
    private boolean running = false;
    private Spawner spawner;

    public Aerospace(SimulatorPreferences preferences) {
        map = new ConcurrentHashMap<>();

        this.preferences = preferences;

        Util.createFolderIfNotExists(Constants.ALERTS_FOLDER_PATH);
        Util.createFolderIfNotExists(Constants.EVENTS_FOLDER_PATH);
        try {
            Watcher preferenceWatcher = new Watcher(Constants.PREFERENCES_FOLDERNAME, StandardWatchEventKinds.ENTRY_MODIFY);
            Watcher collisionWatcher = new Watcher(Constants.ALERTS_FOLDER_PATH, StandardWatchEventKinds.ENTRY_MODIFY);
            Watcher invasionWatcher = new Watcher(Constants.EVENTS_FOLDER_PATH, StandardWatchEventKinds.ENTRY_MODIFY);

            collisionWatcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY,new CollisionHandler(this));
            invasionWatcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY,new InvasionHandler(this));
            preferenceWatcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY, ev -> {
                if (((WatchEvent<Path>) ev).context().toFile().toString().endsWith(Constants.SIMULATOR_PROPERTIES_FILENAME)) {
                    this.reloadPreferences();
                    System.out.println("Novi preferences ucitan u Aerospace, samim tim i spawner!");
                }
            });

            collisionWatcher.start();
            invasionWatcher.start();
            preferenceWatcher.start();
        }
        catch (IOException ex){
            GenericLogger.log(this.getClass(), Level.SEVERE,"Could not register folder watchers for collisions and invasions, those will not be detected",ex);
        }
        updatingRunnable = new UpdatingRunnable(map, this);
        updatingThread = new Thread(updatingRunnable, "updating task thread");
        spawner = new Spawner(this);
    }

    private void reloadPreferences() {
        preferences = SimulatorPreferences.load();
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

    public synchronized void allowFlight() {
        if (!flightAllowed) {
            System.out.println("Postavljan zabranu leta na false");
            flightAllowed = true;
        }
    }

    public synchronized void guiBanFlight() {
        if (guiFlightAllowed) {
            guiFlightAllowed = false;
            banFlight();
        }
    }

    public synchronized void guiAllowFlight() {
        if (!guiFlightAllowed) {
            guiFlightAllowed = true;
        }
    }

    public boolean isFlightAllowed() {
        return flightAllowed;
    }

    public void start() {
        if(!running) {
            updatingThread.start();
            running = true;
        }
    }

    public SimulatorPreferences getPreferences(){
        return preferences;
    }

    public Map<Integer, Map<Integer, Queue<AerospaceObject>>> getMap() {
        return map;
    }

    public Spawner getSpawner(){
        return spawner;
    }

    public void addAerospaceObject(AerospaceObject object) {
        if (object != null && ((flightAllowed && guiFlightAllowed) || object instanceof Military)) {
            int x = object.getX(), y = object.getY();
            if (!map.containsKey(x)) {
                map.put(x, new ConcurrentHashMap<>());
                map.get(x).put(y, new ConcurrentLinkedDeque<>());
            } else if (!map.get(x).containsKey(y)) {
                map.get(x).put(y, new ConcurrentLinkedDeque<>());
            }
            if(object instanceof MilitaryAircraft){
                MilitaryAircraft ma = (MilitaryAircraft) object;
                if (ma instanceof FighterPlane) {
                    FighterPlane fp = (FighterPlane) ma;
                    if (!fp.isForeign() && fp.getFollowing() != null)
                        fp.follow(fp.getFollowing());
                }
            }
            map.get(x).get(y).add(object);
        }
    }
}

class UpdatingRunnable implements Runnable {

    private Map<Integer, Map<Integer, Queue<AerospaceObject>>> map;
    private Aerospace aerospace;
    private RadarExporter exporter;

    UpdatingRunnable(Map<Integer, Map<Integer, Queue<AerospaceObject>>> map, Aerospace aerospace) {
        this.map = map;
        this.aerospace = aerospace;
        //RadarPreferences radarPreferences = RadarPreferences.load();

        exporter = new RadarExporter(map);
        exporter.start();
    }

    private boolean isInsideOfMap(int x, int y, int width, int height) {
        return x <= width && y <= height && x >= 0 && y >= 0;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            long start = System.currentTimeMillis();
            int mapWidth = aerospace.getPreferences().getFieldWidth(), mapHeight = aerospace.getPreferences().getFieldHeight();
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
                        if ((ao instanceof MilitaryAircraft) && !((MilitaryAircraft) ao).isForeign() && ((MilitaryAircraft) ao).getFollowing() != null) {
                            MilitaryAircraft aoMil = (MilitaryAircraft) ao;
                            int x1 = aoMil.getFollowing().getX(), y1 = aoMil.getFollowing().getY();
                            if (Math.sqrt((x1 - oldX) * (x1 - oldX) + (y1 - oldY) * (y1 - oldY)) < 1.1) {
                                System.out.println("Domaca letjelica unistava stranu!");
                                map.values().forEach(yMap -> yMap.values().forEach(q -> q.removeIf(qo -> qo.getId() == ((MilitaryAircraft) ao).getFollowing().getId())));
                                ((MilitaryAircraft) ao).setFollowing(null);
                            }
                        }
                        if (!isInsideOfMap(nextPosition.getFirst(), nextPosition.getSecond(), mapWidth, mapHeight)) {
                            listIter.remove();
                        } else if (oldX != nextPosition.getFirst() || oldY != nextPosition.getSecond()) {
                            //ao.setSkip(true);
                            ao.setX(nextPosition.getFirst());
                            ao.setY(nextPosition.getSecond());
                            listIter.remove();
                            if (!map.containsKey(ao.getX()))
                                map.put(ao.getX(), new ConcurrentHashMap<>());
                            if (!map.get(ao.getX()).containsKey(ao.getY()))
                                map.get(ao.getX()).put(ao.getY(), new ConcurrentLinkedDeque<>());
                            map.get(ao.getX()).get(ao.getY()).add(ao);
                        }
                    }
                }
            }
            //map.values().parallelStream().forEach((yMap) -> yMap.values().forEach(q -> q.forEach(ao -> ao.setSkip(false))));
            if (!aerospace.isFlightAllowed() && map.values().parallelStream().allMatch(yMap -> yMap.values().stream().allMatch(q -> q.stream().allMatch(
                    ao -> !(ao instanceof Military) || !((MilitaryAircraft) ao).isForeign()))))
                aerospace.allowFlight();
            long end = System.currentTimeMillis();
            try {
                Thread.sleep(aerospace.getPreferences().getSimulatorUpdatePeriod());
            } catch (InterruptedException ex) {
                GenericLogger.log(this.getClass(), ex);
            }
        }
    }
}
