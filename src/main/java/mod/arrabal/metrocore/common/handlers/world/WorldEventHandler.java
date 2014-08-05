package mod.arrabal.metrocore.common.handlers.world;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.arrabal.metrocore.common.library.LogHelper;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by Arrabal on 6/16/2014.
 */
public class WorldEventHandler {

    public WorldEventHandler() {}

    // Actions to take place when changing dimensions.  Clean up references to world objects.
    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event){
        LogHelper.debug("onWorldUnload event called");
        WorldGenerationHandler.clearUrbanGenerationMap();
    }

}
