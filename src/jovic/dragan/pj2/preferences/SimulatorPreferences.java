package jovic.dragan.pj2.preferences;


import jovic.dragan.pj2.logger.GenericLogger;

import java.io.*;
import java.util.logging.Level;


public class SimulatorPreferences implements Serializable {

    private int fieldWidth, fieldHeight, spawnTimeMin, spawnTimeMax, speedMin, speedMax;
    private int maxHeight, heightDivisions;
    private String sharedFileName;
    private int sharedFileUpdateInterval;
    private boolean foreignMilitary, homeMilitary;
    private String[] models;

    private static SimulatorPreferences initHardcoded() {
        SimulatorPreferences sp = new SimulatorPreferences();
        sp.fieldWidth = Constants.SIMULATOR_DEFAULT_SIZE;
        sp.fieldHeight = Constants.SIMULATOR_DEFAULT_SIZE;
        sp.spawnTimeMax = Constants.SIMULATOR_DEFAULT_SPAWN_MAX;
        sp.spawnTimeMin = Constants.SIMULATOR_DEFAULT_SPAWN_MIN;
        sp.speedMin = Constants.SIMULATOR_DEFAULT_SPEED_MIN;
        sp.speedMax = Constants.SIMULATOR_DEFAULT_SPEED_MAX;
        sp.maxHeight = Constants.SIMULATOR_HEIGHT_MAX;
        sp.heightDivisions = Constants.SIMULATOR_HEIGHT_DIVS;
        sp.sharedFileName = Constants.SIMULATOR_SHARED_FILENAME;
        sp.sharedFileUpdateInterval = Constants.SIMULATOR_DEFAULT_SHARED_UPDATE_INTERVAL;
        sp.models = Constants.SIMULATOR_MODELS;
        try {
            PrintWriter pw = new PrintWriter(Constants.SIMULATOR_PROPERTIES_FILENAME);
            pw.println(new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(sp));
            pw.flush();
            pw.close();
        } catch (FileNotFoundException ex) {
            GenericLogger.log(SimulatorPreferences.class, ex);
        }
        return sp;
    }

    public static SimulatorPreferences load(){
        SimulatorPreferences sp = null;
        try {
            sp = (new com.google.gson.Gson().fromJson(new FileReader(Constants.SIMULATOR_PROPERTIES_FILENAME), SimulatorPreferences.class));
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

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getHeightDivisions() {
        return heightDivisions;
    }

    public String getSharedFileName() {
        return sharedFileName;
    }

    public int getSharedFileUpdateInterval() {
        return sharedFileUpdateInterval;
    }

    public boolean isForeignMilitary() {
        return foreignMilitary;
    }

    public boolean isHomeMilitary() {
        return homeMilitary;
    }

    public String[] getModels() {
        return models;
    }

}