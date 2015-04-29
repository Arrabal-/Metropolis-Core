package mod.arrabal.metrocore.common.handlers.world;

import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import mod.arrabal.metrocore.common.world.cities.*;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.*;
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
    public void onCreateDecoratorEvent(BiomeEvent.CreateDecorator event){
        event.newBiomeDecorator = new OverrideBiomeDecorator();
    }

    /*private boolean generatingInCityArea(World world, int posX, int posY, int posZ){
        MetropolisGenerationContainer handler = WorldGenerationHandler.getGenContainerFromWorld(world);
        ConcurrentHashMap<String, CityLayoutPlan> mappedCities = handler.getUpdatedCityMap();
        Iterator iterator = mappedCities.entrySet().iterator();
        CityLayoutPlan boundingBox;
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            boundingBox = (CityLayoutPlan) entry.getValue();
            MetropolisBoundingBox clearTerrain = new MetropolisBoundingBox(boundingBox.minX, boundingBox.minZ, boundingBox.maxX, boundingBox.maxZ);
            if (clearTerrain.isVecInside(posX, posY, posZ)){
                return true;
            }
        }
        return false;
    }

    private boolean generatingInCityArea(World world, int posX, int posZ){
        MetropolisGenerationContainer handler = WorldGenerationHandler.getGenContainerFromWorld(world);
        ConcurrentHashMap<String, CityLayoutPlan> mappedCities = handler.getUpdatedCityMap();
        Iterator iterator = mappedCities.entrySet().iterator();
        CityLayoutPlan boundingBox;
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            boundingBox = (CityLayoutPlan) entry.getValue();
            MetropolisBoundingBox clearTerrain = new MetropolisBoundingBox(boundingBox.minX, boundingBox.minZ, boundingBox.maxX, boundingBox.maxZ);
            if (clearTerrain.intersectsWith(posX - 16, posZ - 16, posX + 31, posZ + 31)){
                return true;
            }
        }
        return false;
    }*/
}
