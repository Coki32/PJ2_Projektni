package jovic.dragan.pj2.gui.components;

import jovic.dragan.pj2.controller.AerospaceMessageSender;
import jovic.dragan.pj2.gui.CrashesWindow;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Watcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

public class BarControls extends JPanel {

    private JButton bFlightControl;
    private JButton bShowCrashes;
    private JLabel lLatestEvent;

    private Watcher eventWatcher;

    public BarControls() {
        this.setLayout(new GridLayout(2, 1, 3, 3));
        bFlightControl = new JButton("Ban Flight");
        bFlightControl.addActionListener(new ActionListener() {
            private AerospaceMessageSender sender = new AerospaceMessageSender();
            boolean shouldBan = true;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (shouldBan) {
                    sender.banFlight();
                    ((JButton) e.getSource()).setText("Allow flight");
                } else {
                    sender.allowFlight();
                    ((JButton) e.getSource()).setText("Ban Flight");
                }
                shouldBan = !shouldBan;
            }
        });
        bShowCrashes = new JButton("Show crashes");
        bShowCrashes.addActionListener(ev -> {
            CrashesWindow cw = new CrashesWindow();
            cw.setVisible(true);
        });
        lLatestEvent = new JLabel("Latest events will show up here");
        try {
            eventWatcher = new Watcher(Constants.EVENTS_FOLDER_PATH, StandardWatchEventKinds.ENTRY_MODIFY);
            eventWatcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY, ev -> {
                lLatestEvent.setText("New event available, check the file named " + ((WatchEvent<Path>) ev).context().toFile().getName() + " in the events folder");
            });
            eventWatcher.start();
        } catch (IOException ex) {
            GenericLogger.log(this.getClass(), ex);
        }
        add(bFlightControl);
        add(bShowCrashes);
        add(lLatestEvent);
    }

}
