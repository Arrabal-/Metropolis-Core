package mod.arrabal.metrocore.common.world.cities;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.world.structure.CityComponent;
import mod.arrabal.metrocore.common.world.structure.CityComponentPieces;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 6/24/2014.
 *
 * Class container for the metadata information on the city to be spawned
 */

public class MetropolisStart {

    private MetropolisBaseBB originBB, maxBB, footPrint;
    private ChunkCoordIntPair startCoord;
    private int maxXGenRadius, maxZGenRadius;
    private int baseY;
    private Integer calledByX, calledByZ;
    private UrbanClassification baseType;
    private UrbanType urbanClass;
    private RoadGrid roadLayout;
    private boolean isRuins;
    private List spawnList;
    private boolean currentlyBuilding;
    private Random random;
    private HashMap<String, CityComponent> componentMap;


    public MetropolisStart() {}

    public MetropolisStart(World world, int chunkX, int chunkZ, int avgY, int radiusX, int radiusZ, List spawns){
        this.startCoord = new ChunkCoordIntPair(chunkX, chunkZ);
        this.componentMap = new HashMap<String, CityComponent>();
        this.maxXGenRadius = radiusX;
        this.maxZGenRadius = radiusZ;
        this.baseY = avgY;
        this.spawnList = spawns;
        this.calledByX = null;
        this.calledByZ = null;
        this.random = getNewRandom(world, chunkX, chunkZ);
        int facing;
        facing = random.nextInt(4);
        this.originBB = new MetropolisBaseBB(chunkX, chunkZ, facing, "OriginChunk");
        this.maxBB = new MetropolisBaseBB((chunkX << 4) - (radiusX << 4), (chunkZ << 4) - (radiusZ << 4),
                (chunkX << 4) + 15 + (radiusX << 4), (chunkZ << 4) + 15 + (radiusZ << 4), "MaxDimensions");
        this.footPrint = new MetropolisBaseBB(chunkX, chunkZ, facing, "CityFootprint");
        this.baseType = UrbanClassification.URBAN;
        this.urbanClass = getUrbanClass(this.maxXGenRadius,this.maxZGenRadius);
        switch (random.nextInt(12)){
            case 0:
                this.roadLayout = RoadGrid.MIXED;
                break;
            case 4:
            case 5:
            case 6:
                this.roadLayout = RoadGrid.WEB;
                break;
            case 9:
            case 10:
                this.roadLayout = RoadGrid.SPRAWL;
                break;
            default:
                this.roadLayout = RoadGrid.GRID;
        }
        this.isRuins = random.nextDouble() < ConfigHandler.ruinedCityPercent;
        currentlyBuilding = false;
    }

    public enum UrbanClassification {ROAD, URBAN}
    public enum UrbanType {TOWN, CITY, METROPOLIS, ROAD, HIGHWAY}
    public enum RoadGrid {ROAD_INSTANCE, GRID, WEB, SPRAWL, MIXED}

    private UrbanType getUrbanClass(int radiusX, int radiusZ){
        int genArea = radiusX * radiusZ;
        int sizeThreshold = (ConfigHandler.metropolisMaxGenRadius * ConfigHandler.metropolisMaxGenRadius) / 5;
        if (genArea < 2 * sizeThreshold) { return UrbanType.TOWN;}
        if (genArea < 4 * sizeThreshold) { return UrbanType.CITY;}
        return UrbanType.METROPOLIS;
    }

    private Random getNewRandom(World world, int chunkX, int chunkZ){
        Random random = new Random(world.getSeed());
        long l = (random.nextLong() / 2L) * 2L + 1L;
        long l1 = (random.nextLong() / 2L) * 2L + 1L;
        random.setSeed(chunkX * l + chunkZ * l1 ^ world.getSeed());
        return random;
    }

    public String getStartKey(){
        return startCoord.toString();
    }

    public boolean getCurrentlyBuilding() { return currentlyBuilding; }

    public boolean generate(World world) {

        int genChunkX, genChunkZ;
        genChunkX = this.startCoord.chunkXPos;
        genChunkZ = this.startCoord.chunkZPos;

        return false;
    }

    public boolean getMaxBBIntersection(int xMinPos, int zMinPos, int xMaxPos, int zMaxPos){
        return this.maxBB.intersectsWith(xMinPos, zMinPos, xMaxPos, zMaxPos);
    }
}
