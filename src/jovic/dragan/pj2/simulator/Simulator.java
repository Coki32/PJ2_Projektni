package jovic.dragan.pj2.simulator;

import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.controller.AerospaceController;
import jovic.dragan.pj2.preferences.SimulatorPreferences;

import java.util.Scanner;

public class Simulator {
    private static final SimulatorPreferences properties = SimulatorPreferences.load();
    public static void main(String[] args) {
        System.out.println("Pokrenut simulator");
        Aerospace aerospace = new Aerospace(properties);
        aerospace.start();
        AerospaceController controller = new AerospaceController(aerospace);
        new Thread(()->{
            while(true) {
                int a = new Scanner(System.in).nextInt();
                if(a==0){
                    aerospace.banFlight();
                }
                else if (a==1)
                    aerospace.allowFlight();
                else
                    System.exit(0);
            }
        }).start();
    }
}
