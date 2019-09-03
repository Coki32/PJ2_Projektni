package jovic.dragan.pj2.radar;

import jovic.dragan.pj2.aerospace.AerospaceObject;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.preferences.RadarPreferences;
import jovic.dragan.pj2.util.Util;
import jovic.dragan.pj2.util.Watcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;

public class RadarExporter extends Thread {

    private final Map<Integer, Map<Integer, Queue<AerospaceObject>>> map;
    private RadarPreferences radarPreferences;

    public RadarExporter(Map<Integer, Map<Integer, Queue<AerospaceObject>>> map) {
        this.map = map;
        radarPreferences = RadarPreferences.load();
        try {
            Watcher watcher = new Watcher(Constants.PREFERENCES_FOLDERNAME, StandardWatchEventKinds.ENTRY_MODIFY);
            watcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY, ev -> {
                if (((WatchEvent<Path>) ev).context().toFile().toString().endsWith(Constants.RADAR_PROPERTIES_FILENAME))
                    this.updatePreferences();
            });
            watcher.start();
        } catch (IOException ex) {
            GenericLogger.log(this.getClass(), Level.WARNING,
                    "Nije moguce pokrenuti watcher za radar preference, nece se apdejtovati u exporteru", ex);
        }
        Util.createFolderIfNotExists(Constants.SIMULATOR_SHARED_FOLDERNAME);
    }

    private void updatePreferences() {
        radarPreferences = RadarPreferences.load();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while(true) {
            try (PrintWriter pw = new PrintWriter(Constants.SIMULATOR_SHARED_FILE_FULL_NAME)) {
                synchronized (map) {//Citava mapa se zakljuca da se ne bi neki pomjerili, neki ostali na mjestu
                    long start = System.nanoTime();
                    map.values().forEach(yMap -> yMap.values().forEach(q -> q.forEach(ao -> pw.println(ao.export()))));
                    long end = System.nanoTime();
                    System.out.println("Mapa zakljucana na " + (end - start) / 1000000.0 + " ms");
                }

            } catch (FileNotFoundException ex) {
                GenericLogger.log(this.getClass(), ex);
            }
            try {
                Thread.sleep(radarPreferences.getFileUpdateTime());
            } catch (InterruptedException ex) {
                GenericLogger.log(this.getClass(), ex);
            }
        }
    }
}
