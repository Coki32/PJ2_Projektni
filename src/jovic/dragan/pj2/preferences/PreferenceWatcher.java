package jovic.dragan.pj2.preferences;

import jovic.dragan.pj2.logger.GenericLogger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PreferenceWatcher <T> extends Thread {

    private T original;
    private List<Field> trackingFields;
    private boolean changed;
    private WatchService watchService;
    private Supplier<T> loader;
    private String fileName;

    public PreferenceWatcher(T original, String fileName, Supplier<T> loader) {
        this.original = original;
        changed = false;
        this.fileName = fileName;
        trackingFields = new ArrayList<>();
        Path path = Paths.get(Constants.PREFERENCES_FOLDERNAME);
        watchService = null;
        this.loader = loader;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException ex) {
            GenericLogger.log(this.getClass(), ex);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while(true) {
            WatchKey key = null;
            try {
                key = watchService.take();
            } catch (InterruptedException ex) {
                GenericLogger.log(this.getClass(), ex);
            }
            if (key != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent<Path> pathEv = (WatchEvent<Path>) event;
                    Path changedFileName = pathEv.context();
                    if (changedFileName.toString().trim().endsWith(this.fileName)) {
                        T newPrefs = loader.get();
                        System.out.println("==================>Desio se edit...");
                        changed = true;
                        original = newPrefs;
                    }
                }
                key.reset();
            }
        }
    }

    public synchronized boolean isChanged(){
        return changed;
    }

    public T getOriginal(){
        return original;
    }

    public synchronized void setChanged(boolean value) {
        this.changed = value;
    }
}
