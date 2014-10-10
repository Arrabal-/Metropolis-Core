package mod.arrabal.metrocore.common.handlers;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.handlers.world.TerrainGenEventHandler;
import mod.arrabal.metrocore.common.handlers.world.WorldGenerationHandler;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Arrabal on 2/19/14.
 */
public class MetropolisCoreHandlers {

    public static void init() {
        GameRegistry.registerWorldGenerator(new WorldGenerationHandler(), 1000000);
        registerNetworkEventHandlers();
        registerWorldEventHandlers();
        registerFMLEventHandlers();

    }

    private static void registerNetworkEventHandlers() {

    }

    private static void registerWorldEventHandlers() {
        MinecraftForge.TERRAIN_GEN_BUS.register(new TerrainGenEventHandler());
    }

    private static void registerFMLEventHandlers(){
        FMLCommonHandler.instance().bus().register(new ConfigHandler());
    }


}
