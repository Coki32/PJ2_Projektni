package jovic.dragan.pj2.backup;

import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Backuper extends Thread {
    static final int BACKUP_INTERVAL = 60;

    private List<String> folders;

    private Backuper(String... folders) {
        Util.createFolderIfNotExists("./bkp/");
        this.folders = new ArrayList<>();
        this.folders.addAll(Arrays.asList(folders).stream().map(s -> s.substring(2)).collect(Collectors.toList()));
    }


    @Override
    public void run() {
        while (true) {
            LocalDateTime time = LocalDateTime.now();
            String name = "backup_" + time.getYear() + "_" + time.getMonth() + "_" + time.getDayOfMonth() + "_" + time.getHour() + "_" + time.getMinute();
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("./bkp/" + name + ".zip"))) {
                for (String folder : folders) {
                    zos.putNextEntry(new ZipEntry(folder + "/"));
                    Files.newDirectoryStream(Paths.get(folder)).forEach(path -> {
                        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(path.toFile()))) {
                            //System.out.println("Cuvam fajl " + path.toFile().toString());
                            zos.putNextEntry(new ZipEntry(folder + "/" + path.getFileName().toString()));
                            zos.write(fis.readAllBytes());
                            zos.closeEntry();
                        } catch (IOException ex) {
                            GenericLogger.log(this.getClass(), ex);
                        }
                    });
                    zos.closeEntry();
                }
            } catch (IOException ex) {
                GenericLogger.log(this.getClass(), ex);
            }
            try {
                Thread.sleep(BACKUP_INTERVAL * 1000);
            } catch (InterruptedException ex) {
                GenericLogger.log(this.getClass(), ex);
            }
        }
    }

    public static void main(String[] args) {
        Util.createFolderIfNotExists(Constants.ALERTS_FOLDER_PATH);
        Util.createFolderIfNotExists(Constants.PREFERENCES_FOLDERNAME);
        Util.createFolderIfNotExists(Constants.EVENTS_FOLDER_PATH);
        Util.createFolderIfNotExists(Constants.SIMULATOR_SHARED_FOLDERNAME);
        Backuper backuper = new Backuper(Constants.ALERTS_FOLDER_PATH,
                Constants.PREFERENCES_FOLDERNAME,
                Constants.EVENTS_FOLDER_PATH,
                Constants.SIMULATOR_SHARED_FOLDERNAME);
        backuper.start();
    }
}
