package jovic.dragan.pj2.controller;

import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.preferences.Constants;
import jovic.dragan.pj2.util.Util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class AerospaceMessageSender {

    public AerospaceMessageSender() {
        Util.createFolderIfNotExists(Constants.COMMANDS_FOLDER);
    }

    private void createCommandFile(String command) {
        try (FileOutputStream fos = new FileOutputStream(Paths.get(Constants.COMMANDS_FOLDER, command).toFile())) {
            fos.flush();
        } catch (IOException ex) {
            GenericLogger.log(this.getClass(), ex);
        }
    }

    public void banFlight() {
        createCommandFile(Constants.COMMAND_BAN_FLIGHT);
    }

    public void allowFlight() {
        createCommandFile(Constants.COMMAND_ALLOW_FLIGHT);
    }

}
