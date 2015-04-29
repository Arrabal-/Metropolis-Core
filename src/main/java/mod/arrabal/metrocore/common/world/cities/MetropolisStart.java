package mod.arrabal.metrocore.common.world.cities;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.world.structure.CityComponent;
import mod.arrabal.metrocore.common.world.structure.CityComponentPieces;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.util.*;

/**
 * Created by Arrabal on 6/24/2014.
 *
 * Class container for the metadata information on the city to be spawned
 */

public class MetropolisStart {

    private ChunkCoordIntPair startCoord;
    private int maxXGenRadius, maxZGenRadius;
    private int baseY;
    private UrbanClassification baseType;
    private List spawnList;
    private boolean currentlyBuilding;
    private Random random;
    public CityComponentPieces.Start cityLayoutStart;


    public MetropolisStart() {}

    public MetropolisStart(World world, int chunkX, int chunkZ, int avgY, int radiusX, int radiusZ, List spawns){
        this.startCoord = new ChunkCoordIntPair(chunkX, chunkZ);
        this.maxXGenRadius = radiusX;
        this.maxZGenRadius = radiusZ;
        this.baseY = avgY;
        this.spawnList = spawns;
        this.random = getNewRandom(world, chunkX, chunkZ);
        this.baseType = UrbanClassification.URBAN;
        currentlyBuilding = false;
        UrbanType cityClass = getUrbanClass(this.maxXGenRadius,this.maxZGenRadius);
        List tileList = CityComponentPieces.getCityComponentWeightsLists(random, cityClass, Math.min(radiusX, radiusZ), false);
        List structureList = CityComponentPieces.getCityComponentWeightsLists(random, cityClass, Math.min(radiusX, radiusZ), true);
        this.cityLayoutStart = new CityComponentPieces.Start(0, getStartVariant(random, cityClass), random, chunkX, chunkZ, avgY, tileList, structureList);
        cityLayoutStart.maxSize = new CityLayoutPlan((chunkX << 4) - (radiusX << 4), (chunkZ << 4) - (radiusZ << 4),
                (chunkX << 4) + 15 + (radiusX << 4), (chunkZ << 4) + 15 + (radiusZ << 4), "MaxDimensions");
        cityLayoutStart.citySize = cityClass;
        cityLayoutStart.roadGrid = getRoadGridType(random);
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

    private int getStartVariant(Random random, UrbanType cityClass){
        if (cityClass == UrbanType.METROPOLIS) {
            return random.nextInt(10) < 4 ? 1 : 0;
        }
        if (cityClass == UrbanType.CITY){
            return random.nextInt(10) < 2 ? 1 : 0;
        }
        return 0;
    }

    private RoadGrid getRoadGridType(Random rand){
        switch (rand.nextInt(12)){
            case 0:
                return RoadGrid.MIXED;
            case 4:
            case 5:
            case 6:
                return RoadGrid.WEB;
            case 9:
            case 10:
                return RoadGrid.SPRAWL;
        }
        return RoadGrid.GRID;
    }

    private Random getNewRandom(World world, int chunkX, int chunkZ){
        Random random = new Random(world.getSeed());
        long l = (random.nextLong() / 2L) * 2L + 1L;
        long l1 = (random.nextLong() / 2L) * 2L + 1L;
        random.setSeed(chunkX * l + chunkZ * l1 ^ world.getSeed());
        return random;
    }

    private boolean constructCityTile(World world, Random random, int chunkX, int chunkZ){
        String mapKey = "[" + chunkX + ", " + chunkZ + "]";
        if (cityLayoutStart.cityComponentMap.containsKey(mapKey)){
            currentlyBuilding = true;
            CityComponent cityComponent = cityLayoutStart.cityComponentMap.get(mapKey);
            cityComponent.addComponentParts(world, random);
            currentlyBuilding = false;
            return true;
        }
        return false;
    }

    public String getStartKey(){
        return startCoord.toString();
    }

    public boolean getCurrentlyBuilding() { return currentlyBuilding; }

    public boolean getMaxBBIntersection(int xMinPos, int zMinPos, int xMaxPos, int zMaxPos){
        return this.cityLayoutStart.maxSize.intersectsWith(xMinPos, zMinPos, xMaxPos, zMaxPos);
    }

    public int getMaxGenRadius(boolean getX){
        if (getX) return this.maxXGenRadius;
        return this.maxZGenRadius;
    }

    public int getBaseY() {
        return this.baseY;
    }

    public int getStartX() {
        return startCoord.chunkXPos;
    }

    public int getStartZ(){
        return startCoord.chunkZPos;
    }

    public boolean generate(World world, Random random){
        boolean flag = false;
        Iterator iterator = cityLayoutStart.cityComponentMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            CityComponent cityComponent = (CityComponent) entry.getValue();
            flag = constructCityTile(world, random, cityComponent.getBoundingBox().startX, cityComponent.getBoundingBox().startZ);
        }
        //flag = constructCityTile(world, random, chunkX, chunkZ);
        //flag = placeCityStructures(world, random, chunkX, chunkZ);
        currentlyBuilding = false;
        return flag;
    }
}
