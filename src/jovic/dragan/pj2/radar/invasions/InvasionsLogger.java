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

public class InvasionsLogger {
    public static void logInvasion(ObjectInfo info){
        Util.createFolderIfNotExists(Constants.EVENTS_FOLDER_PATH);
        try(PrintWriter pw =
                    new PrintWriter(Constants.EVENTS_FOLDER_PATH +
                            File.separator + LocalDateTime.now().toLocalTime().toString().replace(':','.')+".txt")){
        pw.println(info.getX()+","+info.getY()+","+info.getAltitude()+","+info.getDirection());
        }catch (FileNotFoundException ex){
            GenericLogger.log(InvasionsChecker.class,ex);
        }
    }
}
