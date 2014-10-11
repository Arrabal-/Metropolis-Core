package mod.arrabal.metrocore.common.library;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;

/**
 * Created by Arrabal on 10/9/14.
 */
public class ModOptions {
    public static final int BASE_DIST_BETWEEN_CITY = 96;
    public static final String DEBUG_OFF = "off";
    public static final String DEBUG_ON = "on";
    public static final String DEBUG_TRACE = "trace";

    public static int metropolisMinDistanceBetween;
    public static int metropolisAxisSpawnLength;
    public static int metropolisAxisSpawnShift;
    public static int metropolisCenterSpawnShift;
    public static int metropolisMinCitySpawnSpread;


    public static void init(){
        metropolisMinDistanceBetween = (int) ((1d/ ConfigHandler.metropolisGenDensity) * BASE_DIST_BETWEEN_CITY/2d);
        metropolisAxisSpawnLength = (int) Math.sqrt((metropolisMinDistanceBetween*metropolisMinDistanceBetween)/2d) +1;
        metropolisMinCitySpawnSpread = 2*ConfigHandler.metropolisMaxGenRadius + 16;
        metropolisAxisSpawnShift = (int) Math.sqrt((metropolisMinCitySpawnSpread*metropolisMinCitySpawnSpread)/2d);
        metropolisCenterSpawnShift = (metropolisAxisSpawnLength - metropolisAxisSpawnShift)/2;
    }
}
