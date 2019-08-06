package jovic.dragan.pj2.preferences;

import java.io.File;

public class Constants {
    private static final String separator = File.separator;

    //ovih nema u SimulatorPreference klasi jeeer mi nema smisla, mora znati koji se prvi otvara unaprijed.
    //Ako neko hoce custom preferences fajl moralo bi se i to pratiti u nekom trecem default preferences fajlu
    //i eto gluposti s lancem fajlova
    public static final String PREFERENCES_FOLDERNAME = "." + separator + "preferences";
    public static final String SIMULATOR_PROPERTIES_FILENAME = "simulator.preferences";
    public static final String SIMULATOR_PROPERTIES_FULL_NAME = PREFERENCES_FOLDERNAME + separator + SIMULATOR_PROPERTIES_FILENAME;

    public static final String SIMULATOR_SHARED_FOLDERNAME = "." + separator + "shared";
    public static final String SIMULATOR_SHARED_FILENAME = "map.txt";
    public static final String SIMULATOR_SHARED_FILE_FULL_NAME = SIMULATOR_SHARED_FOLDERNAME + separator + "map.txt";
    public static final String[] SIMULATOR_MODELS = new String[]{"Boeing 707", "Airbus A380", "Airbus A320", "Boeing 727", "Boeing 767", "Boeing 757"};

    public static final String ALERTS_FOLDER_PATH = "."+separator+"alerts";

    public static final String EVENTS_FOLDER_PATH = "."+separator+"events";

    public static final int SIMULATOR_DEFAULT_SIZE = 50;
    public static final int SIMULATOR_DEFAULT_SPEED_MIN = 1;
    public static final int SIMULATOR_DEFAULT_SPEED_MAX = 3;
    public static final int SIMULATOR_DEFAULT_SPAWN_MIN = 1;
    public static final int SIMULATOR_DEFAULT_SPAWN_MAX = 5;
    public static final int DEFAULT_SHARED_UPDATE_INTERVAL = 3;
    public static final int[] SIMULATOR_HEIGHT_OPTIONS = new int[]{500, 1000, 1500, 2000};
    public static final int SIMULATOR_DEFAULT_FOREIGN_MIL = -1;
    public static final int SIMULATOR_DEFAULT_HOME_MIL = -1;

    public static final String RADAR_PROPERTIES_FILENAME = "radar.properties";
    public static final String RADAR_PROPERTIES_FULL_NAME = PREFERENCES_FOLDERNAME+separator+RADAR_PROPERTIES_FILENAME;


    public static final String[] BOMBER_WEAPONS = new String[]{"B28", "B39", "B53", "Daisy cutter", "Disney bomb"};
    public static final String[] PLANE_WEAPONS = new String[]{"GAU-12 Equalizer", "GIAT 30", "GSh-6-30", "GSh-30-2", "COW 37mm"};
    public static final String WEAPONS_PROPERTIES_FILENAME = "weapons.json";
    public static final String WEAPONS_PROPERTIES_FULL_NAME = PREFERENCES_FOLDERNAME + separator + WEAPONS_PROPERTIES_FILENAME;

    public static final int BOMBER_CARRY_LIMIT = 3;
    public static final int PLANE_CARRY_LIMIT = 3;


}