package mod.arrabal.metrocore.common.world.cities;

import mod.arrabal.metrocore.common.handlers.data.ChunkGenerationLogger;
import mod.arrabal.metrocore.common.handlers.data.CityBoundsSaveData;
import mod.arrabal.metrocore.common.handlers.data.MetropolisDataHandler;
import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import mod.arrabal.metrocore.common.world.gen.MapGenMetropolis;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Arrabal on 9/15/14.
 */
public class MetropolisGenerationContainer {

    private MetropolisDataHandler dataHandler;
    private Metropolis generator;
    private ChunkGenerationLogger chunkLogger;
    private static CityBoundsSaveData cityMap;
    public MapGenMetropolis cityGenerator;

    public ChunkCoordIntPair currentStart;

    public MetropolisGenerationContainer(){

    }

    public MetropolisGenerationContainer(World world){
        this.chunkLogger = (ChunkGenerationLogger) world.getPerWorldStorage().loadData(ChunkGenerationLogger.class, "metropolisChunkLogger");
        if (this.chunkLogger == null) {
            this.chunkLogger = new ChunkGenerationLogger("metropolisChunkLogger");
            world.getPerWorldStorage().setData("metropolisChunkLogger", this.chunkLogger);
        }
        MetropolisGenerationContainer.cityMap = (CityBoundsSaveData) world.getPerWorldStorage().loadData(CityBoundsSaveData.class, "metropolisCityBounds");
        if (MetropolisGenerationContainer.cityMap == null){
            MetropolisGenerationContainer.cityMap = new CityBoundsSaveData("metropolisCityBounds");
            world.getPerWorldStorage().setData("metropolisCityBounds",MetropolisGenerationContainer.cityMap);
        }
        this.generator = new Metropolis();
        this.dataHandler = new MetropolisDataHandler();
    }

    public boolean doGenerateSurfaceMetropolis(World world, Random random, int chunkX, int chunkZ){
        if (this.generator != null){
            if (this.dataHandler.isGenMapEmpty()){
                this.dataHandler.setGenMap(MetropolisGenerationContainer.cityMap.getBoundingBoxMap());
            }
            return Metropolis.generateMetropolis(random, chunkX, chunkZ, world, this);
        }
        return false;
    }

    public boolean doBuildMetropolis(World world, Random random, int chunkX, int chunkZ){
        //TODO:  Need to revise this to use the structure start class (not written yet) to actually spawn the city
        ChunkCoordIntPair genCoords = new ChunkCoordIntPair(chunkX, chunkZ);
        if (this.dataHandler.startMapContainsKey(genCoords.toString())){
            MetropolisStart start = this.dataHandler.getStartFromKey(genCoords.toString());
            if (!start.getCurrentlyBuilding()){
                return start.generate(world, random);
            }
        }else {
            MetropolisStart start = null;
            boolean validGenChunk = false;
            Iterator iterator = this.dataHandler.startMap.entrySet().iterator();
            while (iterator.hasNext() && !validGenChunk){
                Map.Entry entry = (Map.Entry) iterator.next();
                start = (MetropolisStart) entry.getValue();
                if (start.getMaxBBIntersection(chunkX << 4, chunkZ << 4, (chunkX << 4) + 15, (chunkZ << 4) + 15)){
                    validGenChunk = true;
                }
            }
            if (validGenChunk && !start.getCurrentlyBuilding()) {
                return start.generate(world, random);
            }
        }

        return false;
    }

    public boolean doConflictCheck(MetropolisBoundingBox boundingBox){
        return this.dataHandler.ConflictCheck(boundingBox);
    }

    public boolean catchChunkBug(int chunkX, int chunkZ){
        return this.chunkLogger.catchChunkBug(chunkX, chunkZ);
    }

    public void addToGenerationMap(MetropolisBoundingBox urbanArea) {
        this.dataHandler.addToBoundingBoxMap(urbanArea);
        MetropolisGenerationContainer.cityMap.saveBoundingBoxData(urbanArea);
    }

    public void addToStartMap(MetropolisStart start){
        this.dataHandler.addToStartMap(start);
    }

    public boolean startMapContainsKey(Object key){
        return this.dataHandler.startMapContainsKey(key);
    }

    public MetropolisStart getStartFromKey(Object key){
        return this.dataHandler.getStartFromKey(key);
    }

    public void doGenerateMetropolisStart(World world, int chunkX, int chunkZ, int avgY, int xGenRadius, int zGenRadius) {
        this.generator.generateMetropolisStart(world, chunkX, chunkZ, avgY, xGenRadius, zGenRadius);
    }

    public ConcurrentHashMap<String, MetropolisBoundingBox> getUpdatedCityMap(){
        this.dataHandler.setGenMap(MetropolisGenerationContainer.cityMap.getBoundingBoxMap());
        return MetropolisGenerationContainer.cityMap.getBoundingBoxMap();
    }

}
