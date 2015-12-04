package mod.arrabal.metrocore.common.world.cities;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.world.gen.MapGenStructureIO;
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
    private boolean currentlyBuilding;
    private Random random;
    private int maxComponents;
    public CityComponentPieces.Start cityLayoutStart;


    public MetropolisStart() {}

    public MetropolisStart(World world, int chunkX, int chunkZ, int avgY, int radiusX, int radiusZ, int maxCityComponents){
        this.startCoord = new ChunkCoordIntPair(chunkX, chunkZ);
        this.maxXGenRadius = radiusX;
        this.maxZGenRadius = radiusZ;
        this.baseY = avgY;
        this.random = getNewRandom(world, chunkX, chunkZ);
        this.baseType = UrbanClassification.URBAN;
        this.currentlyBuilding = false;
        this.maxComponents = maxCityComponents;
        UrbanType cityClass = getUrbanClass();
        List tileList = CityComponentPieces.getCityComponentWeightsLists(random, cityClass, Math.min(radiusX, radiusZ), false);
        List structureList = CityComponentPieces.getCityComponentWeightsLists(random, cityClass, Math.min(radiusX, radiusZ), true);
        this.cityLayoutStart = new CityComponentPieces.Start(0, getStartVariant(random, cityClass), random, chunkX, chunkZ, avgY, tileList, structureList);
        this.cityLayoutStart.maxSize = new CityLayoutPlan((chunkX << 4) - (radiusX << 4), (chunkZ << 4) - (radiusZ << 4),
                (chunkX << 4) + 15 + (radiusX << 4), (chunkZ << 4) + 15 + (radiusZ << 4), "MaxDimensions");
        this.cityLayoutStart.citySize = cityClass;
        this.cityLayoutStart.roadGrid = getRoadGridType(random);
        this.cityLayoutStart.setMaxCityTiles(this.maxComponents);
    }

    public enum UrbanClassification {ROAD, URBAN}
    public enum UrbanType {TOWN, CITY, METROPOLIS, ROAD, HIGHWAY}
    public enum RoadGrid {ROAD_INSTANCE, GRID, WEB, SPRAWL, MIXED}

    private UrbanType getUrbanClass() {
        if (this.maxComponents <= 25) return UrbanType.TOWN;
        if (this.maxComponents < 50 ) return UrbanType.CITY;
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
        LogHelper.debug("CALL TO MetropolisStart.constructCityTile TO ADD COMPONENT PARTS");
        String mapKey = "[" + chunkX + ", " + chunkZ + "]";
        if (this.cityLayoutStart.cityComponentMap.containsKey(mapKey)){
            this.currentlyBuilding = true;
            CityComponent cityComponent = cityLayoutStart.cityComponentMap.get(mapKey);
            cityComponent.addComponentParts(world, random);
            this.currentlyBuilding = false;
            return true;
        }
        return false;
    }

    public String getStartKey(){
        return this.startCoord.toString();
    }

    public boolean getCurrentlyBuilding() { return this.currentlyBuilding; }

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
        return this.startCoord.chunkXPos;
    }

    public int getStartZ(){
        return this.startCoord.chunkZPos;
    }

    public boolean generate(World world, Random random, ChunkCoordIntPair chunkCoords){
        LogHelper.debug("CALL TO MetropolisStart.generate TO CONSTRUCT CITY TILE");
        if (MapGenStructureIO.currentBuildCity.isEmpty()){
            LogHelper.debug("Passed an empty currentBuildCity hash map");
            return false;
        }
        boolean flag = false;
        CityComponent cityComponent = MapGenStructureIO.currentBuildCity.get(chunkCoords.toString());
        if (cityComponent == null) return flag;
        CityLayoutPlan cityPlan = (CityLayoutPlan) cityComponent.getBoundingBox();
        flag = this.constructCityTile(world, random, cityPlan.startX, cityPlan.startZ);
        if (flag) MapGenStructureIO.currentBuildCity.remove(chunkCoords.toString());

        /*Iterator iterator = cityLayoutStart.cityComponentMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            CityComponent cityComponent = (CityComponent) entry.getValue();
            CityLayoutPlan cityPlan = (CityLayoutPlan) cityComponent.getBoundingBox();
            flag = this.constructCityTile(world, random, cityPlan.startX, cityPlan.startZ);
        }*/
        //flag = constructCityTile(world, random, chunkX, chunkZ);
        //flag = placeCityStructures(world, random, chunkX, chunkZ);

        this.currentlyBuilding = false;
        return flag;
    }
}
