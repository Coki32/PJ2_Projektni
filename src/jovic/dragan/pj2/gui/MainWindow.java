package jovic.dragan.pj2.gui;

import jovic.dragan.pj2.gui.components.BarControls;
import jovic.dragan.pj2.gui.components.MapViewer;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private MapViewer viewer;
    private BarControls barControls;
    public MainWindow(){
        super("Gui boi");
        this.setLayout(new BorderLayout(3, 3));
        this.setSize(850,650);
        viewer = new MapViewer(800, 600);
        barControls = new BarControls();
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
