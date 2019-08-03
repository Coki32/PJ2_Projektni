package jovic.dragan.pj2.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenericLogger {

    /**
     * Logs the exception with the default level of WARNING
     *
     * @param C class which has thrown the exception
     * @param ex exception that was thrown
     */
    public static  void log(Class<?> C, Exception ex){
        GenericLogger.log(C,Level.WARNING,ex.fillInStackTrace().toString(),ex);
    }

    public static void log(Class<?> C, Level level, String msg, Throwable thrown){
        Logger logger = Logger.getLogger(C.getName());
        if(logger.getHandlers().length==0)
            try {
                logger.addHandler(new FileHandler(C.getName() + ".log"));
            }
            catch (IOException exc){
                exc.printStackTrace();
            }
        logger.log(level,msg,thrown);
    }
}
