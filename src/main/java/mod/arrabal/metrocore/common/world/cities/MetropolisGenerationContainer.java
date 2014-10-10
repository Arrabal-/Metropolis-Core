package mod.arrabal.metrocore.common.world.cities;

import mod.arrabal.metrocore.common.handlers.data.ChunkGenerationLogger;
import mod.arrabal.metrocore.common.handlers.data.CityBoundsSaveData;
import mod.arrabal.metrocore.common.handlers.data.MetropolisDataHandler;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by Arrabal on 9/15/14.
 */
public class MetropolisGenerationContainer {

    private MetropolisDataHandler dataHandler;
    private Metropolis generator;
    private ChunkGenerationLogger chunkLogger;
    private CityBoundsSaveData cityMap;

    public MetropolisGenerationContainer(World world){
        this.chunkLogger = (ChunkGenerationLogger) world.perWorldStorage.loadData(ChunkGenerationLogger.class, "metropolisChunkLogger");
        if (this.chunkLogger == null) {
            this.chunkLogger = new ChunkGenerationLogger("metropolisChunkLogger");
            world.perWorldStorage.setData("metropolisChunkLogger", this.chunkLogger);
        }
        this.cityMap = (CityBoundsSaveData) world.perWorldStorage.loadData(CityBoundsSaveData.class, "metropolisCityBounds");
        if (this.cityMap == null){
            this.cityMap = new CityBoundsSaveData("metropolisCityBounds");
            world.perWorldStorage.setData("metropolisCityBounds",this.cityMap);
        }
        this.generator = new Metropolis();
        this.dataHandler = new MetropolisDataHandler();
    }

    public boolean doGenerateSurfaceMetropolis(World world, Random random, int chunkX, int chunkZ){
        if (this.generator != null){
            if (this.dataHandler.isGenMapEmpty()){
                this.dataHandler.setGenMap(this.cityMap.getBoundingBoxMap());
            }
            Metropolis.generateMetropolis(random, chunkX, chunkZ, world, this);
            return true;
        }
        return false;
    }

    public boolean doCityGeneration(World world, int chunkX, int chunkZ){
        ChunkCoordIntPair genCoords = new ChunkCoordIntPair(chunkX, chunkZ);
        if (this.dataHandler.startMapContainsKey(genCoords.toString())){
            MetropolisStart start = this.dataHandler.getStartFromKey(genCoords.toString());
            if (!start.getCurrentlyBuilding()){
                return start.generate(world);
            }
        }
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
        if (validGenChunk) { return start.generate(world, chunkX, chunkZ); }
        return false;
    }

    public boolean doConflictCheck(MetropolisBaseBB boundingBox){
        return this.dataHandler.ConflictCheck(boundingBox);
    }

    public boolean catchChunkBug(int chunkX, int chunkZ){
        return this.chunkLogger.catchChunkBug(chunkX, chunkZ);
    }

    public void addToBlacklistMap(MetropolisBaseBB urbanArea) {
        this.dataHandler.addToBoundingBoxMap(urbanArea);
        //this.cityMap.saveBoundingBoxData(urbanArea);
    }

    public void addToStartMap(MetropolisStart start){
        this.dataHandler.addToStartMap(start);
    }
}
