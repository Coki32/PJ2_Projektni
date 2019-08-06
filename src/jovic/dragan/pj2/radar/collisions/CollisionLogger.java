package jovic.dragan.pj2.radar.collisions;

import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Util;

import java.io.*;
import java.nio.file.Path;

public class CollisionLogger {
    public static void logCollision(CollisionInfo info){
        Util.createFolderIfNotExists(Constants.ALERTS_FOLDER_PATH);
        try(ObjectOutputStream pw = new ObjectOutputStream(
                new FileOutputStream(
                new File(Constants.ALERTS_FOLDER_PATH+File.separator+System.currentTimeMillis()+".sudar")))){
            pw.writeObject(info.getSerializible());
        }
        catch (IOException ex){
            GenericLogger.log(CollisionLogger.class,ex);
        }
    }
}
