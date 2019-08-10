package jovic.dragan.pj2.gui;

import javax.swing.*;

public class MainWindow extends JFrame {

    private MapViewer viewer;

    public MainWindow(){
        super("Gui boi");
        this.setSize(850,650);
        viewer = new MapViewer(800, 600);
        this.add(viewer);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
