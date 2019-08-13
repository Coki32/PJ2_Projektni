package jovic.dragan.pj2.controller;

import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Util;
import jovic.dragan.pj2.util.Watcher;

import java.io.IOException;
import java.nio.file.*;
import java.util.function.Consumer;

public class AerospaceController {

    private Aerospace aerospace;

    private Watcher commandWatcher;

    public AerospaceController(Aerospace aerospace) {
        this.aerospace = aerospace;
        Util.createFolderIfNotExists(Constants.COMMANDS_FOLDER);
        try {
            commandWatcher = new Watcher(Constants.COMMANDS_FOLDER, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
            Consumer<WatchEvent> consumer = watchEvent -> {
                Path path = ((WatchEvent<Path>) watchEvent).context();
                System.out.println("Primljena komanda: " + path.toString());
                if (path.toString().endsWith(Constants.COMMAND_BAN_FLIGHT)) {
                    aerospace.guiBanFlight();
                } else if (path.toString().endsWith(Constants.COMMAND_ALLOW_FLIGHT)) {
                    aerospace.guiAllowFlight();
                }
                try {
                    Files.delete(Paths.get(Constants.COMMANDS_FOLDER).resolve(path));
                } catch (IOException ex) {
                    GenericLogger.log(this.getClass(), ex);
                    System.out.println("Nemogase obrisati fajl " + path.toString());
                }
            };
            commandWatcher.addEventHandler(StandardWatchEventKinds.ENTRY_CREATE, consumer);
            commandWatcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY, consumer);


            commandWatcher.start();
        } catch (IOException ex) {
            GenericLogger.log(this.getClass(), ex);
        }
    }

}
