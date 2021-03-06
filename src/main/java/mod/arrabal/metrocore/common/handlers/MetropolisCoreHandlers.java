package mod.arrabal.metrocore.common.handlers;

import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.init.Biomes;
import mod.arrabal.metrocore.common.world.biome.CityBiomeManager;
import mod.arrabal.metrocore.common.world.gen.ModdedWorldProviderSurface;
import mod.arrabal.metrocore.network.GuiHandler;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.handlers.world.TerrainGenEventHandler;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Arrabal on 2/19/14.
 */
public class MetropolisCoreHandlers {

    public static void preInit() {
        //GameRegistry.registerWorldGenerator(new WorldGenerationHandler(), 1000000);
        registerNetworkEventHandlers();
        registerWorldEventHandlers();
        registerFMLEventHandlers();

    }

    public static void init(){
        registerGuiHandlers();
        swapOverworldWorldProvider();
    }

    public static void postInit(){
        registerBiomes();
        CityBiomeManager.init();
    }

    private static void registerNetworkEventHandlers() {

    }

    private static void registerWorldEventHandlers() {
        MinecraftForge.TERRAIN_GEN_BUS.register(new TerrainGenEventHandler());
    }

    private static void registerFMLEventHandlers(){
        MinecraftForge.EVENT_BUS.register(new ConfigHandler());
    }

    private static void registerGuiHandlers(){
        NetworkRegistry.INSTANCE.registerGuiHandler(MetropolisCore.instance, new GuiHandler());
    }

    private static void swapOverworldWorldProvider(){

        int[] dimensionIds;

        dimensionIds = DimensionManager.unregisterProviderType(0);
        DimensionManager.registerProviderType(0, ModdedWorldProviderSurface.class, false);
        if (dimensionIds.length > 0){
            for (int i = 0; i < dimensionIds.length; ++i){
                DimensionManager.registerProviderType(0, ModdedWorldProviderSurface.class, false);
            }
        }
    }

    private static void registerBiomes(){
        BiomeDictionary.registerBiomeType(Biomes.plainsMetro, BiomeDictionary.Type.PLAINS);
    }

}
