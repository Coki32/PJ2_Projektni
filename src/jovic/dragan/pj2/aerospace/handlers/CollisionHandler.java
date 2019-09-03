package jovic.dragan.pj2.aerospace.handlers;

import jovic.dragan.pj2.aerospace.Aerospace;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.function.Consumer;

public class CollisionHandler implements Consumer<WatchEvent> {

    private CrashConsumer consumer;

    public CollisionHandler(Aerospace aerospace) {
        consumer = new CrashConsumer(aerospace);
        consumer.start();
    }

    @Override
    public Consumer<WatchEvent> andThen(Consumer<? super WatchEvent> after) {
        return null;
    }

    @Override
    public void accept(WatchEvent watchEvent) {
        Path path = ((WatchEvent<Path>) watchEvent).context();
        consumer.enqueuePath(path);//workaround za javu i filesystem kasnjenje
    }

}

