package jovic.dragan.pj2.preferences;


import jovic.dragan.pj2.logger.GenericLogger;

import java.io.*;
import java.nio.file.Paths;
import java.util.logging.Level;


public class SimulatorPreferences implements Serializable {

    public int fieldWidth, fieldHeight, spawnTimeMin, spawnTimeMax, speedMin, speedMax;
    public int[] heightOptions;
    public String sharedFolder, sharedFileName, sharedFullName;
    public int sharedFileUpdateInterval;
    public int foreignMilitary, homeMilitary;
    public String[] models;


    private void attachChangeWatcher(){

    }

    private static SimulatorPreferences initHardcoded() {
        SimulatorPreferences sp = new SimulatorPreferences();
        sp.fieldWidth = Constants.SIMULATOR_DEFAULT_SIZE;
        sp.fieldHeight = Constants.SIMULATOR_DEFAULT_SIZE;
        sp.spawnTimeMax = Constants.SIMULATOR_DEFAULT_SPAWN_MAX;
        sp.spawnTimeMin = Constants.SIMULATOR_DEFAULT_SPAWN_MIN;
        sp.foreignMilitary = Constants.SIMULATOR_DEFAULT_FOREIGN_MIL;
        sp.homeMilitary = Constants.SIMULATOR_DEFAULT_HOME_MIL;
        sp.speedMin = Constants.SIMULATOR_DEFAULT_SPEED_MIN;
        sp.speedMax = Constants.SIMULATOR_DEFAULT_SPEED_MAX;
        sp.heightOptions = Constants.SIMULATOR_HEIGHT_OPTIONS;
        sp.sharedFileName = Constants.SIMULATOR_SHARED_FILENAME;
        sp.sharedFolder = Constants.SIMULATOR_SHARED_FOLDERNAME;
        sp.sharedFullName = Constants.SIMULATOR_SHARED_FILE_FULL_NAME;
        sp.sharedFileUpdateInterval = Constants.SIMULATOR_DEFAULT_SHARED_UPDATE_INTERVAL;
        sp.models = Constants.SIMULATOR_MODELS;
        try (PrintWriter pw = new PrintWriter(Constants.SIMULATOR_PROPERTIES_FULL_NAME)){
            pw.println(new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(sp));
        } catch (FileNotFoundException ex) {
            GenericLogger.log(SimulatorPreferences.class, ex);
        }
        return sp;
    }

    public static SimulatorPreferences load(){
        SimulatorPreferences sp = null;
        try {
            PreferencesHelper.createFolderIfNotExists(Constants.PREFERENCES_FOLDERNAME);
            sp = (new com.google.gson.Gson().fromJson(new FileReader(Constants.SIMULATOR_PROPERTIES_FULL_NAME), SimulatorPreferences.class));
        }
        catch (FileNotFoundException ex){
            GenericLogger.log(SimulatorPreferences.class,Level.INFO,"Config file does not exist, creating new one...",ex);
            sp = SimulatorPreferences.initHardcoded();
        }
        return sp;
    }

    public int getFieldWidth() {
        return fieldWidth;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public int getSpawnTimeMin() {
        return spawnTimeMin;
    }

    public int getSpawnTimeMax() {
        return spawnTimeMax;
    }

    public int getSpeedMin() {
        return speedMin;
    }

    public int getSpeedMax() {
        return speedMax;
    }

    public int[] getHeightOptions(){
        return heightOptions;
    }

    public String getSharedFileName() {
        return sharedFileName;
    }

    public int getSharedFileUpdateInterval() {
        return sharedFileUpdateInterval;
    }

    public String[] getModels() {
        return models;
    }

}