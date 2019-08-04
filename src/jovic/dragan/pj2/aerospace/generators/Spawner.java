package jovic.dragan.pj2.aerospace.generators;

import jovic.dragan.pj2.Simulator.Simulator;
import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.aerospace.AerospaceObject;
import jovic.dragan.pj2.aerospace.BomberPlane;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.preferences.PreferenceWatcher;
import jovic.dragan.pj2.preferences.SimulatorPreferences;
import jovic.dragan.pj2.util.Direction;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;
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

    private Direction oneOf(Direction... sides){
        return sides[rng.nextInt(sides.length)];
    }

    private AerospaceObject randomObject(){
        if(watcher.isChanged()){
            System.out.println("Ucitano u spawneru");
            preferences = watcher.getOriginal();
            watcher.setChanged(false);
        }
        int x=0,y=0;
        Direction direction;
        if(rng.nextBoolean()) {
            x = rng.nextInt(100);
            direction = (x==0) ? oneOf(Direction.UP, Direction.RIGHT):
                    oneOf(Direction.UP, Direction.LEFT, Direction.RIGHT);
        }
        else {
            y = rng.nextInt(100);
            direction = oneOf(Direction.UP, Direction.DOWN);
        }
        return rpg.getRandom(x,y,rng.nextInt(500),1,direction);
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
