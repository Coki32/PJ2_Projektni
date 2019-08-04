package jovic.dragan.pj2.preferences;

import java.nio.file.Paths;

public class PreferencesHelper {
    public static void createFolderIfNotExists(String path){
        if(!Paths.get(path).toFile().exists()) {
            Paths.get(path).toFile().mkdir();
        }
    }
}
