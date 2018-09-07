package net.montoyo.mcef.utilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

/**
 * A set of functions to log messages into the MCEF log channel.
 * @author montoyo
 *
 */
public class Log {
    
    public static void info(String what, Object ... data) {
        LogManager.getLogger("MCEF").log(Level.INFO, String.format(what, data));
    }
    
    public static void warning(String what, Object ... data) {
        LogManager.getLogger("MCEF").log(Level.WARN, String.format(what, data));
    }
    
    public static void error(String what, Object ... data) {
        LogManager.getLogger("MCEF").log(Level.ERROR, String.format(what, data));
    }

}
