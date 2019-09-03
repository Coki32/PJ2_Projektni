package jovic.dragan.pj2.aerospace.handlers;

import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.radar.collisions.TextCollisionInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

//ne treba da bude public
class CrashConsumer extends Thread {

    private final Object empty = new Object();
    private Aerospace aerospace;
    private Queue<Path> paths = new ConcurrentLinkedQueue<>();

    CrashConsumer(Aerospace aerospace) {
        this.aerospace = aerospace;
    }

    void enqueuePath(Path path) {
        paths.add(path);
        if (paths.size() == 1) {
            synchronized (empty) {
                empty.notify();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            Path path = paths.poll();
            if (path == null) {
                synchronized (empty) {
                    try {
                        empty.wait();
                    } catch (InterruptedException ex) {
                        GenericLogger.log(this.getClass(), ex);
                    }
                }
            } else {
                TextCollisionInfo collisionInfo = null;
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Paths.get(Constants.ALERTS_FOLDER_PATH).resolve(path).toFile()));
                    collisionInfo = (TextCollisionInfo) ois.readObject();
                } catch (IOException | ClassNotFoundException ex) {
                    paths.add(path);//vrati ga u queue
                    GenericLogger.log(this.getClass(), ex);
                }
                if (collisionInfo != null) {
                    TextCollisionInfo finalCollisionInfo = collisionInfo;
                    aerospace.getMap().values().forEach(yMap -> yMap.values().forEach(q -> q.removeIf(ao -> finalCollisionInfo.getIDs().contains(ao.getId()))));
                    System.out.println(collisionInfo.getOpis() + ", uklanjam ih sa lokacije " + collisionInfo.getPozicija());
                }

            }

        }
    }
}

