package net.minecraftforge.mercurius.utils;

import net.minecraftforge.mercurius.Config;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {
    private static Logger logger = null;
    private static String modID = null;

    public static void log(Level logLevel, String message) {
        if (logger == null)
        {
            modID = "mercurius"; //Should never change, but we could ask the binding.
            logger = LogManager.getLogger(modID);
        }
        logger.log(logLevel, "[" + modID + "] " + message);
    }

    public static void all(String message) {
        log(Level.ALL, message);
    }

    public static void debug(String message) {
        log(Level.DEBUG, message);
    }

    public static void trace(String message) {
        log(Level.TRACE, message);
    }

    public static void fatal(String message) {
        log(Level.FATAL, message);
    }

    public static void error(String message) {
        log(Level.ERROR, message);
    }

    public static void warn(String message) {
        log(Level.WARN, message);
    }

    public static void info(String message) {
        if(Config.spammyLogs) {
            log(Level.INFO, message);
        }
    }

    public static void off(String message) {
        log(Level.OFF, message);
    }

}
