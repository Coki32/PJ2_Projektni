package jovic.dragan.pj2.preferences;


import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.simulator.Simulator;
import jovic.dragan.pj2.util.Util;

import java.io.*;
import java.util.logging.Level;


public class SimulatorPreferences implements Serializable {

    private int fieldWidth, fieldHeight, spawnTimeMin, spawnTimeMax, speedMin, speedMax;
    private int[] heightOptions;
    private String sharedFolder, sharedFileName, sharedFullName;
    private int simulatorUpdatePeriod;
    private int foreignMilitary, homeMilitary;


    private static SimulatorPreferences initHardcoded() {
        SimulatorPreferences sp = new SimulatorPreferences();
        sp.simulatorUpdatePeriod = Constants.SIMULATOR_DEFAULT_PERIOD;
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
        SimulatorPreferences.save(sp);
        return sp;
    }

    public static void save(SimulatorPreferences sp) {
        try (PrintWriter pw = new PrintWriter(Constants.SIMULATOR_PROPERTIES_FULL_NAME)){
            pw.println(new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(sp));
        } catch (FileNotFoundException ex) {
            GenericLogger.log(SimulatorPreferences.class, ex);
        }

    }

    public static SimulatorPreferences load(){
        SimulatorPreferences sp = null;
        try {
            Util.createFolderIfNotExists(Constants.PREFERENCES_FOLDERNAME);
            sp = (new com.google.gson.Gson().fromJson(new FileReader(Constants.SIMULATOR_PROPERTIES_FULL_NAME), SimulatorPreferences.class));
        }
        catch (FileNotFoundException ex){
            GenericLogger.log(SimulatorPreferences.class,Level.INFO,"Config file does not exist, creating new one...",ex);
            sp = SimulatorPreferences.initHardcoded();
        }
        return sp;
    }

    public int getSimulatorUpdatePeriod() {
        return simulatorUpdatePeriod;
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

    //Samo za gui olaksicu, inace ne bi bilo ovdje
    public void setForeignMilitary(int foreignMilitary) {
        this.foreignMilitary = foreignMilitary;
    }

    public int getForeignMilitary() {
        return foreignMilitary;
    }

    public int getHomeMilitary() {
        return homeMilitary;
    }
}