package mod.arrabal.metrocore.common.handlers.world;

import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import mod.arrabal.metrocore.common.world.cities.*;
import mod.arrabal.metrocore.common.world.gen.*;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.*;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Arrabal on 6/24/2014.
 */
public class TerrainGenEventHandler {

    public TerrainGenEventHandler() {}

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPopulateEvent(PopulateChunkEvent.Populate event){
        if (event.type == PopulateChunkEvent.Populate.EventType.LAKE || event.type == PopulateChunkEvent.Populate.EventType.LAVA){
            if (generatingInCityArea(event.world, event.chunkX >> 4, event.chunkZ >> 4)){
                event.setResult(Event.Result.DENY);
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onInitMapGenEvent(InitMapGenEvent event){
        switch (event.type){
            case CAVE:{
                MetropolisGenerationContainer cityGenContainer = new MetropolisGenerationContainer();
                MapGenMetropolis metropolisGenerator = new MapGenMetropolis();
                cityGenContainer.cityGenerator = (MapGenMetropolis) TerrainGen.getModdedMapGen(metropolisGenerator, InitMapGenEvent.EventType.CUSTOM);
                event.newGen = new ModdedMapGenCaves();
                // set new generator to modified cave generator which will exclude to section of city chunks
                // use modified cave generator to initiate city generation in valid chunks
                break;
            }
            case VILLAGE:{
                // set new generator to modified village generator which will exclude city chunks
                event.newGen = new ModdedMapGenVillage();
                break;
            }
            case SCATTERED_FEATURE:{
                // set new generator to modified feature generator which will exclude city chunks
                event.newGen = new ModdedMapGenScatteredFeature();
                break;
            }
            case RAVINE:{
                // set new generator to modified ravine generator which will exclude city chunks
                event.newGen = new ModdedMapGenRavine();
                break;
            }
        }
        //if (event.type == InitMapGenEvent.EventType.VILLAGE) event.newGen = new OverrideMapGenVillage();
        //if (event.type == InitMapGenEvent.EventType.SCATTERED_FEATURE) event.newGen = new OverrideMapGenScatteredFeature();
    }

/*    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDecorateBiomeEvent(DecorateBiomeEvent.Decorate event){
        MetropolisGenerationContainer handler = WorldGenerationHandler.getGenContainerFromWorld(event.world);
        ConcurrentHashMap<String, MetropolisBaseBB> mappedCities = handler.getUpdatedCityMap();
        Iterator iterator = mappedCities.entrySet().iterator();
        MetropolisBaseBB boundingBox;
        boolean flag = false;
        while (iterator.hasNext() && !flag){
            Map.Entry entry = (Map.Entry) iterator.next();
            boundingBox = (MetropolisBaseBB) entry.getValue();
            MetropolisBoundingBox clearTerrain = new MetropolisBoundingBox(boundingBox.minX, boundingBox.minZ, boundingBox.maxX, boundingBox.maxZ);
            if (clearTerrain.isVecInside(event.pos.getX(), event.pos.getY(),event.pos.getZ())){
                flag = true;
            }
        }
        if (flag) event.setResult(Event.Result.DENY);
    }*/

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onCreateDecoratorEvent(BiomeEvent.CreateDecorator event){
        event.newBiomeDecorator = new OverrideBiomeDecorator();
    }

    private boolean generatingInCityArea(World world, int posX, int posY, int posZ){
        MetropolisGenerationContainer handler = WorldGenerationHandler.getGenContainerFromWorld(world);
        ConcurrentHashMap<String, MetropolisBaseBB> mappedCities = handler.getUpdatedCityMap();
        Iterator iterator = mappedCities.entrySet().iterator();
        MetropolisBaseBB boundingBox;
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            boundingBox = (MetropolisBaseBB) entry.getValue();
            MetropolisBoundingBox clearTerrain = new MetropolisBoundingBox(boundingBox.minX, boundingBox.minZ, boundingBox.maxX, boundingBox.maxZ);
            if (clearTerrain.isVecInside(posX, posY, posZ)){
                return true;
            }
        }
        return false;
    }

    private boolean generatingInCityArea(World world, int posX, int posZ){
        MetropolisGenerationContainer handler = WorldGenerationHandler.getGenContainerFromWorld(world);
        ConcurrentHashMap<String, MetropolisBaseBB> mappedCities = handler.getUpdatedCityMap();
        Iterator iterator = mappedCities.entrySet().iterator();
        MetropolisBaseBB boundingBox;
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            boundingBox = (MetropolisBaseBB) entry.getValue();
            MetropolisBoundingBox clearTerrain = new MetropolisBoundingBox(boundingBox.minX, boundingBox.minZ, boundingBox.maxX, boundingBox.maxZ);
            if (clearTerrain.intersectsWith(posX - 16, posZ - 16, posX + 31, posZ + 31)){
                return true;
            }
        }
        return false;
    }
}
