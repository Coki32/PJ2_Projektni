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

}
