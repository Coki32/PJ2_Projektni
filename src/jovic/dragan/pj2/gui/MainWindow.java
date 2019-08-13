package jovic.dragan.pj2.gui;

import jovic.dragan.pj2.gui.components.BarControls;
import jovic.dragan.pj2.gui.components.MapViewer;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Util;
import jovic.dragan.pj2.util.Watcher;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

public class MainWindow extends JFrame {

    private MapViewer viewer;
    private BarControls barControls;

    private Watcher crashWatcher;

    public MainWindow() {
        try {
            Util.createFolderIfNotExists(Constants.ALERTS_FOLDER_PATH);//Sad stvarno ne moze biti exception...
            crashWatcher = new Watcher(Constants.ALERTS_FOLDER_PATH, StandardWatchEventKinds.ENTRY_MODIFY);
            crashWatcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY, ev -> {
                Path path = ((WatchEvent<Path>) ev).context();
                new CrashPopup(Paths.get(Constants.ALERTS_FOLDER_PATH).resolve(path).toString());
            });
            crashWatcher.start();
        } catch (IOException ex) {
            GenericLogger.log(this.getClass(), ex);
        }
        this.setLayout(new BorderLayout(3, 3));
        this.setSize(850, 650);
        viewer = new MapViewer(800, 600);
        barControls = new BarControls();
        this.setTitle("Java vazdusni prostor");
        this.add(viewer, BorderLayout.CENTER);
        this.add(barControls, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}