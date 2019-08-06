package jovic.dragan.pj2.radar;


import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.radar.collisions.CollisionChecker;
import jovic.dragan.pj2.radar.invasions.InvasionsChecker;
import jovic.dragan.pj2.util.Watcher;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Radar {


    public static void main(String[] args){
        Watcher watcher = null;
        try {
            watcher = new Watcher(Constants.SIMULATOR_SHARED_FOLDERNAME, StandardWatchEventKinds.ENTRY_MODIFY);
        }
        catch (IOException ex){
            GenericLogger.log(Radar.class,ex);
        }
        if(watcher!=null){
            Executor executor = Executors.newCachedThreadPool();
            Consumer<WatchEvent> eventConsumer = ev->{
                System.out.println(ev.kind());
                Path path = ((WatchEvent<Path>)ev).context();
                try {
                    List<String> lines = Files.readAllLines(Paths.get(Constants.SIMULATOR_SHARED_FOLDERNAME).resolve(path));
                    if (lines.size() != 0) {
                        List<ObjectInfo> info = new ArrayList<>(lines.size());
                        for (String line: lines) {
                            info.add(new ObjectInfo(line.split(",")));
                        }
                        executor.execute(new CollisionChecker(info));
                        executor.execute(new InvasionsChecker(info));
                    }

                }
                catch (IOException ex){
                    GenericLogger.log(Radar.class,ex);
                }
            };
            watcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY, eventConsumer);
            watcher.start();
        }
    }

}

