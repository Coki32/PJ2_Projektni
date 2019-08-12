package jovic.dragan.pj2.simulator;

import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.preferences.SimulatorPreferences;

import java.util.Scanner;

public class Simulator {
    private static final SimulatorPreferences properties = SimulatorPreferences.load();
    public static void main(String[] args) {
        Aerospace aerospace = new Aerospace(properties);
        aerospace.start();
        new Thread(()->{
            while(true) {
                int a = new Scanner(System.in).nextInt();
                if(a==0){
                    aerospace.banFlight();
                }
                else if (a==1)
                    aerospace.resumeFlight();
                else
                    System.exit(0);
            }
        }).start();
    }
}
