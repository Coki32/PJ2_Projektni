package jovic.dragan.pj2.util;


import jovic.dragan.pj2.logger.GenericLogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

public abstract class Watcher {
    protected WatchService service;
    protected Path watchingPath;

    public Watcher(String path, WatchEvent.Kind... kinds) throws FileNotFoundException {
        try {
            watchingPath = Paths.get(path);
            if(!watchingPath.toFile().exists())
                throw new FileNotFoundException();
            service = FileSystems.getDefault().newWatchService();
            watchingPath.register(service, kinds);
        }catch (IOException ex){
            GenericLogger.log(this.getClass(),ex);
        }
    }
}
