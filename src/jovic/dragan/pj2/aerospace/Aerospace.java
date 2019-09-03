package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.aerospace.generators.Spawner;
import jovic.dragan.pj2.aerospace.handlers.CollisionHandler;
import jovic.dragan.pj2.aerospace.handlers.InvasionHandler;
import jovic.dragan.pj2.interfaces.Military;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.preferences.SimulatorPreferences;
import jovic.dragan.pj2.util.Direction;
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

    private final Map<Integer, Map<Integer, Queue<AerospaceObject>>> map;
    private Thread updatingThread;
    private boolean flightAllowed = true, guiFlightAllowed = true;
    private SimulatorPreferences preferences;
    private boolean running = false;
    private Spawner spawner;
    private final Object synchronizationObject = new Object();

    public Aerospace(SimulatorPreferences preferences) {
        map = new ConcurrentHashMap<>(preferences.getFieldWidth() / 2);

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
                    //System.out.println("Novi preferences ucitan u Aerospace, samim tim i spawner!");
                }
            });

            collisionWatcher.start();
            invasionWatcher.start();
            preferenceWatcher.start();
        }
        catch (IOException ex){
            GenericLogger.log(this.getClass(), Level.SEVERE,"Could not register folder watchers for collisions and invasions, those will not be detected",ex);
        }
        AerospaceUpdatingRunnable updatingRunnable = new AerospaceUpdatingRunnable(map, this);
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
            //System.out.println("Postavljan zabranu svi izlaze najkraicm putem");
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
            //System.out.println("Postavljeno!");
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