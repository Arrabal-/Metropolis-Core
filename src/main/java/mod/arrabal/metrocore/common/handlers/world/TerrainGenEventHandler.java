package mod.arrabal.metrocore.common.handlers.world;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

/**
 * Created by Evan on 6/24/2014.
 */
public class TerrainGenEventHandler {

    public TerrainGenEventHandler() {}

    @SubscribeEvent
    public void onPopulatePostEvent(PopulateChunkEvent.Post event){

        //DebugMessenger.message("Captured populate post event.  Ready to inject MetropolisStart.generate.");

        switch (event.world.provider.dimensionId){
            case -1:
                // nether
                break;
            case 0:
                // overworld
                WorldGenerationHandler.doCityGeneration(event.world, event.chunkX, event.chunkZ);
                break;
            case 1:
                // the end
                break;
        }
    }
}
