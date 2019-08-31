package jovic.dragan.pj2.aerospace.generators;

import jovic.dragan.pj2.aerospace.*;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.SimulatorPreferences;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

public class Spawner {

    private Thread spawningThread;
    private SpawningRunnable spawningRunnable;
    private Queue<AerospaceObject> spawnQueue;

    public Spawner(Aerospace aerospace) {
        spawnQueue = new ConcurrentLinkedQueue<>();
        spawningRunnable = new SpawningRunnable(aerospace, spawnQueue);
        spawningThread = new Thread(spawningRunnable);
        spawningThread.start();
    }

    public void enqueueSpawn(AerospaceObject ao) {
        spawnQueue.add(ao);
    }

    public synchronized void setPaused(boolean value){
        spawningRunnable.setPaused(value);
    }

}

class SpawningRunnable implements Runnable {

    private boolean paused;
    private Aerospace aerospace;
    private Random rng;
    private RandomPlaneGenerator rpg;
    private Queue<AerospaceObject> spawnQueue;
    private int oldForeign;

    public SpawningRunnable(Aerospace aerospace, Queue<AerospaceObject> spawnQueue) {
        this.paused = false;
        this.spawnQueue = spawnQueue;
        this.aerospace = aerospace;
        oldForeign = aerospace.getPreferences().getForeignMilitary();
        this.rng = new Random();
        rpg = new RandomPlaneGenerator(PassengerPlane.class, TransportHelicopter.class, FirefighterPlane.class,
                FirefighterHelicopter.class, AntiHailRocket.class, PassengerHelicopter.class, TransportPlane.class);
    }

    public synchronized void setPaused(boolean value) {
        this.paused = value;
    }

    private int randomBetween(int min, int max) {
        return rng.nextInt(max - min + 1) + min;
    }

    private AerospaceObject randomObject() {
        boolean invader = false;
        AerospaceObject nextToSpawn = spawnQueue.poll();
        if(nextToSpawn!=null){
            return nextToSpawn;
        }
        SimulatorPreferences preferences = aerospace.getPreferences();
        if (preferences.getForeignMilitary() > oldForeign) {
            oldForeign = preferences.getForeignMilitary();
            System.out.println("Spawner vidi da se povecao broj!");
            invader = true;
        }
        AerospaceObject spawned = null;
        int x = 0, y = 0;
        //bice glupo tipa spawningFrom bude LEFT pa to znaci sa lijeve ivice ide do desne
        Direction spawningFrom = Direction.fromInt(rng.nextInt(4));
        if (spawningFrom == Direction.LEFT || spawningFrom == Direction.RIGHT) {
            x = spawningFrom == Direction.LEFT ? 0 : preferences.getFieldWidth();
            y = rng.nextInt(preferences.getFieldHeight());
        } else {
            x = rng.nextInt(preferences.getFieldWidth());
            y = spawningFrom == Direction.UP ? preferences.getFieldHeight() : 0;
        }
        if (invader) {
            spawned = new FighterPlane(x, y, preferences.getHeightOptions()[Util.randomBetween(0, preferences.getHeightOptions().length - 1)],
                    Util.randomBetween(preferences.getSpeedMin(), preferences.getSpeedMax())
                    , spawningFrom.opposite());
            ((MilitaryAircraft) spawned).setForeign(true);
            System.out.println("Spawned invaders!");
        } else
            spawned = rpg.getRandom(x, y,
                    preferences.getHeightOptions()[Util.randomBetween(0, preferences.getHeightOptions().length - 1)],
                    Util.randomBetween(preferences.getSpeedMin(), preferences.getSpeedMax()),
                    spawningFrom.opposite());
        return spawned;

    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            int minSpawn = aerospace.getPreferences().getSpawnTimeMin(),
                    maxSpawn = aerospace.getPreferences().getSpawnTimeMax();
            AerospaceObject ao = randomObject();
            aerospace.addAerospaceObject(ao);
            try {
                int pauza = Util.randomBetween(minSpawn, maxSpawn);
                Thread.sleep(pauza * 1000);
            } catch (InterruptedException ex) {
                GenericLogger.log(this.getClass(), Level.SEVERE, "Spawner thread prekinut na spavanju!", ex);
            }
        }
    }
}
