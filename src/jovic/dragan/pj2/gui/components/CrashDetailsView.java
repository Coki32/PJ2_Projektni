package jovic.dragan.pj2.gui.components;

import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.radar.collisions.CollisionInfo;
import jovic.dragan.pj2.radar.collisions.TextCollisionInfo;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class CrashDetailsView extends JPanel {
    private JLabel lTime, lDescription, lPosition;
    private JTextArea taTime, taDescription, taPosition;

    public CrashDetailsView() {
        this(new TextCollisionInfo(new CollisionInfo(-1, -1, -1)));
    }

    public CrashDetailsView(TextCollisionInfo info) {
        lTime = new JLabel("Vrijeme:");
        lDescription = new JLabel("Opis:");
        lPosition = new JLabel("Pozicija:");

        String time = "", description = "", position = "";

        if (info.getIDs() != null) {
            time = info.getVrijeme();
            description = info.getOpis();
            position = info.getPozicija();
        }

        taTime = new JTextArea(time);
        taTime.setEnabled(false);
        taTime.setMinimumSize(new Dimension(300, 30));
        taDescription = new JTextArea(description);
        taDescription.setEnabled(false);
        taDescription.setMinimumSize(new Dimension(300, 30));

        taPosition = new JTextArea(position);
        taPosition.setEnabled(false);
        taPosition.setMinimumSize(new Dimension(300, 30));

        this.setLayout(new GridLayout(6, 1, 5, 5));
        this.add(lTime);
        this.add(taTime);

        this.add(lPosition);
        this.add(taPosition);

        this.add(lDescription);
        this.add(taDescription);

        this.setSize(300, 200);

    }

    public void setCrash(String path) {
        try {
            TextCollisionInfo tci = (TextCollisionInfo) (new ObjectInputStream(new FileInputStream(path)).readObject());
            taTime.setText(tci.getVrijeme());
            taDescription.setText(tci.getOpis());
            taPosition.setText(tci.getPozicija());
        } catch (IOException | ClassNotFoundException ex) {
            GenericLogger.log(this.getClass(), ex);//jeeebiga nece nikad...
        }
    }

}
