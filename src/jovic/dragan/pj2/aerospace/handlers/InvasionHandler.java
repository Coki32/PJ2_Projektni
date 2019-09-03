package jovic.dragan.pj2.aerospace.handlers;

import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.aerospace.FighterPlane;
import jovic.dragan.pj2.aerospace.MilitaryAircraft;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.preferences.SimulatorPreferences;
import jovic.dragan.pj2.radar.ObjectInfo;
import jovic.dragan.pj2.util.Direction;
import jovic.dragan.pj2.util.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class InvasionHandler implements Consumer<WatchEvent> {

    private Aerospace aerospace;
    private Set<Integer> handled;
    public InvasionHandler(Aerospace aerospace){
        this.aerospace = aerospace;
        handled = new HashSet<>();
    }

    @Override
    public Consumer<WatchEvent> andThen(Consumer<? super WatchEvent> after) {
        return null;
    }

    @Override
    public void accept(WatchEvent watchEvent) {
        Path path = ((WatchEvent<Path>)watchEvent).context();
        try {
            ObjectInfo invader = new ObjectInfo(Files.readString(Paths.get(Constants.EVENTS_FOLDER_PATH).resolve(path)).trim().split(","));
            if (!invader.isFollowed() && handled.add(invader.getId())) {
                Direction invaderDirection = invader.getDirection();
                System.out.println("---------PREPOZNAT INVADER! " + invader);
                aerospace.banFlight();
                int xLeft = -1, xRight = -1, yLeft = -1, yRight = -1;
                SimulatorPreferences preferences = aerospace.getPreferences();
                int width = preferences.getFieldWidth(), height = preferences.getFieldHeight();
                int invaderX = invader.getX(), invaderY = invader.getY();
                switch (invaderDirection) {
                    case UP: {
                        xLeft = Math.max(invaderX - 1, 0);
                        xRight = Math.min(invaderX + 1, width);
                        yLeft = yRight = 0;
                    }
                    break;
                    case DOWN: {//Ovdje se pod lijevi misli na "pilotov" lijevi. Kad bi gledali iz perspektive invadera
                        yLeft = yRight = height;
                        xLeft = Math.min(invaderX + 1, width);
                        xRight = Math.max(invaderX - 1, 0);
                    }
                    break;
                    case LEFT: {
                        xLeft = xRight = width;
                        yLeft = Math.max(invaderY - 1, 0);
                        yRight = Math.min(invaderY + 1, height);
                    }
                    break;
                    case RIGHT: {
                        xLeft = xRight = 0;
                        yLeft = Math.min(invaderY + 1, height);
                        yRight = Math.max(invaderY - 1, 0);
                    }
                    break;
                }
                int speedLeft = Util.randomBetween(preferences.getSpeedMin(), preferences.getSpeedMax());
                int speedRight = Util.randomBetween(preferences.getSpeedMin(), preferences.getSpeedMax());
                MilitaryAircraft left = new FighterPlane(xLeft, yLeft, invader.getAltitude(), speedLeft, invaderDirection);
                MilitaryAircraft right = new FighterPlane(xRight, yRight, invader.getAltitude(), speedRight, invaderDirection);
                aerospace.getMap().values().parallelStream().forEach(yMap-> yMap.values().forEach(q->{
                    q.forEach(ao->{
                        if(ao.getId()==invader.getId())
                        {
                            ((MilitaryAircraft)ao).setFollowed(true);
                            ((MilitaryAircraft) ao).addFollower(left);
                            ((MilitaryAircraft) ao).addFollower(right);
                            left.setFollowing((MilitaryAircraft) ao);
                            right.setFollowing((MilitaryAircraft) ao);
                        }
                    });
                }));
                aerospace.getSpawner().enqueueSpawn(left);
                aerospace.getSpawner().enqueueSpawn(right);
            } else
                System.out.println(handled + ", ne dodajem " + invader.getId());
        }
        catch (IOException ex){
            GenericLogger.log(this.getClass(),ex);
        }
    }
}
