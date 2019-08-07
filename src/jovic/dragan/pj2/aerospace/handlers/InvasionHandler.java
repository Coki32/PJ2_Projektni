package jovic.dragan.pj2.aerospace.handlers;

import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.aerospace.FighterPlane;
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
            ObjectInfo invader = new ObjectInfo(Files.readString(Paths.get(Constants.EVENTS_FOLDER_PATH).resolve(path)).split(","));
            Direction invaderDirection = invader.getDirection();
            Direction defenseDirection = null;//Direction za (x1,y1), za (x2,y2) je .opposite
            aerospace.banFlight();
            int x1,x2,y1,y2;
            if(invaderDirection == Direction.UP || invaderDirection == Direction.DOWN) {
                x1 = x2 = invader.getX();
                y1 = 0;
                y2 = aerospace.getPreferences().getFieldHeight();
                defenseDirection = Direction.UP;
            }
            else {
                y1 = y2 = invader.getY();
                x1 = 0;
                x2 = aerospace.getPreferences().getFieldWidth();
                defenseDirection = Direction.RIGHT;
            }
            System.out.println("Odbrana spawnuje dva aviona na ("+x1+","+y1+") i ("+x2+","+y2+")");
            aerospace.addAerospaceObject(new FighterPlane(x1,y1,invader.getAltitude(),1,defenseDirection));
            aerospace.addAerospaceObject(new FighterPlane(x2,y2,invader.getAltitude(),1,defenseDirection.opposite()));
        }
        catch (IOException ex){
            GenericLogger.log(this.getClass(),ex);
        }
    }
}
