package jovic.dragan.pj2.aerospace.generators;

import jovic.dragan.pj2.aerospace.*;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.preferences.PreferenceWatcher;
import jovic.dragan.pj2.preferences.SimulatorPreferences;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.util.Random;
import java.util.logging.Level;

public class Spawner {

    private Thread spawningThread;
    private SpawningRunnable spawningRunnable;

    public Spawner(SimulatorPreferences properties, Aerospace aerospace){
        spawningRunnable = new SpawningRunnable(properties, aerospace);
        spawningThread = new Thread(spawningRunnable);
        spawningThread.start();
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
    private SimulatorPreferences preferences;
    private PreferenceWatcher<SimulatorPreferences> watcher;

    public SpawningRunnable(SimulatorPreferences preferences, Aerospace aerospace) {
        this.paused = false;
        this.preferences = preferences;
        this.aerospace = aerospace;
        this.rng = new Random();
        rpg = new RandomPlaneGenerator(PassengerPlane.class, TransportHelicopter.class, FirefighterPlane.class, FirefighterHelicopter.class,
                AntiHailRocket.class, PassengerHelicopter.class);
        watcher = new PreferenceWatcher<>(preferences, Constants.SIMULATOR_PROPERTIES_FILENAME, SimulatorPreferences::load);
        watcher.start();
    }

    public synchronized void setPaused(boolean value) {
        this.paused = value;
    }

    private int randomBetween(int min, int max) {
        return rng.nextInt(max - min + 1) + min;
    }

    private AerospaceObject randomObject() {
        boolean invader = false;
        if (watcher.isChanged()) {
            System.out.println("Ucitano u spawneru");
            int oldForeign = preferences.getForeignMilitary();
            preferences = watcher.getOriginal();
            int newForeign = preferences.getForeignMilitary();
            watcher.setChanged(false);
            invader = newForeign > oldForeign;
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
            int minSpawn = preferences.getSpawnTimeMin(),
                    maxSpawn = preferences.getSpawnTimeMax();
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
