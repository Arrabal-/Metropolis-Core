package mod.arrabal.metrocore.common.handlers.world;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.init.Biomes;
import mod.arrabal.metrocore.common.world.biome.MetropolisBiomeDecorator;
import mod.arrabal.metrocore.common.world.gen.MetropolisChunkProviderGenerate;
import mod.arrabal.metrocore.common.world.gen.ModdedBiomeDecorator;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.terraingen.*;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Arrabal on 6/24/2014.
 */
public class TerrainGenEventHandler {

    //private static boolean decoratingCityAreaEvent = false;

    public TerrainGenEventHandler() {}

    //public static void decoratingCityArea(boolean setTrue) {
    //    decoratingCityAreaEvent = setTrue;
    //}

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onCreateDecoratorEvent(BiomeEvent.CreateDecorator event){
        //event.newBiomeDecorator = new MetropolisBiomeDecorator();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDecorateBiomeEvent(DecorateBiomeEvent.Decorate event){
        if (event.type != DecorateBiomeEvent.Decorate.EventType.CUSTOM){
            if (event.type == DecorateBiomeEvent.Decorate.EventType.GRASS && event.world.getBiomeGenForCoords(event.pos) == Biomes.plainsMetro){
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public void onPopulateChunkEvent(PopulateChunkEvent.Populate event){
        if (event.type == PopulateChunkEvent.Populate.EventType.LAKE || event.type == PopulateChunkEvent.Populate.EventType.LAVA){
            BlockPos blockpos = new BlockPos(event.chunkX * 16, 0, event.chunkZ * 16);
            BiomeGenBase biomeGenBase = event.world.getBiomeGenForCoords(blockpos.add(16, 0, 16));
            if (biomeGenBase == Biomes.plainsMetro) event.setResult(Event.Result.DENY);
        }

    }

    @SubscribeEvent
    public void onPostPopulateChunkEvent(PopulateChunkEvent.Post event){

    }
}
