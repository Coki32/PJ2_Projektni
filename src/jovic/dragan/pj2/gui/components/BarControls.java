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

    private JPanel buttonPanel;

    private Watcher eventWatcher;

    public BarControls() {
        bFlightControl = new JButton("Ban Flight");
        buttonPanel = new JPanel(new GridLayout(1, 2, 3, 3));
        bShowCrashes = new JButton("Show crashes");
        lLatestEvent = new JLabel("Latest events will show up here");

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
        bShowCrashes.addActionListener(ev -> {
            CrashesWindow cw = new CrashesWindow();
            cw.setVisible(true);
        });

        try {
            eventWatcher = new Watcher(Constants.EVENTS_FOLDER_PATH, StandardWatchEventKinds.ENTRY_MODIFY);
            eventWatcher.addEventHandler(StandardWatchEventKinds.ENTRY_MODIFY, ev -> {
                //noinspection unchecked
                lLatestEvent.setText("New event available, check the file named " + ((WatchEvent<Path>) ev).context().toFile().getName() + " in the events folder");
            });
            eventWatcher.start();
        } catch (IOException ex) {
            GenericLogger.log(this.getClass(), ex);
        }

        this.setLayout(new GridLayout(2, 1, 3, 3));

        buttonPanel.add(bFlightControl);
        buttonPanel.add(bShowCrashes);

        add(buttonPanel);
        add(lLatestEvent);
    }

}
