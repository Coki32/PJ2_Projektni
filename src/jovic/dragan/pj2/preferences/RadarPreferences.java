package jovic.dragan.pj2.preferences;

import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.util.Util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class RadarPreferences  {

    private int fileUpdateTime;

    private static RadarPreferences initHardcoded(){
        RadarPreferences rp = new RadarPreferences();

        rp.fileUpdateTime = Constants.DEFAULT_SHARED_UPDATE_INTERVAL;
        RadarPreferences.save(rp);
        return rp;
    }

    public static void save(RadarPreferences rp) {
        try(PrintWriter pw = new PrintWriter(Constants.RADAR_PROPERTIES_FULL_NAME)){
            pw.println(new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(rp));
        } catch (FileNotFoundException ex){
            GenericLogger.log(RadarPreferences.class, ex);
        }

    }

    public static RadarPreferences load(){
        RadarPreferences rp;
        try{
            Util.createFolderIfNotExists(Constants.PREFERENCES_FOLDERNAME);
            rp = (new com.google.gson.Gson().fromJson(new FileReader(Constants.RADAR_PROPERTIES_FULL_NAME),RadarPreferences.class));
        }
        catch (FileNotFoundException ex){
            GenericLogger.log(RadarPreferences.class, ex);
            rp = RadarPreferences.initHardcoded();
        }
        return rp;
    }

    public int getFileUpdateTime() {
        return fileUpdateTime;
    }

}
