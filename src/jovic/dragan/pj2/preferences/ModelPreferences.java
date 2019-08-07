package jovic.dragan.pj2.preferences;

import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.util.Util;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.logging.Level;

public class ModelPreferences implements Serializable {

    private String[] planes;
    private String[] helicopters;
    private String[] militaryPlanes;
    private String[] militaryBombers;

    private static ModelPreferences initHardcoded(){
        ModelPreferences mp = new ModelPreferences();
        mp.planes = Constants.MODELS_PLANES;
        mp.helicopters = Constants.MODELS_HELICOPTERS;
        mp.militaryBombers = Constants.MODELS_MILITARY_BOMBERS;
        mp.militaryPlanes = Constants.MODELS_MILITARY_PLANES;
        try(PrintWriter pw = new PrintWriter(Constants.MODELS_FULL_NAME)){
            pw.println(new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(mp));
        }
        catch (FileNotFoundException ex){
            GenericLogger.log(ModelPreferences.class,ex);
        }
        return mp;
    }

    public static ModelPreferences load(){
        ModelPreferences mp = null;
        try {
            Util.createFolderIfNotExists(Constants.PREFERENCES_FOLDERNAME);
            mp = (new com.google.gson.Gson().fromJson(new FileReader(Constants.MODELS_FULL_NAME), ModelPreferences.class));
        }
        catch (FileNotFoundException ex){
            GenericLogger.log(ModelPreferences.class, Level.INFO,"Models file does not exist, creating new one...",ex);
            mp = ModelPreferences.initHardcoded();
        }
        return mp;
    }


    public String[] getPlanes() {
        return planes;
    }

    public String[] getHelicopters() {
        return helicopters;
    }

    public String[] getMilitaryPlanes() {
        return militaryPlanes;
    }


    public String[] getMilitaryBombers() {
        return militaryBombers;
    }

}
