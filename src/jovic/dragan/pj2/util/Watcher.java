package jovic.dragan.pj2.util;


import jovic.dragan.pj2.logger.GenericLogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Watcher extends Thread{
    protected WatchService service;
    protected Path watchingPath;
    protected Map<WatchEvent.Kind,Consumer<WatchEvent>> handlers;
    protected WatchEvent.Kind[] availableKinds;

    /**
     * @param path  Folder for which events will be tracked
     * @param kinds List of kinds of events to be tracked
     * @throws IOException registering the watcher may throw an IOException
     */
    public Watcher(String path, WatchEvent.Kind... kinds) throws IOException {
        try {
            watchingPath = Paths.get(path);
            if(!watchingPath.toFile().exists())
                throw new FileNotFoundException();
            service = FileSystems.getDefault().newWatchService();
        }catch (IOException ex){
            GenericLogger.log(this.getClass(),ex);
        }
        availableKinds = kinds;
        handlers = new HashMap<>();
        watchingPath.register(service, kinds);
    }

    public void addEventHandler(WatchEvent.Kind kind, Consumer<WatchEvent> consumer) {
        if(!handlers.containsKey(kind) && Arrays.asList(availableKinds).contains(kind))
            handlers.put(kind, consumer);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while(true){
            WatchKey key = null;
            try{
                key = service.take();
            } catch (InterruptedException ex){
                GenericLogger.log(this.getClass(),ex);
            }
            if(key!=null){
                for(WatchEvent<?> event : key.pollEvents()){
                    if(handlers.containsKey(event.kind()))
                        handlers.get(event.kind()).accept(event);
                }
                key.reset();
            }
        }
    }
}
