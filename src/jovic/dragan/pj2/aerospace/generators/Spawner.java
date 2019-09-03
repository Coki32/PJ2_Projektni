package jovic.dragan.pj2.aerospace.generators;

import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.aerospace.AerospaceObject;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Spawner {

    private Queue<AerospaceObject> spawnQueue;

    public Spawner(Aerospace aerospace) {

        spawnQueue = new ConcurrentLinkedQueue<>();
        SpawningRunnable spawningRunnable = new SpawningRunnable(aerospace, spawnQueue);
        Thread spawningThread = new Thread(spawningRunnable);
        spawningThread.start();
    }

    public void enqueueSpawn(AerospaceObject ao) {
        spawnQueue.add(ao);
    }

}
