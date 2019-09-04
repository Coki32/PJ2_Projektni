package jovic.dragan.pj2.aerospace.generators;

import jovic.dragan.pj2.aerospace.*;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.SimulatorPreferences;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;

class SpawningRunnable implements Runnable {

    private Aerospace aerospace;
    private Random rng;
    private RandomPlaneGenerator rpg;
    private Queue<AerospaceObject> spawnQueue;
    private int oldForeign;

    SpawningRunnable(Aerospace aerospace, Queue<AerospaceObject> spawnQueue) {
        this.spawnQueue = spawnQueue;
        this.aerospace = aerospace;
        oldForeign = aerospace.getPreferences().getForeignMilitary();
        this.rng = new Random();
        rpg = new RandomPlaneGenerator(PassengerPlane.class, TransportHelicopter.class, FirefighterPlane.class,
                FirefighterHelicopter.class, AntiHailRocket.class, PassengerHelicopter.class, TransportPlane.class,
                UnmannedAircraft.class);
    }

    private AerospaceObject randomObject() {
        boolean invader = false;
        AerospaceObject nextToSpawn = spawnQueue.poll();
        if (nextToSpawn != null) {
            return nextToSpawn;
        }

        SimulatorPreferences preferences = aerospace.getPreferences();

        if (preferences.getForeignMilitary() > oldForeign) {
            oldForeign = preferences.getForeignMilitary();
            invader = true;
        }
        AerospaceObject spawned;

        int x, y;
        int altitude = preferences.getHeightOptions()[Util.randomBetween(0, preferences.getHeightOptions().length - 1)];
        int speed = Util.randomBetween(preferences.getSpeedMin(), preferences.getSpeedMax());

        Direction spawningFrom = Direction.fromInt(rng.nextInt(4));
        if (spawningFrom == Direction.LEFT || spawningFrom == Direction.RIGHT) {
            x = spawningFrom == Direction.LEFT ? 0 : preferences.getFieldWidth();
            y = rng.nextInt(preferences.getFieldHeight());
        } else {
            x = rng.nextInt(preferences.getFieldWidth());
            y = spawningFrom == Direction.UP ? preferences.getFieldHeight() : 0;
        }

        if (invader) {
            spawned = new FighterPlane(x, y, altitude, speed, spawningFrom.opposite());
            ((MilitaryAircraft) spawned).setForeign(true);
        } else
            spawned = rpg.getRandom(x, y, altitude, speed, spawningFrom.opposite());
        return spawned;

    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            AerospaceObject ao = randomObject();
            aerospace.addAerospaceObject(ao);
            try {
                int minSpawn = aerospace.getPreferences().getSpawnTimeMin(),
                        maxSpawn = aerospace.getPreferences().getSpawnTimeMax();
                int pause = Util.randomBetween(minSpawn, maxSpawn);
                Thread.sleep(pause * 1000);
            } catch (InterruptedException ex) {
                GenericLogger.log(this.getClass(), Level.SEVERE, "Spawner thread prekinut na spavanju!", ex);
            }
        }
    }
}
