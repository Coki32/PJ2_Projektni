package jovic.dragan.pj2.aerospace.handlers;

import jovic.dragan.pj2.Interfaces.Military;
import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.aerospace.FighterPlane;
import jovic.dragan.pj2.aerospace.MilitaryAircraft;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.radar.ObjectInfo;
import jovic.dragan.pj2.util.Direction;

import java.io.DataInput;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.util.function.Consumer;

public class InvasionHandler implements Consumer<WatchEvent> {

    private Aerospace aerospace;

    public InvasionHandler(Aerospace aerospace){
        this.aerospace = aerospace;
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
            Direction invaderDirection = invader.getDirection();
            System.out.println("---------PREPOZNAT INVADER! " + invader);
            Direction defenseDirection = null;//Direction za (x1,y1), za (x2,y2) je .opposite
            aerospace.banFlight();
            int x1,x2,y1,y2;
            if(invaderDirection == Direction.UP || invaderDirection == Direction.DOWN) {
                x1 = invader.getX()-1;
                x2 = invader.getX()+1;
                y1 = y2 = invaderDirection==Direction.UP ? 0 : aerospace.getPreferences().getFieldHeight();
                defenseDirection = invader.getY()<(aerospace.getPreferences().getFieldHeight()/2) ? Direction.UP : Direction.DOWN;
            }
            else {
                y1 = invader.getY()-1;
                y2 = invader.getY()+1;
                x1 = x2 = invader.getDirection() == Direction.RIGHT ? 0 : aerospace.getPreferences().getFieldWidth();
                defenseDirection = invader.getX()<(aerospace.getPreferences().getFieldWidth()/2) ? Direction.RIGHT:Direction.LEFT;
            }
//            Thread.sleep(3000);
            System.out.println("Odbrana spawnuje dva aviona na ("+x1+","+y1+") i ("+x2+","+y2+")");
            aerospace.addAerospaceObject(new FighterPlane(x1,y1,invader.getAltitude(),1,defenseDirection));
            aerospace.addAerospaceObject(new FighterPlane(x2,y2,invader.getAltitude(),1,defenseDirection));
            aerospace.getField(invader.getX(),invader.getY()).stream()
                    .filter(ao->ao.getAltitude()==invader.getAltitude() && (ao instanceof MilitaryAircraft) &&
                            ((MilitaryAircraft)ao).isForeign()).forEach(ao->((MilitaryAircraft)ao).setFollowed(true));
        }
        catch (IOException  ex){
            GenericLogger.log(this.getClass(),ex);
        }
    }
}
