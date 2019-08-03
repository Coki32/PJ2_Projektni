package jovic.dragan.pj2.aerospace.generators;

import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.aerospace.AerospaceObject;
import jovic.dragan.pj2.aerospace.BomberPlane;
import jovic.dragan.pj2.logger.GenericLogger;
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
    private int minInterval, maxInterval;
    private Random rng;
    private RandomPlaneGenerator rpg;

    public SpawningRunnable(SimulatorPreferences properties, Aerospace aerospace) {
        this.paused = false;
        this.minInterval = properties.getSpawnTimeMin();
        this.maxInterval = properties.getSpawnTimeMax();
        this.aerospace = aerospace;
        this.rng = new Random();
        rpg = new RandomPlaneGenerator(BomberPlane.class);
    }

    public synchronized void setPaused(boolean value){
        this.paused = value;
    }

    private Direction oneOf(Direction... sides){
        return sides[rng.nextInt(sides.length)];
    }

    private AerospaceObject randomObject(){
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
        while (true) {
            if(!paused) {
                AerospaceObject ao = randomObject();
                aerospace.addAerospaceObject(ao);
            }
            try {
                int pauza = rng.nextInt(maxInterval - minInterval) + minInterval;
                Thread.sleep(pauza*1000);
            } catch (InterruptedException ex) {
                GenericLogger.log(this.getClass(), Level.SEVERE, "Spawner thread prekinut na spavanju!", ex);
            }
        }
    }
}
