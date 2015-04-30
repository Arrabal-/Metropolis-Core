package mod.arrabal.metrocore.common.handlers;

import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.world.gen.ModdedMapGenCaves;
import mod.arrabal.metrocore.common.world.gen.ModdedWorldProviderSurface;
import mod.arrabal.metrocore.network.GuiHandler;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.handlers.world.TerrainGenEventHandler;
import mod.arrabal.metrocore.common.handlers.world.WorldGenerationHandler;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

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

    private static void registerNetworkEventHandlers() {

    }

    private static void registerWorldEventHandlers() {
        MinecraftForge.TERRAIN_GEN_BUS.register(new TerrainGenEventHandler());
    }

    private static void registerFMLEventHandlers(){
        FMLCommonHandler.instance().bus().register(new ConfigHandler());
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

}
