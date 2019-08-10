package jovic.dragan.pj2.gui.handlers;

import jovic.dragan.pj2.gui.MapViewer;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.radar.ObjectInfo;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class MapUpdateHandler implements Consumer<WatchEvent> {

    private MapViewer viewer;

    public MapUpdateHandler(MapViewer viewer){
        this.viewer = viewer;
    }


    @Override
    public Consumer<WatchEvent> andThen(Consumer<? super WatchEvent> after) {
        return null;
    }

    @Override
    public void accept(WatchEvent watchEvent) {
        Path path = ((WatchEvent<Path>) watchEvent).context();
        try {
            List<String> lines = Files.readAllLines(Paths.get(Constants.SIMULATOR_SHARED_FOLDERNAME).resolve(path));
            if (lines.size() != 0) {
                Graphics g = viewer.getGraphics();
                viewer.paint(g);
                int h = viewer.getHeight(), w = viewer.getWidth();
                int mapHeight = viewer.getPreferences().getFieldHeight(),
                        mapWidth = viewer.getPreferences().getFieldWidth();
                int planeWidth = (int)Math.ceil(w/mapWidth) - 1, planeHeight = (int)Math.ceil(h/mapHeight) - 1;
                for (String line : lines) {
                    ObjectInfo info = new ObjectInfo(line.split(","));
                    Color color = Color.BLACK;
                    if(info.isMilitary()) {
                        color = info.isForeign() ? Color.RED : Color.BLUE;
                    }
                    g.setColor(color);
                    g.fillRect(w/mapWidth*info.getX(), h/mapHeight*info.getY(),planeWidth,planeHeight);
                }

            }
        }
        catch (IOException ex){
            GenericLogger.log(this.getClass(),ex);
        }
    }
}
