package jovic.dragan.pj2.gui;

import jovic.dragan.pj2.gui.components.CrashDetailsView;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Util;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class CrashesWindow extends JFrame {

    private JList<String> crashList;
    private CrashDetailsView detailsView;

    private JScrollPane scrollable;

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
        crashList = new JList<>(paths.toArray(new String[]{}));
        crashList.addListSelectionListener(e -> detailsView.setCrash(((JList<String>) e.getSource()).getSelectedValue()));
        crashList.setVisibleRowCount(Math.min(paths.size(), 10));
        detailsView.setPreferredSize(new Dimension(300, 200));
        scrollable = new JScrollPane(crashList);
        crashList.setAutoscrolls(true);
        scrollable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.setTitle("Prikaz sudara");
        this.setLayout(new BorderLayout(5, 5));
        //this.add(crashList, BorderLayout.WEST);
        this.add(scrollable, BorderLayout.WEST);
        this.add(detailsView, BorderLayout.CENTER);
        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
