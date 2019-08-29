package jovic.dragan.pj2.radar;

import jovic.dragan.pj2.aerospace.AerospaceObject;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.preferences.PreferenceWatcher;
import jovic.dragan.pj2.preferences.PreferencesHelper;
import jovic.dragan.pj2.preferences.RadarPreferences;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RadarExporter extends Thread {

    private Map<Integer,Map<Integer,ConcurrentLinkedDeque<AerospaceObject>>> map;
    private RadarPreferences radarPreferences;
    private PreferenceWatcher<RadarPreferences> preferenceWatcher;
    public RadarExporter(Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> map){
        this.map = map;
        radarPreferences = RadarPreferences.load();
        preferenceWatcher = new PreferenceWatcher<>(radarPreferences,Constants.RADAR_PROPERTIES_FILENAME,RadarPreferences::load);
        PreferencesHelper.createFolderIfNotExists(Constants.SIMULATOR_SHARED_FOLDERNAME);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while(true) {
            Map<Integer, Map<Integer, ConcurrentLinkedDeque<AerospaceObject>>> copy;
            synchronized (map) {
                copy = new ConcurrentHashMap<>(map);
            }
            try (PrintWriter pw = new PrintWriter(Constants.SIMULATOR_SHARED_FILE_FULL_NAME)) {
                copy.values().parallelStream().forEach(yMap -> yMap.values().forEach(q ->
                {
                    q.forEach(ao -> {
                        pw.println(ao.export());
                    });
                }));
            } catch (FileNotFoundException ex) {
                GenericLogger.log(this.getClass(), ex);
            }
            try {
                if(preferenceWatcher.isChanged()){
                    radarPreferences = preferenceWatcher.getOriginal();
                    preferenceWatcher.setChanged(false);
                }
                Thread.sleep(radarPreferences.getFileUpdateTime());
            } catch (InterruptedException ex) {
                GenericLogger.log(this.getClass(), ex);
            }
        }
    }
}
