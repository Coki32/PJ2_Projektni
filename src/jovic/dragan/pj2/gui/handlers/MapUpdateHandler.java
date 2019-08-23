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
    private boolean gridEnabled;

    public MapUpdateHandler(MapViewer viewer, boolean gridEnabled) {
        this.viewer = viewer;
        this.gridEnabled = gridEnabled;
    }


    @Override
    public Consumer<WatchEvent> andThen(Consumer<? super WatchEvent> after) {
        return null;
    }

    private void drawGrid(int h, int w, int hStep, int wStep) {

    }

    @Override
    public void accept(WatchEvent watchEvent) {
        Path path = ((WatchEvent<Path>) watchEvent).context();
        try {
            List<String> lines = Files.readAllLines(Paths.get(Constants.SIMULATOR_SHARED_FOLDERNAME).resolve(path));
            if (lines.size() != 0) {
                Graphics g = viewer.getGraphics();
                viewer.paint(g);
                double h = viewer.getHeight(), w = viewer.getWidth();
                double mapHeight = viewer.getPreferences().getFieldHeight(),
                        mapWidth = viewer.getPreferences().getFieldWidth();
                int planeWidth = (int) Math.ceil(w / mapWidth), planeHeight = (int) Math.ceil(h / mapHeight);
                if (gridEnabled)
                    drawGrid((int) h, (int) w, planeHeight, planeWidth);
                for (String line : lines) {
                    String[] split = line.trim().split(",");
                    ObjectInfo info = new ObjectInfo(line.trim().split(","));
//                    Color color = Color.BLACK;
//
//                    if(info.isMilitary()) {
//                        color = info.isForeign() ? Color.RED : Color.BLUE;
//                    }
                    g.setColor(info.getDrawingColor());
                    g.fillRect((int) (w / mapWidth * info.getX()), (int) (h / mapHeight * info.getY()), planeWidth, planeHeight);
                }
                System.out.println(String.format("View:%f x %f, Mapa: %f x %f a velicina %d x %d",
                        h, w, mapHeight, mapWidth, planeHeight, planeWidth));
            }
        }
        catch (IOException ex){
            GenericLogger.log(this.getClass(),ex);
        }
    }
}
