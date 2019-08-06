package jovic.dragan.pj2.aerospace.generators;

import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.aerospace.AerospaceObject;
import jovic.dragan.pj2.aerospace.BomberPlane;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.preferences.PreferenceWatcher;
import jovic.dragan.pj2.preferences.SimulatorPreferences;
import jovic.dragan.pj2.util.Direction;

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
        rpg = new RandomPlaneGenerator(BomberPlane.class);
        watcher = new PreferenceWatcher<>(preferences, Constants.SIMULATOR_PROPERTIES_FILENAME, SimulatorPreferences::load);
        watcher.start();
    }

    public synchronized void setPaused(boolean value){
        this.paused = value;
    }

    private int randomBetween(int min, int max){
        return rng.nextInt(max-min+1)+min;
    }

    private AerospaceObject randomObject(){
        if(watcher.isChanged()){
            System.out.println("Ucitano u spawneru");
            preferences = watcher.getOriginal();
            watcher.setChanged(false);
        }
        int x=0,y=0;
        //bice glupo tipa spawningFrom bude LEFT pa to znaci sa lijeve ivice ide do desne
        Direction spawningFrom = Direction.fromInt(rng.nextInt(4));
        if(spawningFrom == Direction.LEFT || spawningFrom == Direction.RIGHT) {
            x = spawningFrom == Direction.LEFT ? 0 : preferences.getFieldWidth();
            y = rng.nextInt(preferences.getFieldHeight());
        }
        else {
            x = rng.nextInt(preferences.getFieldWidth());
            y = spawningFrom == Direction.UP ? preferences.getFieldHeight() : 0;
        }
        return rpg.getRandom(x,y,
                rng.nextInt(500),
                randomBetween(preferences.getSpeedMin(),preferences.getSpeedMax()),
                spawningFrom.opposite());
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            int minSpawn = preferences.getSpawnTimeMin(),
                    maxSpawn = preferences.getSpawnTimeMax();
            if(!paused) {
                AerospaceObject ao = randomObject();
                aerospace.addAerospaceObject(ao);
            }
            try {
                int pauza = rng.nextInt(maxSpawn-minSpawn) + minSpawn;
                Thread.sleep(pauza*1000);
            } catch (InterruptedException ex) {
                GenericLogger.log(this.getClass(), Level.SEVERE, "Spawner thread prekinut na spavanju!", ex);
            }
        }
    }
}
