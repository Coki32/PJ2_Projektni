package jovic.dragan.pj2.backup;

import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Backuper extends Thread {
    static final int BACKUP_INTERVAL = 60;

    private List<String> folders;

    private Backuper(String... folders) {
        Util.createFolderIfNotExists("./bkp/");
        this.folders = new ArrayList<>();
        //substring jer su folderi ./, a to u zipu napravi folder ".", to nije... fino
        this.folders.addAll(Arrays.asList(folders));
    }


    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            LocalDateTime time = LocalDateTime.now();
            String name = "backup_" + time.getYear() + "_" + time.getMonth() + "_" + time.getDayOfMonth() + "_" + time.getHour() + "_" + time.getMinute();
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(Constants.BACKUP_FOLDERNAME + File.separator + name + ".zip"))) {
                for (String folder : folders) {
                    //substring jer folderi pocinju sa ./ a to u zipu napravi folder "." sto nije... fino
                    zos.putNextEntry(new ZipEntry(folder.substring(2) + "/"));//Zip mora imati /, ne moze biti \
                    Files.newDirectoryStream(Paths.get(folder)).forEach(path -> {
                        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(path.toFile()))) {
                            zos.putNextEntry(new ZipEntry(folder.substring(2) + "/" + path.getFileName().toString()));
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
        System.out.println("Pokrenut backup utility");
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
