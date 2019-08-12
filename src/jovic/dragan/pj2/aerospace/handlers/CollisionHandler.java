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
import java.nio.file.WatchEvent;
import java.util.function.Consumer;

public class CollisionHandler implements Consumer<WatchEvent> {

    private Aerospace aerospace;

    public CollisionHandler(Aerospace aerospace) {
        this.aerospace = aerospace;
    }

    @Override
    public Consumer<WatchEvent> andThen(Consumer<? super WatchEvent> after) {
        return null;
    }

    @Override
    public void accept(WatchEvent watchEvent) {
        Path path = ((WatchEvent<Path>) watchEvent).context();
        TextCollisionInfo collisionInfo = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Paths.get(Constants.ALERTS_FOLDER_PATH).resolve(path).toFile()));
            collisionInfo = (TextCollisionInfo) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            GenericLogger.log(this.getClass(), ex);
        }
        if (collisionInfo != null) {
            TextCollisionInfo finalCollisionInfo = collisionInfo;
            aerospace.getMap().values().forEach(yMap -> yMap.values().forEach(q -> q.removeIf(ao -> finalCollisionInfo.getIDs().contains(ao.getId()))));
            System.out.println(collisionInfo.getOpis() + ", uklanjam ih sa lokacije " + collisionInfo.getPozicija());
        }
    }
}