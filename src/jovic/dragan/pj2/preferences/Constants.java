package jovic.dragan.pj2.preferences;

public class Constants {
    public static final String PREFERENCES_FOLDERNAME = "./preferences";
    public static final String SIMULATOR_PROPERTIES_FILENAME = "./preferences/simulator.preferences";
    public static final String SIMULATOR_SHARED_FILENAME = "./shared/map.txt";
    public static final String[] SIMULATOR_MODELS = new String[]{"Boeing 707", "Airbus A380", "Airbus A320", "Boeing 727", "Boeing 767", "Boeing 757"};
    public static final int SIMULATOR_DEFAULT_SIZE = 50;
    public static final int SIMULATOR_DEFAULT_SPEED_MIN = 1;
    public static final int SIMULATOR_DEFAULT_SPEED_MAX = 3;
    public static final int SIMULATOR_DEFAULT_SPAWN_MIN = 1;
    public static final int SIMULATOR_DEFAULT_SPAWN_MAX = 5;
    public static final int SIMULATOR_DEFAULT_SHARED_UPDATE_INTERVAL = 3;
    public static final int SIMULATOR_HEIGHT_MAX = 3000;
    public static final int SIMULATOR_HEIGHT_DIVS = 6;
    public static final int SIMULATOR_DEFAULT_FOREIGN_MIL = -1;
    public static final int SIMULATOR_DEFAULT_HOME_MIL = -1;
    public static final String[] BOMBER_WEAPONS = new String[]{"B28", "B39", "B53", "Daisy cutter", "Disney bomb"};
    public static final String[] PLANE_WEAPONS = new String[]{"GAU-12 Equalizer", "GIAT 30", "GSh-6-30", "GSh-30-2", "COW 37mm"};
    public static final String WEAPONS_PROPERTIES_FILENAME = "./preferences/weapons.json";
    public static final int BOMBER_CARRY_LIMIT = 3;
    public static final int PLANE_CARRY_LIMIT = 3;
}
