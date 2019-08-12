package jovic.dragan.pj2.gui.components;

import jovic.dragan.pj2.gui.CrashesWindow;

import javax.swing.*;
import java.awt.*;

public class BarControls extends JPanel {

    private JButton bFlightControl;
    private JButton bShowCrashes;
    private JLabel lLatestEvent;

    public BarControls() {
        this.setLayout(new GridLayout(2, 1, 3, 3));
        bFlightControl = new JButton("BanFlight");
        bShowCrashes = new JButton("Show crashes");
        bShowCrashes.addActionListener(ev -> {
            CrashesWindow cw = new CrashesWindow();
            cw.setVisible(true);
        });
        lLatestEvent = new JLabel("Latest events will show up here");
        add(bFlightControl);
        add(bShowCrashes);
        add(lLatestEvent);
    }

}
