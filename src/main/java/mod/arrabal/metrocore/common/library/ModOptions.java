package mod.arrabal.metrocore.common.library;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;

/**
 * Created by Arrabal on 10/9/14.
 */
public class ModOptions {
    public static final int BASE_DIST_BETWEEN_CITY = 1536;
    public static final String DEBUG_OFF = "off";
    public static final String DEBUG_ON = "on";
    public static final String DEBUG_TRACE = "trace";

    public static int metropolisMinDistanceBetween;
    public static int metropolisCenterSpawnShift;
    public static int metropolisMaxCitySpawnSpread;


    public static void init(){
        metropolisMinDistanceBetween = (int) ((1d/ ConfigHandler.metropolisGenDensity) * BASE_DIST_BETWEEN_CITY/2d); // blocks
        metropolisMaxCitySpawnSpread = 2 * (ConfigHandler.metropolisMaxGenRadius * 16) + 16;
        metropolisCenterSpawnShift = ((2 * metropolisMaxCitySpawnSpread) + metropolisMinDistanceBetween) >> 4; //blocks
    }
}
