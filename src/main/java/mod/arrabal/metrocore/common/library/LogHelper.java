package mod.arrabal.metrocore.common.library;

import net.minecraftforge.fml.common.FMLLog;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import org.apache.logging.log4j.Level;

/**
 * Created by Arrabal on 6/27/2014.
 */
public class LogHelper {

    public static void log(Level logLevel, Object object){
        FMLLog.log(ModRef.MOD_NAME, logLevel, String.valueOf(object));
    }

    public static void all(Object object){
        log(Level.ALL, object);
    }

    public static void debug(Object object){
        if (ConfigHandler.enableDebugMessages == ModOptions.DEBUG_ON){
            FMLLog.log(ModRef.MOD_NAME, Level.INFO, "[DEBUG]: " + String.valueOf(object));
        }
    }

    public static void error(Object object){
        log(Level.ERROR, object);
    }

    public static void fatal(Object object){
        log(Level.FATAL, object);
    }

    public static void info(Object object){
        log(Level.INFO, object);
    }

    public static void off(Object object){
        log(Level.OFF, object);
    }

    public static void trace(Object object){
        if (ConfigHandler.enableDebugMessages == ModOptions.DEBUG_TRACE){
            FMLLog.log(ModRef.MOD_NAME, Level.INFO, "[TRACE]: " + String.valueOf(object));
        }
    }

    public static void warn(Object object){
        log(Level.WARN, object);
    }
}
