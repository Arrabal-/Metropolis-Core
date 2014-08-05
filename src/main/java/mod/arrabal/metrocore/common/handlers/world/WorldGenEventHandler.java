package mod.arrabal.metrocore.common.handlers.world;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.arrabal.metrocore.common.world.structure.MapGenUrban;
import net.minecraft.block.Block;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

/**
 * Created by Arrabal on 2/26/14.
 */
public class WorldGenEventHandler {

    private static MapGenUrban urbanGenerator;
    private Block[] blocks = null;

    @SubscribeEvent
    public void onPopulatePreEvent(PopulateChunkEvent.Pre event){


        switch (event.world.provider.dimensionId){
            case -1:
                break;
            case 0:
                //DebugMessenger.message("Captured PopulateChunk pre event.  Ready to hijack to run urbanGenerator.generate.");
                urbanGenerator = MapGenEventHandler.getUrbanGenerator();
                //TODO:  urbanGenerator.generate()
                urbanGenerator.func_151539_a(event.chunkProvider, event.world, event.chunkX, event.chunkZ, blocks);
                break;
            case 1:
                break;
            default:
        }
        /*
        if (event.chunkProvider instanceof ChunkProviderGenerate || event.chunkProvider instanceof ChunkProviderFlat){
            urbanGenerator = MapGenEventHandler.getUrbanGenerator();
            //World world = something that does not return null!
            DebugMessenger.message("Insert Stop");
            urbanGenerator.generate(event.chunkProvider, event.world, event.chunkX, event.chunkZ, blocks);
        }
        else if (event.chunkProvider instanceof ChunkProviderEnd){
            DebugMessenger.message("ReplaceBiomeBlocks event captured for End.");
        }
        else if (event.chunkProvider instanceof ChunkProviderHell){
            DebugMessenger.message("ReplaceBiomeBlocks event captured for Nether.");
        }
        */
    }

    @SubscribeEvent
    public void onPopulatePostEvent(PopulateChunkEvent.Post event){

        //DebugMessenger.message("Captured populate post event.  Ready to inject urbanGenerator.generateStructuresInChunk.");

        switch (event.world.provider.dimensionId){
            case -1:

                break;
            case 0:
                if (event.hasVillageGenerated){
                    //DebugMessenger.message("Village generated.  Skipping urban generation.");
                    return;
                }
                //urbanGenerator.generateStructuresInChunk(event.world, event.rand, event.chunkX, event.chunkZ);
                break;
            case 1:

                break;
        }
    }
}
