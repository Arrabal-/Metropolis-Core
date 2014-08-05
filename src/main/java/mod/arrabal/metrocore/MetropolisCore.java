package mod.arrabal.metrocore;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import mod.arrabal.metrocore.common.block.Blocks;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.handlers.MetropolisCoreHandlers;
import mod.arrabal.metrocore.common.item.Items;
import mod.arrabal.metrocore.common.library.CreativeTabsMetroCore;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.library.ModRef;
import mod.arrabal.metrocore.proxy.IProxy;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by Arrabal on 12/19/13.
 */

@Mod(modid = ModRef.MOD_ID, name = ModRef.MOD_NAME, version = ModRef.VERSION, guiFactory = ModRef.GUI_FACTORY_CLASS)

public class MetropolisCore {
    //Instance of the mod used by Forge
    @Mod.Instance(ModRef.MOD_ID)
    public static MetropolisCore instance;

    //Where the client and server proxy code is loaded
    @SidedProxy(modId = ModRef.MOD_ID, clientSide = ModRef.CLIENT_PROXY_LOCATION, serverSide = ModRef.COMMON_PROXY_LOCATION)
    public static IProxy proxy;

    public static CreativeTabs tabMetroWorld;
    public static String configPath;

    //Actions taken prior to loading mods
    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        configPath = event.getModConfigurationDirectory() + "/Metropolis/";
        ConfigHandler.init(configPath);
        MetropolisCoreHandlers.init();
        tabMetroWorld = new CreativeTabsMetroCore(CreativeTabs.getNextID(), "tabMetroWorld");
        Blocks.init();
        Items.init();
        proxy.registerSounds();
        proxy.registerRenderers();
    }

    //Actions taken during loading of mods
    @Mod.EventHandler
    public static void Init(FMLInitializationEvent event) {
        Blocks.oreRegistration();


    }

    //Actions taken after mods have loaded
    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {

    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event){
        LogHelper.debug("onServerStopping event called");
    }
}
