package jovic.dragan.pj2.radar;

import jovic.dragan.pj2.aerospace.AerospaceObject;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.preferences.PreferencesHelper;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RadarExporter extends Thread {

    private Map<Integer,Map<Integer,ConcurrentLinkedDeque<AerospaceObject>>> map;
    private int frequency;

    public RadarExporter(Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> map, int frequency){
        this.map = map;
        this.frequency = frequency;
        PreferencesHelper.createFolderIfNotExists(Constants.SIMULATOR_SHARED_FOLDERNAME);
    }

    @Override
    public void run() {
        while(true) {
            long start = System.currentTimeMillis();

            Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> copy;
            synchronized (map) {
                copy = new ConcurrentHashMap<>(map);
            }
            try (PrintWriter pw = new PrintWriter(Constants.SIMULATOR_SHARED_FILE_FULL_NAME)) {
                copy.values().parallelStream().forEach(yMap -> yMap.values().forEach(q ->
                {
                    q.forEach(ao -> pw.println(ao.export()));
                }));
            } catch (FileNotFoundException ex) {
                GenericLogger.log(this.getClass(), ex);
            }
            long end = System.currentTimeMillis();
            System.out.println("Exported in " + (end - start) + "ms");
            try {
                Thread.sleep(frequency * 1000);
            } catch (InterruptedException ex) {
                GenericLogger.log(this.getClass(), ex);
            }
        }
    }
}
