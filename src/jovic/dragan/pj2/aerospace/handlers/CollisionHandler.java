package jovic.dragan.pj2.aerospace.handlers;

import jovic.dragan.pj2.Interfaces.Military;
import jovic.dragan.pj2.aerospace.Aerospace;
import jovic.dragan.pj2.aerospace.AerospaceObject;
import jovic.dragan.pj2.aerospace.MilitaryAircraft;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.radar.collisions.TextCollisionInfo;
import jovic.dragan.pj2.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CollisionHandler implements Consumer<WatchEvent> {

    private Aerospace aerospace;

    public CollisionHandler(Aerospace aerospace){
        this.aerospace = aerospace;
    }

    @Override
    public Consumer<WatchEvent> andThen(Consumer<? super WatchEvent> after) {
        return null;
    }

    @Override
    public void accept(WatchEvent watchEvent) {
        Path path = ((WatchEvent<Path>)watchEvent).context();
        TextCollisionInfo collisionInfo = null;
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Paths.get(Constants.ALERTS_FOLDER_PATH).resolve(path).toFile()));
            collisionInfo = (TextCollisionInfo)ois.readObject();
        }
        catch (IOException | ClassNotFoundException ex){
            GenericLogger.log(this.getClass(),ex);
        }
        if(collisionInfo!=null) {
            Pattern pattern = Pattern.compile("\\(x,y,z\\)=\\((\\d+),(\\d+),(\\d+)\\)",Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(collisionInfo.getPozicija());
            if(matcher.matches()){
                int x,y,altitude;
                x = Integer.parseInt(matcher.group(1));
                y = Integer.parseInt(matcher.group(2));
                altitude = Integer.parseInt(matcher.group(3));
                var collidedList = aerospace.getField(x,y).stream()
                        .filter(ao->ao.getAltitude()==altitude).collect(Collectors.toList());
                if(collidedList.stream().anyMatch(ao-> ((ao instanceof MilitaryAircraft) && ((MilitaryAircraft)ao).isForeign())) &&
                        collidedList.stream().anyMatch(ao-> (ao instanceof MilitaryAircraft) && !((MilitaryAircraft)ao).isForeign()))
                    System.out.println("Domaca vojska unistava stranu!");
                else
                    System.out.println("Sudar, uklanjam ih sa lokacije "+ collisionInfo.getPozicija());
                aerospace.clearPosition(x,y, altitude);
            }
        }
    }
}
