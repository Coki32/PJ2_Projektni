package jovic.dragan.pj2.Simulator;

import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.aerospace.generators.Spawner;
import jovic.dragan.pj2.preferences.SimulatorPreferences;

public class Simulator {
    private static final SimulatorPreferences properties = SimulatorPreferences.load();

    public static void main(String[] args) {
        Aerospace aerospace = new Aerospace(properties.getFieldHeight(),
                properties.getFieldWidth(),
                properties.getMaxHeight(),
                properties.getHeightDivisions());
        Spawner spawner = new Spawner(properties, aerospace);
        aerospace.start();
    }
}
