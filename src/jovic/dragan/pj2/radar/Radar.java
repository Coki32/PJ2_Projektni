package jovic.dragan.pj2.radar;


import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.radar.collisions.CollisionChecker;
import jovic.dragan.pj2.radar.invasions.InvasionsChecker;
import jovic.dragan.pj2.util.Watcher;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Radar {


    public static void main(String[] args){
        System.out.println("Pokrenut radar");
        Watcher watcher = null;
        try {
            watcher = new Watcher(Constants.SIMULATOR_SHARED_FOLDERNAME, StandardWatchEventKinds.ENTRY_MODIFY);
        }
        catch (IOException ex){
            GenericLogger.log(Radar.class,ex);
        }
        if(watcher!=null){
            ExecutorService executor = Executors.newCachedThreadPool();
            Consumer<WatchEvent> eventConsumer = new Consumer<WatchEvent>() {
                long newStart = 0, prevStart = 0;

                @Override
                public void accept(WatchEvent ev) {
                    Path path = ((WatchEvent<Path>) ev).context();
                    newStart = System.currentTimeMillis();
//                    System.out.println("Od proslog "+(newStart-prevStart)+"ms");
                    try {
                        List<String> lines = Files.readAllLines(Paths.get(Constants.SIMULATOR_SHARED_FOLDERNAME).resolve(path));
//                        System.out.print(lines.size()>=1 && ((newStart-prevStart)>20));
                        if (lines.size() >= 1) {//&& ((newStart - prevStart) > 20)) {
                            System.out.println(LocalDateTime.now() + " - radar tick");
                            Queue<ObjectInfo> invasions = new ConcurrentLinkedQueue<>();
                            Queue<ObjectInfo> collisions = new ConcurrentLinkedQueue<>();
                            for (String line : lines) {
                                ObjectInfo info = new ObjectInfo(line.trim().split(","));
                                invasions.add(info);
                                collisions.add(info);
                            }
                            //Jednom threadu kopija, jednom original jer collision checker izbacuje elemente iz kolekcije
                            executor.execute(new CollisionChecker(collisions));
                            executor.execute(new InvasionsChecker(invasions));
                        } else {
//                            System.out.println("Ne radim nista: "+lines);
                        }
                    } catch (IOException ex) {
                        GenericLogger.log(Radar.class, ex);
                    }
                    prevStart = newStart;
                }
            };
            watcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY, eventConsumer);
            watcher.start();
        }

        (new Thread(() -> {
            int a = new Scanner(System.in).nextInt();
            System.exit(0);
        })).start();
    }

}

