package jovic.dragan.pj2.logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenericLogger {

    private static Map<String, Handler> handlers = new HashMap<>();

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
                if(!handlers.containsKey(C.getName())) {
                    Handler handler = new FileHandler(C.getName()+".log");
                    handlers.put(C.getName(),handler);
                    logger.addHandler(handler);
                }
            }
            catch (IOException exc){
                exc.printStackTrace();
            }
        logger.log(level,msg,thrown);
    }

    public static void closeHandlers(){
        for (Handler handler : handlers.values()) {
            handler.close();
        }
    }
}
