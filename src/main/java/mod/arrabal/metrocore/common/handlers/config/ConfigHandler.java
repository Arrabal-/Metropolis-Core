package mod.arrabal.metrocore.common.handlers.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.File;

/**
 * Created by Arrabal on 12/19/13.
 */
public class ConfigHandler {

    public static File worldFile;

    public static Configuration config;

    //configuration variables
    public static boolean overrideCraftingModDependency;
    public static int enableDebugMessages;
    public static boolean enableCityCreation;
    public static int metropolisMinDistanceBetween;
    public static int metropolisMaxDistanceBetween;
    public static int metropolisMinGenRadius;
    public static int metropolisMaxGenRadius;
    public static double metropolisGenDensity;
    public static double metropolisGenRarity;
    public static int metropolisSpawnBlockRadius;
    public static double ruinedCityPercent;

    public static void init(String configPath) {
        if (config == null) {
            worldFile = new File(configPath + "MetropolisCore.cfg");
            config = new Configuration(worldFile);
            loadConfiguration();
        }
    }

   private static void loadConfiguration() {

        try {
            //mod options
            overrideCraftingModDependency = config.get(ModRef.CATEGORY_MOD_MECHANICS, "DependencyOverrides", false,
                    "Enable to allow crafting recipes without MetropolisResources mod.  Having mod installed deprecates this setting").getBoolean(false);
            enableDebugMessages = config.get(ModRef.CATEGORY_MOD_MECHANICS, "EnableDebugMessages", 0,
                    "Enable to generate internal debug messages.  0 - disables debug messages; 1 - enables debug messages in the log; " +
                            "2 - enables debug and trace messages in the log.  Probably don't want to do this unless you like console spam.").getInt(0);
            enableCityCreation = config.get(ModRef.CATEGORY_MOD_MECHANICS, "EnableCityGeneration", true,
                    "Disable to prevent ANY cities from spawning in the world.  This is a core mod feature, so only disable if you really mean it.").getBoolean(true);

            // Add configuration options here
            metropolisSpawnBlockRadius = config.get(ModRef.CATEGORY_CITY_GENERATION, "SpawnAreaBlocked", 160,
                    "Sets the radius (in BLOCKS) of the area around the spawn point that is marked as invalid for city generation").getInt(160);
            metropolisMinDistanceBetween = config.get(ModRef.CATEGORY_CITY_GENERATION, "MinDistanceBetween", 32,
                    "Sets the minimum allowable distance (in CHUNKS) between cities that is valid for generation").getInt(32);
            metropolisMaxDistanceBetween = config.get(ModRef.CATEGORY_CITY_GENERATION, "MaxDistanceBetween", 75,
                    "Sets the maximum allowable distance (in CHUNKS) between cities that is valid for generation.  Currently " +
                            "does not function properly--changing this value will influence spawn distance but does not cap it.").getInt(75);
            metropolisMinGenRadius = config.get(ModRef.CATEGORY_CITY_GENERATION, "MinGenRadius", 2,
                    "Sets the minimum allowable size (radius), in CHUNKS, for a generated city").getInt(2);
            metropolisMaxGenRadius = config.get(ModRef.CATEGORY_CITY_GENERATION, "MaxGenRadius", 5,
                    "Sets the maximum allowable size (radius), in CHUNKS, for a generated city").getInt(5);
            metropolisGenDensity = config.get(ModRef.CATEGORY_CITY_GENERATION, "GenDensity", 0.5d,
                    "Determines the frequency with with attemps at city generation are attempted during world generation.  " +
                            "Set value between 0 and 1; a value of 0 will prevent any attempt at city generation and a value of 1 will" +
                            "always test for valid city generation locations").getDouble(0.5d);
            metropolisGenRarity = config.get(ModRef.CATEGORY_CITY_GENERATION, "GenRarity", 0.5d,
                    "Determines the probability that valid city generation locations will be skipped.  Set value between 0 and 1; " +
                            "a value of 0 will make ANY city generation highly unlikely").getDouble(0.5d);
            ruinedCityPercent = config.get(ModRef.CATEGORY_CITY_GENERATION, "RuinPopulation", 0.10d,
                    "Sets the approximate % of generated urban areas that will be heavily damaged (think Mad Max)." +
                            "  Set value from 0 (no ruined cities) to 1 (all cities ruined)").getDouble(0.10d);

            LogHelper.info("Loaded config file");
        } catch (Exception e) {
            LogHelper.error("MetropolisCore has had a problem loading its configuration");
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event){
        if (event.modID.equalsIgnoreCase(ModRef.MOD_ID)){
            //Resync configs
            loadConfiguration();
        }
    }
}
