package jovic.dragan.pj2.preferences;

import jovic.dragan.pj2.logger.GenericLogger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class RadarPreferences  {

    private int fileUpdateTime;

    public int getFileUpdateTime() {
        return fileUpdateTime;
    }

    public RadarPreferences(){
    }

    private static RadarPreferences initHardcoded(){
        RadarPreferences rp = new RadarPreferences();

        rp.fileUpdateTime = Constants.DEFAULT_SHARED_UPDATE_INTERVAL;

        try(PrintWriter pw = new PrintWriter(Constants.RADAR_PROPERTIES_FULL_NAME)){
            pw.println(new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(rp));
        }
        catch (FileNotFoundException ex){
            GenericLogger.log(RadarPreferences.class, ex);
        }
        return rp;
    }

    public static RadarPreferences load(){
        RadarPreferences rp = null;
        try{
            PreferencesHelper.createFolderIfNotExists(Constants.PREFERENCES_FOLDERNAME);
            rp = (new com.google.gson.Gson().fromJson(new FileReader(Constants.RADAR_PROPERTIES_FULL_NAME),RadarPreferences.class));
        }
        catch (FileNotFoundException ex){
            GenericLogger.log(RadarPreferences.class, ex);
            rp = RadarPreferences.initHardcoded();
        }
        return rp;
    }



}
