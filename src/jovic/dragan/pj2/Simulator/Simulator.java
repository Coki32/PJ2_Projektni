package jovic.dragan.pj2.Simulator;

import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.aerospace.generators.Spawner;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.SimulatorPreferences;

import java.util.Scanner;

public class Simulator {
    private static final SimulatorPreferences properties = SimulatorPreferences.load();

    public static void main(String[] args) throws Exception {

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Zatvaram loggere...");
            GenericLogger.closeHandlers();
        }));
        Aerospace aerospace = new Aerospace(properties.getFieldHeight(),
                properties.getFieldWidth(),
                properties.getMaxHeight(),
                properties.getHeightDivisions());
        Spawner spawner = new Spawner(properties, aerospace);
        aerospace.start();
        new Thread(()->{
            int a = new Scanner(System.in).nextInt();
            System.exit(0);
        }).start();
    }
}
