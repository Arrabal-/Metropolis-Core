package mod.arrabal.metrocore.common.world.cities;

import mod.arrabal.metrocore.common.handlers.data.ChunkGenerationLogger;
import mod.arrabal.metrocore.common.handlers.data.CityBoundsSaveData;
import mod.arrabal.metrocore.common.world.gen.MapGenMetropolis;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

/**
 * Created by Arrabal on 9/15/14.
 */
public class MetropolisGenerationContainer {

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
    }

}
