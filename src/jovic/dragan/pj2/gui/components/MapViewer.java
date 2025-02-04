package jovic.dragan.pj2.gui.components;

import jovic.dragan.pj2.gui.handlers.MapUpdateHandler;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.preferences.SimulatorPreferences;
import jovic.dragan.pj2.util.Watcher;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

public class MapViewer extends Canvas implements Runnable {
    private SimulatorPreferences preferences;
    private MapUpdateHandler handler;

    public MapViewer(int width, int height) {
        preferences = SimulatorPreferences.load();
        handler = new MapUpdateHandler(this);
        try {
            Watcher mapWatcher = new Watcher(Constants.SIMULATOR_SHARED_FOLDERNAME, StandardWatchEventKinds.ENTRY_MODIFY);
            mapWatcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY, handler);
            mapWatcher.start();
            Watcher prefWatcher = new Watcher(Constants.PREFERENCES_FOLDERNAME, StandardWatchEventKinds.ENTRY_MODIFY);
            prefWatcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY,watchEvent -> {
               Path path = ((WatchEvent<Path>)watchEvent).context();
               if(path.toString().endsWith(Constants.SIMULATOR_PROPERTIES_FILENAME)){
                    preferences = SimulatorPreferences.load();
               }
            });
            prefWatcher.start();
        }
        catch (IOException ex){
            GenericLogger.log(this.getClass(),ex);
        }
        this.setSize(width,height);
        this.setBackground(Color.orange);
    }


    public SimulatorPreferences getPreferences() {
        return preferences;
    }

    @Override
    public void run() {

    }
}
