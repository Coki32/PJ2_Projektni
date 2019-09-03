package jovic.dragan.pj2.radar.invasions;

import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.radar.ObjectInfo;
import jovic.dragan.pj2.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InvasionsLogger {
    private static Executor delayingExecutor = Executors.newCachedThreadPool();

    static {
        Util.createFolderIfNotExists(Constants.EVENTS_FOLDER_PATH);
    }

    public static void logInvasion(ObjectInfo info){
        delayingExecutor.execute(() -> {
            try (PrintWriter pw =
                         new PrintWriter(Constants.EVENTS_FOLDER_PATH +
                                 File.separator + LocalDateTime.now().toLocalTime().toString().replace(':', '.') + ".txt")) {
                pw.println(info.toCsv());
            } catch (FileNotFoundException ex) {
                GenericLogger.log(InvasionsChecker.class, ex);
            }
        });

    }
}
