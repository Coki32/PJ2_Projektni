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
                Direction defenseDirection = null;//Direction za (xLeft,yLeft), za (xRight,yRight) je .opposite
                aerospace.banFlight();
                int xLeft, xRight, yLeft, yRight;
                SimulatorPreferences preferences = aerospace.getPreferences();

                if (invaderDirection == Direction.UP || invaderDirection == Direction.DOWN) {
                    xLeft = Math.max(invader.getX() - 1, 0);//xLeft = invader.getX()-1 < 0 ? 0 : invader.getX()-1;
                    xRight = Math.min(invader.getX() + 1, preferences.getFieldWidth());//ili to, ili desna ivica
                    yLeft = yRight = invaderDirection == Direction.UP ? 0 : preferences.getFieldHeight();
                    defenseDirection = invader.getY() < (preferences.getFieldHeight() / 2) ? Direction.UP : Direction.DOWN;
                } else {
                    //Left je bellow, right je above
                    yLeft = Math.max(invader.getY() - 1, 0);
                    yRight = Math.min(invader.getY() + 1, preferences.getFieldWidth());
                    xLeft = xRight = invader.getDirection() == Direction.RIGHT ? 0 : preferences.getFieldWidth();
                    defenseDirection = invader.getX() < (preferences.getFieldWidth() / 2) ? Direction.RIGHT : Direction.LEFT;
                }
                System.out.print("Invader:"+invader.getX()+","+invader.getY());
                System.out.println("Odbrana:" + xLeft + "," + yLeft + " == " + xRight + "," + yRight);
                int speedLeft = Util.randomBetween(preferences.getSpeedMin(), preferences.getSpeedMax());
                int speedRIght = Util.randomBetween(preferences.getSpeedMin(), preferences.getSpeedMax());
                MilitaryAircraft left = new FighterPlane(xLeft, yLeft, invader.getAltitude(),
                        speedLeft, defenseDirection);
                MilitaryAircraft right = new FighterPlane(xRight, yRight, invader.getAltitude(),
                        speedRIght, defenseDirection);
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
