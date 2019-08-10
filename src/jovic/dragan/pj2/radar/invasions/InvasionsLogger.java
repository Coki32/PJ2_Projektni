package jovic.dragan.pj2.radar.invasions;

import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.radar.ObjectInfo;
import jovic.dragan.pj2.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InvasionsLogger {
    private static Executor delayingExecutor = Executors.newCachedThreadPool();
    public static void logInvasion(ObjectInfo info){
        delayingExecutor.execute( new Runnable(){
            @Override
            public void run() {
                //try {

                    Util.createFolderIfNotExists(Constants.EVENTS_FOLDER_PATH);
                    //Thread.sleep(10000);//pauza da ovaj strani malo "pobjegne"
                    try (PrintWriter pw =
                                 new PrintWriter(Constants.EVENTS_FOLDER_PATH +
                                         File.separator + LocalDateTime.now().toLocalTime().toString().replace(':', '.') + ".txt")) {
                        pw.println(info.toCsv());
                    } catch (FileNotFoundException ex) {
                        GenericLogger.log(InvasionsChecker.class, ex);
                    }
                //}catch (InterruptedException ex){
                //    GenericLogger.log(this.getClass(),ex);
                //}
            }
        });

    }
}
