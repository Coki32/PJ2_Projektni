package jovic.dragan.pj2.gui.handlers;

import jovic.dragan.pj2.gui.components.MapViewer;
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
import java.util.function.Consumer;

public class MapUpdateHandler implements Consumer<WatchEvent> {

    private MapViewer viewer;
    private long lastDraw = 0;

    public MapUpdateHandler(MapViewer viewer) {
        this.viewer = viewer;
    }


    @Override
    public Consumer<WatchEvent> andThen(Consumer<? super WatchEvent> after) {
        return null;
    }

    @Override
    public void accept(WatchEvent watchEvent) {
        if (((System.currentTimeMillis() - lastDraw) > 500)) {
            Path path = ((WatchEvent<Path>) watchEvent).context();
            try {
                int count = 0;
                List<String> lines = Files.readAllLines(Paths.get(Constants.SIMULATOR_SHARED_FOLDERNAME).resolve(path));
                if (lines.size() != 0) {
                    Graphics g = viewer.getGraphics();
                    viewer.paint(g);
                    double h = viewer.getHeight(), w = viewer.getWidth();
                    double mapHeight = viewer.getPreferences().getFieldHeight(),
                            mapWidth = viewer.getPreferences().getFieldWidth();
                    int planeWidth = (int) Math.ceil(w / mapWidth), planeHeight = (int) Math.ceil(h / mapHeight);
                    for (String line : lines) {
                        String[] split = line.trim().split(",");
                        ObjectInfo info = null;
                        try {
                            info = new ObjectInfo(split);
                        } catch (Exception ex) {//fajl moze biti "corrupted" pa se info ne procita ispravno
                            //A exceptioni su od IndexOutOfBounds, null pointer, sve, ovo je blanket za sve
                            GenericLogger.log(this.getClass(), ex);
                        }
                        if (info != null) {
                            g.setColor(info.getDrawingColor());
                            g.fillRect((int) (w / mapWidth * info.getX()), (int) (h / mapHeight * info.getY()), planeWidth, planeHeight);
                            count++;
                        }
                    }
                    lastDraw = System.currentTimeMillis();
                    //System.out.println("Nacrtao sam " + count + " aviona");
                }
            } catch (IOException ex) {
                GenericLogger.log(this.getClass(), ex);
            }
        }
    }
}
