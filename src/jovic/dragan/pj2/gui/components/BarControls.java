package jovic.dragan.pj2.gui.components;

import jovic.dragan.pj2.controller.AerospaceMessageSender;
import jovic.dragan.pj2.gui.CrashesWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BarControls extends JPanel {

    private JButton bFlightControl;
    private JButton bShowCrashes;
    private JLabel lLatestEvent;

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
        add(bFlightControl);
        add(bShowCrashes);
        add(lLatestEvent);
    }

}
