package jovic.dragan.pj2.preferences;

import jovic.dragan.pj2.logger.GenericLogger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class WeaponPreferences {

    private String[] bomberWeapons = null;
    private String[] planeWeapons = null;
    private int bomberCarryLimit;
    private int planeCarryLimit;

    private static WeaponPreferences initHardcoded() {
        WeaponPreferences wp = new WeaponPreferences();
        wp.bomberWeapons = Constants.BOMBER_WEAPONS;
        wp.planeWeapons = Constants.PLANE_WEAPONS;
        wp.bomberCarryLimit = Constants.BOMBER_CARRY_LIMIT;
        wp.planeCarryLimit = Constants.PLANE_CARRY_LIMIT;
        try (PrintWriter writer = new PrintWriter(Constants.WEAPONS_PROPERTIES_FULL_NAME)){
            writer.println(new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(wp));
        } catch (FileNotFoundException ex) {
            GenericLogger.log(WeaponPreferences.class, ex);
        }
        return wp;
    }

    public static WeaponPreferences load() {
        WeaponPreferences wp = null;
        try {
            PreferencesHelper.createFolderIfNotExists(Constants.PREFERENCES_FOLDERNAME);
            wp = new com.google.gson.Gson().fromJson(new FileReader(Constants.WEAPONS_PROPERTIES_FULL_NAME), WeaponPreferences.class);
        } catch (FileNotFoundException ex) {
            GenericLogger.log(WeaponPreferences.class, ex);
            wp = initHardcoded();
        }
        return wp;
    }

    public String[] getBomberWeapons() {
        return bomberWeapons;
    }

    public String[] getPlaneWeapons() {
        return planeWeapons;
    }

    public int getBomberCarryLimit() {
        return bomberCarryLimit;
    }

    public int getPlaneCarryLimit() {
        return planeCarryLimit;
    }

}