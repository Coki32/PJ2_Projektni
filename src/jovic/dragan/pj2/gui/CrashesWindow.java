package jovic.dragan.pj2.gui;

import jovic.dragan.pj2.gui.components.CrashDetailsView;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Util;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class CrashesWindow extends JFrame {

    private JList<String> crashList;
    private CrashDetailsView detailsView;

    public CrashesWindow() {
        detailsView = new CrashDetailsView();
        Util.createFolderIfNotExists(Constants.ALERTS_FOLDER_PATH);
        List<String> paths = new ArrayList<>();
        try {
            Files.newDirectoryStream(Paths.get(Constants.ALERTS_FOLDER_PATH)).forEach(p -> paths.add(p.toString()));
        } catch (IOException ex) {
            GenericLogger.log(this.getClass(), ex);
            paths.add("Nije moguce otvoriti folder, pokusajte ponovo");
        }
        crashList = new JList<String>(paths.toArray(new String[]{}));
        crashList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                detailsView.setCrash(((JList<String>) e.getSource()).getSelectedValue());

            }
        });
        crashList.setPreferredSize(new Dimension(200, 200));
        detailsView.setPreferredSize(new Dimension(300, 200));
        this.setTitle("Prikaz sudara");
        this.setLayout(new BorderLayout(5, 5));
        this.add(crashList, BorderLayout.WEST);
        this.add(detailsView, BorderLayout.CENTER);
        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
