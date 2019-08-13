package jovic.dragan.pj2.gui;

import jovic.dragan.pj2.gui.components.CrashDetailsView;

import javax.swing.*;
import java.awt.*;

public class CrashPopup extends JFrame {

    private CrashDetailsView info;

    public CrashPopup(String filename) {

        info = new CrashDetailsView();
        info.setCrash(filename);
        this.setTitle("Sudar");
        this.setPreferredSize(new Dimension(300, 300));
        this.add(info);
        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}
