package mod.arrabal.metrocore.common.world.structure;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.library.ModRef;
import mod.arrabal.metrocore.common.world.cities.MetropolisBaseBB;
import mod.arrabal.metrocore.common.world.cities.MetropolisStart;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.util.*;

/**
 * Created by Arrabal on 6/27/2014.
 *
 * Class container for the various classes that make up the different possible city tiles / structures
 * Each class will either extend the CityComponent class or a common base class that extends the
 * CityComponent class
 */
public class CityComponentPieces {

    public static void registerCityComponents(){

    }

    @SuppressWarnings("unchecked")
    public static List getCityComponentWeightsLists(Random random, MetropolisStart.UrbanType cityClass, boolean buildingList){

        ArrayList arrayList = new ArrayList();
        //TODO:  Add ability to handle max gen radius > 5
        int maxChunks = (ConfigHandler.metropolisMaxGenRadius * 2 + 1) * (ConfigHandler.metropolisMaxGenRadius * 2 + 1);
        if (!buildingList) {
            //Additions to the list need to be in the desired order of evaluation
            //Tiles more likely to spawn in the center of the city should be first
            if (cityClass == MetropolisStart.UrbanType.METROPOLIS) {
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.MunicipalDistrict.class, 10, MathHelper.getRandomIntegerInRange(random, 1, 2)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Monument.class, 12, MathHelper.getRandomIntegerInRange(random, 1, 2)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.CityPlaza.class, 12, MathHelper.getRandomIntegerInRange(random, 1, 2)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Avenue.class, 20, MathHelper.getRandomIntegerInRange(random, 8, 20)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.CityPark.class, 15, MathHelper.getRandomIntegerInRange(random, 2, 6)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.CitySquare.class, 15, MathHelper.getRandomIntegerInRange(random, 1, 5)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.NeighborhoodDistrict.class, 25, MathHelper.getRandomIntegerInRange(random, 3, 24)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.NeighborhoodAlley.class, 20, MathHelper.getRandomIntegerInRange(random, 1, 12)));
                //TODO:  check for new tiles added by other mods and add them to the list
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Alley.class, 25, MathHelper.getRandomIntegerInRange(random, 16, 36)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Street.class, 50, MathHelper.getRandomIntegerInRange(random, 32, 72)));
            } else if (cityClass == MetropolisStart.UrbanType.CITY) {
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.CityPark.class, 10, MathHelper.getRandomIntegerInRange(random, 1, 2)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Avenue.class, 20, MathHelper.getRandomIntegerInRange(random, 8, 16)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.CitySquare.class, 15, MathHelper.getRandomIntegerInRange(random, 1, 5)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.NeighborhoodAlley.class, 15, MathHelper.getRandomIntegerInRange(random, 1, 10)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.NeighborhoodDistrict.class, 20, MathHelper.getRandomIntegerInRange(random, 3, 20)));
                //TODO:  check for new tiles added by other mods and add them to the list
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Alley.class, 16, MathHelper.getRandomIntegerInRange(random, 8, 22)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Street.class, 35, MathHelper.getRandomIntegerInRange(random, 20, 46)));
            } else {
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Avenue.class, 6, MathHelper.getRandomIntegerInRange(random, 8, 12)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.CitySquare.class, 6, MathHelper.getRandomIntegerInRange(random, 1, 3)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.NeighborhoodDistrict.class, 15, MathHelper.getRandomIntegerInRange(random, 1, 7)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.NeighborhoodAlley.class, 7, MathHelper.getRandomIntegerInRange(random, 1, 3)));
                //TODO:  check for new tiles added by other mods and add them to the list
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Alley.class, 7, MathHelper.getRandomIntegerInRange(random, 4, 13)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Street.class, 15, MathHelper.getRandomIntegerInRange(random, 20, 27)));
            }
            return arrayList;
        }
        // check for new tiles added by other mods and add them to the list
        return null;
    }

    public static class CityWeight{

        public Class cityComponentClass;
        public final int cityComponentWeight;
        public int cityComponentSpawned;
        public int cityComponentMax;

        public CityWeight(Class cityClass, int weight, int maxComponents){
            this.cityComponentClass = cityClass;
            this.cityComponentWeight = weight;
            this.cityComponentMax = maxComponents;
        }

        public boolean canSpawnMoreComponents(){

            return this.cityComponentMax == 0 || this.cityComponentSpawned < this.cityComponentMax;
        }

        public boolean hasNoSpawnLimit(){
            if (cityComponentClass == CityComponentPieces.Street.class || cityComponentClass == CityComponentPieces.Alley.class ||
                   cityComponentClass == CityComponentPieces.NeighborhoodDistrict.class || cityComponentClass == CityComponentPieces.NeighborhoodAlley.class) return true;
            return false;
        }
    }

    private static int getComponentMaxWeightedValue(List componentList){
        boolean flag = false;
        int i = 0;
        int max = -1;
        CityWeight cityWeight;
        for (Iterator iterator = componentList.iterator(); iterator.hasNext(); i+= cityWeight.cityComponentWeight){
            cityWeight = (CityComponentPieces.CityWeight)iterator.next();
            if (cityWeight.cityComponentMax > 0 && cityWeight.cityComponentSpawned < cityWeight.cityComponentMax){
                flag = true;
                max = cityWeight.cityComponentWeight > max ? cityWeight.cityComponentWeight : max;
            }
        }
        if (flag) return max;
        if (componentList.size() <= 4){
            i = 0;
            for (Iterator iterator = componentList.iterator(); iterator.hasNext(); i+=cityWeight.cityComponentWeight){
                cityWeight = (CityComponentPieces.CityWeight)iterator.next();
                if (cityWeight.cityComponentMax == 0 && cityWeight.hasNoSpawnLimit()){
                    max = cityWeight.cityComponentWeight * 2 > max ? cityWeight.cityComponentWeight * 2 : max;
                }
            }
        }
        return max;
    }

    private static CityComponent getCityComponent(CityComponentPieces.Start start, CityComponentPieces.CityWeight cityWeight, Random random, int minX, int minY, int minZ,
            int maxX, int maxY, int maxZ) {
        Class componentClass = cityWeight.cityComponentClass;
        Object object = null;

        if (componentClass == CityComponentPieces.CitySquare.class){
            object = CityComponentPieces.CitySquare.getNewCitySquare(ModRef.CITY_SQUARE_ID, -2, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.Street.class){
            object = CityComponentPieces.Street.getNewCityStreet(ModRef.STREET_ID, -2, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.NeighborhoodAlley.class){
            object = CityComponentPieces.NeighborhoodAlley.getNewNeighborhoodAlley(ModRef.N_ALLEY_ID, -2, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.Monument.class){
            object = CityComponentPieces.Monument.getNewCityMonument(ModRef.MONUMENT_ID, -2, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.Alley.class){
            object = CityComponentPieces.Alley.getNewCityAlley(ModRef.ALLEY_ID, -2, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.CityPark.class){
            object = CityComponentPieces.CityPark.getNewCityPark(ModRef.CITY_PARK_ID, -2, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.MunicipalDistrict.class){
            object = CityComponentPieces.MunicipalDistrict.getNewMunicipalDistrict(ModRef.MUNICIPAL_DIST_ID, -2, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.Avenue.class){
            object = CityComponentPieces.Avenue.getNewCityAvenue(ModRef.AVENUE_ID, -2, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.CityPlaza.class){
            object = CityComponentPieces.CityPlaza.getNewCityPlaza(ModRef.CITY_PLAZA_ID, -2, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.NeighborhoodDistrict.class){
            object = CityComponentPieces.NeighborhoodDistrict.getNewNeighborhood(ModRef.NEIGHBORHOOD_ID, -2, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.Tower.class){
            object = CityComponentPieces.Tower.getNewTower(ModRef.TOWER_ID, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.Industrial.class){
            object = CityComponentPieces.Industrial.getNewIndustrial(ModRef.INDUSTRIAL_ID, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.Institution.class){
            object = CityComponentPieces.Institution.getNewInstitution(ModRef.INSTITUTION_ID, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.Residential.class){
            object = CityComponentPieces.Residential.getNewResidential(ModRef.RESIDENTIAL_ID, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.nFlat.class){
            object = CityComponentPieces.nFlat.getNewFlat(ModRef.FLAT_ID, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.Prison.class){
            object = CityComponentPieces.Prison.getNewPrison(ModRef.PRISON_ID, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else if (componentClass == CityComponentPieces.Municipal.class){
            object = CityComponentPieces.Municipal.getNewMunicipal(ModRef.MUNICIPAL_BUILDING_ID, start, random, minX, minY, minZ, maxX, maxY, maxZ);
        }
        else {
            //TODO:  Get components for mod added classes
        }
        return (CityComponent)object;
    }
    //TODO: This is returning null for new components
    private static CityComponent getNextValidComponent(CityComponentPieces.Start start, Random random, int chunkX, int chunkZ, int minY, int maxY, boolean buildings){
        int maxWeights = buildings ? getComponentMaxWeightedValue(start.weightedBuildingList) : getComponentMaxWeightedValue(start.weightedCityComponentList);
        int threshold = random.nextInt(maxWeights); //Threshold initial value is too high relative to weights.
        int listSize;
        int listThreshold = buildings ? 1 : 4;
        int i = 0;

        Iterator iterator = buildings ? start.weightedBuildingList.iterator() : start.weightedCityComponentList.iterator();

        while (iterator.hasNext()){
            listSize = buildings ? start.weightedBuildingList.size() : start.weightedCityComponentList.size();
            CityComponentPieces.CityWeight cityWeight = (CityComponentPieces.CityWeight)iterator.next();
            if (threshold < cityWeight.cityComponentWeight + i){
                if (!cityWeight.canSpawnMoreComponents() || (cityWeight == start.cityWeight && listSize > listThreshold &&
                        !cityWeight.hasNoSpawnLimit())){
                    i+=cityWeight.cityComponentWeight;
                    continue;
                }
                CityComponent cityComponent = getCityComponent(start, cityWeight, random, (chunkX << 4), minY, (chunkZ << 4),
                        (chunkX << 4) + 15, maxY, (chunkZ << 4) + 15);

                if (cityComponent != null){
                    ++cityWeight.cityComponentSpawned;
                    start.cityWeight = cityWeight;

                    if (!cityWeight.canSpawnMoreComponents()){
                        if (buildings){
                            start.weightedBuildingList.remove(cityWeight);
                        }
                        else {
                            if (cityWeight.hasNoSpawnLimit()){
                                cityWeight.cityComponentMax = 0;
                            }
                            else start.weightedCityComponentList.remove(cityWeight);
                        }
                    }
                    return cityComponent;
                }
                else LogHelper.debug("getNextValid Component failed to find next tile at [" + chunkX + ", " + chunkZ + "]");
            }
            i+=cityWeight.cityComponentWeight;
        }
        //instead of returning null when no match is found either calculate a new threshold value (with a max number of iterations)
        //or default to a street tile
        return null;
    }

    public abstract static class Metropolis extends CityComponent {

        protected int maxBuildingLevels;
        protected int minBuildingLevels;
        protected boolean isRuins;
        protected CityComponentPieces.Start startPiece;
        protected BuildMap joinedTileMap;


        public Metropolis() {
        }

        protected Metropolis(int tileTypeID, CityComponentPieces.Start start) {
            super(tileTypeID);
            if (start != null) {
                this.isRuins = start.isRuins;
                this.startPiece = start;
            }
            this.minBuildingLevels = 3;
            this.joinedTileMap = new BuildMap();
        }

        protected CityComponent getNextCityComponent() {
            LogHelper.debug("Call to empty getNextCityComponent() method in Metropolis class");
            return null;
        }


        protected CityComponent getNextCityComponentXZ(CityComponentPieces.Start start, Random random, int xShift, int zShift, int minY, int maxY, boolean buildings) {

            int chunkX, chunkZ;
            Object defaultTile = buildings ? CityComponentPieces.Tower.class : CityComponentPieces.Street.class;
            chunkX = this.getChunkPosition().getX() >> 4;
            chunkZ = this.getChunkPosition().getZ() >> 4;
            CityComponent cityComponent = getNextValidComponent(start, random, chunkX + xShift, chunkZ + zShift, minY, maxY, buildings);
            if (cityComponent != null) return cityComponent;
            LogHelper.debug("Failed to get next city component (XZ).  Fall back to default tile.");
            return getNextCityComponentP(start, defaultTile, random, chunkX + xShift, chunkZ + zShift, minY, maxY, buildings);
        }

        protected CityComponent getNextCityComponentP(CityComponentPieces.Start start, Object preciseClass, Random random, int chunkX, int chunkZ, int minY, int maxY, boolean buildings) {

            Iterator iterator = buildings ? start.weightedBuildingList.iterator() : start.weightedCityComponentList.iterator();
            Object defaultClass = buildings ? CityComponentPieces.Tower.class : CityComponentPieces.Street.class;

            while (iterator.hasNext()) {
                CityComponentPieces.CityWeight cityWeight = (CityComponentPieces.CityWeight) iterator.next();
                if (!cityWeight.canSpawnMoreComponents() || cityWeight.cityComponentClass != preciseClass) {
                    continue;
                }
                CityComponent cityComponent = getCityComponent(start, cityWeight, random, (chunkX << 4), minY, (chunkZ << 4),
                        (chunkX << 4) + 15, maxY, (chunkZ << 4) + 15);

                if (cityComponent != null) {
                    ++cityWeight.cityComponentSpawned;
                    start.cityWeight = cityWeight;

                    if (!cityWeight.canSpawnMoreComponents()) {
                        if (buildings) {
                            start.weightedBuildingList.remove(cityWeight);
                        } else {
                            if (cityWeight.cityComponentClass == CityComponentPieces.Street.class) {
                                cityWeight.cityComponentMax = 0;
                            } else start.weightedCityComponentList.remove(cityWeight);
                        }
                    }
                    return cityComponent;
                }
            }
            LogHelper.debug("Failed to get next city component (precise).  Fall back to default tile.");
            return getNextCityComponentP(start, defaultClass, random, chunkX, chunkZ, minY, maxY, buildings);
        }

        @Override
        public void buildComponent(CityComponent cityTile, Random random){
            int chunkX, chunkZ;
            chunkX = this.getChunkPosition().getX() >> 4;
            chunkZ = this.getChunkPosition().getZ() >> 4;

            this.joinedTileMap.getIntersections(chunkX, chunkZ);

            CityComponent cityComponent1, cityComponent2, cityComponent3, cityComponent4;
            String newChunkKey;

            newChunkKey = "[" + chunkX + ", " + (chunkZ + 1) + "]";
            if (!this.startPiece.cityComponentMap.containsKey(newChunkKey)){
                cityComponent1 = getNextCityComponentXZ(this.startPiece, random, 0, 1, 1, this.startPiece.baseY, false);
                if (cityComponent1 != null){
                    this.startPiece.cityComponentMap.put(newChunkKey, (CityComponentPieces.Metropolis) cityComponent1);
                    this.joinedTileMap.intersections.set(0,(CityComponentPieces.Metropolis) cityComponent1);
                }
            }

            newChunkKey = "[" + (chunkX - 1) + ", " + (chunkZ) + "]";
            if (!this.startPiece.cityComponentMap.containsKey(newChunkKey)){
                cityComponent2 = getNextCityComponentXZ(this.startPiece, random, -1, 0, 1, this.startPiece.baseY, false);
                if (cityComponent2 != null){
                    this.startPiece.cityComponentMap.put(newChunkKey, (CityComponentPieces.Metropolis) cityComponent2);
                    this.joinedTileMap.intersections.set(1,(CityComponentPieces.Metropolis) cityComponent2);
                }
            }

            newChunkKey = "[" + (chunkX) + ", " + (chunkZ - 1) + "]";
            if (!this.startPiece.cityComponentMap.containsKey(newChunkKey)){
                cityComponent3 = getNextCityComponentXZ(this.startPiece, random, 0, -1, 1, this.startPiece.baseY, false);
                if (cityComponent3 != null){
                    this.startPiece.cityComponentMap.put(newChunkKey, (CityComponentPieces.Metropolis) cityComponent3);
                    this.joinedTileMap.intersections.set(2,(CityComponentPieces.Metropolis) cityComponent3);
                }
            }

            newChunkKey = "[" + (chunkX + 1) + ", " + (chunkZ) + "]";
            if (!this.startPiece.cityComponentMap.containsKey(newChunkKey)){
                cityComponent4 = getNextCityComponentXZ(this.startPiece, random, 1, 0, 1, this.startPiece.baseY, false);
                if (cityComponent4 != null){
                    this.startPiece.cityComponentMap.put(newChunkKey, (CityComponentPieces.Metropolis) cityComponent4);
                    this.joinedTileMap.intersections.set(3,(CityComponentPieces.Metropolis) cityComponent4);
                }
            }

        }

        @Override
        public void buildComponent(CityComponent cityTile, Random random, int xShift, int zShift) {
            int buildX = (this.getChunkPosition().getX() >> 4) + xShift;
            int buildZ = (this.getChunkPosition().getZ() >> 4) + zShift;
            String newChunkKey = "[" + buildX + ", " + buildZ + "]";
            CityComponent cityComponent = getNextCityComponentXZ((CityComponentPieces.Start) cityTile, random, xShift, zShift, 1, startPiece.baseY, false);
            if (cityComponent != null){
                this.startPiece.cityComponentMap.put(newChunkKey, (CityComponentPieces.Metropolis) cityComponent);
            }
            else LogHelper.debug("Failed to build new component at " + newChunkKey);
        }

        @Override
        public boolean addComponentParts(World world, Random random){

            return false;
        }

        protected class BuildMap {

            public ArrayList<CityComponentPieces.Metropolis> intersections;
            public boolean requiredTile;
            public int requiredTileLocation;
            public byte rightOfWay; // 1 = East/West 2 = North/South 3 = All

            public static final byte ROW_EW = 1;
            public static final byte ROW_NS = 2;
            public static final byte ROW_ALL = 3;
            public static final byte ROW_NONE = 0;
            public static final int TILE_LOC_SOUTH = 1;
            public static final int TILE_LOC_WEST = 2;
            public static final int TILE_LOC_NORTH = 3;
            public static final int TILE_LOC_EAST = 4;

            protected BuildMap(){
                intersections = new ArrayList();
                requiredTile = false;
                requiredTileLocation = 0;
                rightOfWay = ROW_NONE;
            }

            public void getIntersections(int chunkX, int chunkZ){
                ArrayList<CityComponentPieces.Metropolis> adjacentTiles = new ArrayList();

                String newChunk = "[" + (chunkX) + ", " + (chunkZ + 1) + "]";
                if (startPiece.cityComponentMap.containsKey(newChunk)){
                    adjacentTiles.add(0, startPiece.cityComponentMap.get(newChunk)); // South
                } else adjacentTiles.add(0, null);

                newChunk = "[" + (chunkX - 1) + ", " + (chunkZ) + "]";
                if (startPiece.cityComponentMap.containsKey(newChunk)){
                    adjacentTiles.add(1 ,startPiece.cityComponentMap.get(newChunk)); // West
                } else adjacentTiles.add(1, null);

                newChunk = "[" + (chunkX) + ", " + (chunkZ - 1) + "]";
                if (startPiece.cityComponentMap.containsKey(newChunk)){
                    adjacentTiles.add(2, startPiece.cityComponentMap.get(newChunk)); // North
                } else adjacentTiles.add(2, null);

                newChunk = "[" + (chunkX + 1) + ", " + (chunkZ) + "]";
                if (startPiece.cityComponentMap.containsKey(newChunk)){
                    adjacentTiles.add(3, startPiece.cityComponentMap.get(newChunk)); // East
                } else adjacentTiles.add(3, null);

                if (adjacentTiles.size() > 0) intersections = adjacentTiles;

            }

            public void getHasRequiredTile(Class matchClass){

                for (int i = 0; i < intersections.size(); ++i){
                    if (intersections.get(i) != null){
                        if (matchClass.isInstance(intersections.get(i))){
                            requiredTile = true;
                            requiredTileLocation = requiredTileLocation | i;
                        }
                    }
                }
            }

            public void getRightOfWay(Class matchClass){
                byte row = 0;

                for (int i = 0; i < intersections.size(); ++i){
                    if (intersections.get(i) != null){
                        if (matchClass.isInstance(intersections.get(i))){
                            if (row > 0){
                                if (row == 1 && (i == 1 || i == 3)){
                                    rightOfWay = ROW_ALL;
                                    return;
                                }
                                if (row == 2 && (i == 0 || i == 2)){
                                    rightOfWay = ROW_ALL;
                                    return;
                                }
                            } else {
                                switch (i) {
                                    case 1:
                                        row = ROW_EW;
                                        break;
                                    case 2:
                                        row = ROW_NS;
                                        break;
                                    default:
                                        row = (byte) (i - 2);
                                }
                            }
                        }
                    }
                }
                rightOfWay = row;
            }
        }

    }

    public static class CitySquare extends CityComponentPieces.Metropolis {

        public CitySquare() {}

        protected CitySquare(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random, MetropolisCityPlan boundingBox) {
            super(tileTypeID, start);
            if (tileVariant < -1) {
                this.typeVariant = (random.nextInt(10) < 2) ? 1 : 0;
            }
            else if (tileVariant > -1) this.typeVariant = tileVariant;
            this.coordBaseMode = start.coordBaseMode;
            this.boundingBox = boundingBox;
        }

        protected CitySquare(int tileTypeID, int tileVariant, int chunkX, int chunkZ, int baseY, CityComponentPieces.Start start, Random random){
            super(tileTypeID, start);
            this.coordBaseMode = random.nextInt(4);
            this.boundingBox = new MetropolisCityPlan(chunkX << 4, 1, chunkZ << 4, (chunkX << 4) + 15, baseY, (chunkZ << 4) + 15,
                    this.coordBaseMode, "OriginSquare");
            if (tileVariant < 0) {
                this.typeVariant = (random.nextInt(10) < 2) ? 1 : 0;
            }
            else if (tileVariant > -1) this.typeVariant = tileVariant;
        }

        public static CitySquare getNewCitySquare(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random,
                                                  int minX, int minY, int minZ, int maxX, int maxY, int maxZ){

            MetropolisCityPlan dimensions = new MetropolisCityPlan(minX, minY, minZ, maxX, maxY, maxZ, start.coordBaseMode, "CitySquare");
            return new CitySquare(tileTypeID, tileVariant, start, random, dimensions);
        }

        @Override
        public void buildComponent(CityComponent start, Random random){
            int chunkX, chunkZ;
            CityComponent cityComponent1;

            chunkX = this.getChunkPosition().getX() >> 4;
            chunkZ = this.getChunkPosition().getZ() >> 4;

            this.joinedTileMap.getIntersections(chunkX, chunkZ);
            this.joinedTileMap.getHasRequiredTile(CityComponentPieces.Avenue.class);

            List buildList = new ArrayList();
            for (int i = 0; i < this.joinedTileMap.intersections.size(); ++i){
                CityComponent component = this.joinedTileMap.intersections.get(i);
                if (component == null){
                    buildList.add(i);
                }
            }
            while (!buildList.isEmpty()){
                int working = random.nextInt(buildList.size());
                int buildX = 0;
                int buildZ = 0;
                int newTile = (int) buildList.get(working);
                switch(newTile){
                    case 0:
                        buildZ = 1;
                        break;
                    case 1:
                        buildX = -1;
                        break;
                    case 2:
                        buildZ = -1;
                        break;
                    case 3:
                        buildX = 1;
                        break;
                }
                if (!this.joinedTileMap.requiredTile){
                    cityComponent1 = getNextCityComponentP((CityComponentPieces.Start)start, CityComponentPieces.Avenue.class, random, chunkX + buildX, chunkZ + buildZ, 1, startPiece.baseY, false);
                    if (cityComponent1 != null) this.joinedTileMap.requiredTile = true;
                } else {
                    cityComponent1 = getNextCityComponentXZ((CityComponentPieces.Start)start, random, buildX, buildZ, 1, startPiece.baseY, false);
                }
                if (cityComponent1 != null){
                    buildList.remove(working);
                    String mapKey = "[" + (chunkX + buildX) + ", " + (chunkZ + buildZ) + "]";
                    this.joinedTileMap.intersections.set(newTile, (CityComponentPieces.Metropolis) cityComponent1);
                    this.startPiece.cityComponentMap.put(mapKey, (CityComponentPieces.Metropolis) cityComponent1);
                } else LogHelper.debug("Failed to build new tile adjacent to City Square");
            }

        }


        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Start extends CityComponentPieces.CitySquare {

        public CityComponentPieces.CityWeight cityWeight;
        public HashMap<String, CityComponentPieces.Metropolis> cityComponentMap;
        public HashMap<String, CityComponentPieces.Building> buildingMap;
        public List weightedCityComponentList;
        public List weightedBuildingList;

        public MetropolisStart.UrbanType citySize;
        public MetropolisStart.RoadGrid roadGrid;
        public MetropolisBaseBB maxSize;
        public MetropolisCityPlan cityPlan;

        public int baseY;

        @SuppressWarnings("unused")
        public Start() {}

        public Start(int tileTypeID, int tileVariant, Random random, int chunkX, int chunkZ, int baseY, List cityList, List buildingList) {
            super(0, 0, chunkX, chunkZ, baseY, null, random);
            this.weightedCityComponentList = new ArrayList();
            this.weightedBuildingList = new ArrayList();
            this.isRuins = random.nextDouble() < ConfigHandler.ruinedCityPercent;
            this.weightedCityComponentList = cityList;
            this.weightedBuildingList = buildingList;
            this.baseY = baseY;
            this.cityPlan = new MetropolisCityPlan(chunkX << 4, 1, chunkZ << 4, (chunkX << 4) + 15, baseY, (chunkZ << 4) + 15,
                    random.nextInt(4), "UrbanLayout");
            this.cityComponentMap = new HashMap<String, Metropolis>();
            this.buildingMap = new HashMap<String, Building>();
        }

        @Override
        public void buildComponent(CityComponent start, Random random){
            int chunkX, chunkZ;
            CityComponent cityComponent1, cityComponent2, cityComponent3, cityComponent4;

            chunkX = this.getChunkPosition().getX() >> 4;
            chunkZ = this.getChunkPosition().getZ() >> 4;

            switch(roadGrid) {
                case GRID:
                case SPRAWL:
                case MIXED:
                case WEB: {
                    // add 4 avenues leading away from the start area
                    cityComponent1 = getNextCityComponentP((CityComponentPieces.Start)start, CityComponentPieces.Avenue.class, random, chunkX + 1, chunkZ, 1, this.baseY, false);
                    cityComponent2 = getNextCityComponentP((CityComponentPieces.Start)start, CityComponentPieces.Avenue.class, random, chunkX - 1, chunkZ, 1, this.baseY, false);
                    cityComponent3 = getNextCityComponentP((CityComponentPieces.Start)start, CityComponentPieces.Avenue.class, random, chunkX, chunkZ + 1, 1, this.baseY, false);
                    cityComponent4 = getNextCityComponentP((CityComponentPieces.Start)start, CityComponentPieces.Avenue.class, random, chunkX, chunkZ - 1, 1, this.baseY, false);
                    cityComponentMap.put("[" + (chunkX + 1) + ", " + chunkZ + "]", (CityComponentPieces.Avenue) cityComponent1);
                    cityComponentMap.put("[" + (chunkX - 1) + ", " + chunkZ + "]", (CityComponentPieces.Avenue) cityComponent2);
                    cityComponentMap.put("[" + chunkX + ", " + (chunkZ + 1) + "]", (CityComponentPieces.Avenue) cityComponent3);
                    cityComponentMap.put("[" + chunkX + ", " + (chunkZ - 1) + "]", (CityComponentPieces.Avenue) cityComponent4);

                    // extend the avenues a second chunk
                    cityComponent1 = getNextCityComponentP((CityComponentPieces.Start)start,CityComponentPieces.Avenue.class, random, chunkX + 2, chunkZ, 1, this.baseY, false);
                    cityComponent2 = getNextCityComponentP((CityComponentPieces.Start)start,CityComponentPieces.Avenue.class, random, chunkX - 2, chunkZ, 1, this.baseY, false);
                    cityComponent3 = getNextCityComponentP((CityComponentPieces.Start)start,CityComponentPieces.Avenue.class, random, chunkX, chunkZ + 2, 1, this.baseY, false);
                    cityComponent4 = getNextCityComponentP((CityComponentPieces.Start)start,CityComponentPieces.Avenue.class, random, chunkX, chunkZ - 2, 1, this.baseY, false);
                    cityComponentMap.put("[" + (chunkX + 2) + ", " + chunkZ + "]", (CityComponentPieces.Avenue) cityComponent1);
                    cityComponentMap.put("[" + (chunkX - 2) + ", " + chunkZ + "]", (CityComponentPieces.Avenue) cityComponent2);
                    cityComponentMap.put("[" + chunkX + ", " + (chunkZ + 2) + "]", (CityComponentPieces.Avenue) cityComponent3);
                    cityComponentMap.put("[" + chunkX + ", " + (chunkZ - 2) + "]", (CityComponentPieces.Avenue) cityComponent4);

                    // fill in the corners with alleys
                    cityComponent1 = getNextCityComponentP((CityComponentPieces.Start)start,CityComponentPieces.Alley.class, random, chunkX + 1, chunkZ + 1, 1, this.baseY, false);
                    cityComponent2 = getNextCityComponentP((CityComponentPieces.Start)start,CityComponentPieces.Alley.class, random, chunkX - 1, chunkZ - 1, 1, this.baseY, false);
                    cityComponent3 = getNextCityComponentP((CityComponentPieces.Start)start,CityComponentPieces.Alley.class, random, chunkX - 1, chunkZ + 1, 1, this.baseY, false);
                    cityComponent4 = getNextCityComponentP((CityComponentPieces.Start)start,CityComponentPieces.Alley.class, random, chunkX + 1, chunkZ - 1, 1, this.baseY, false);
                    cityComponentMap.put("[" + (chunkX + 1) + ", " + (chunkZ + 1) + "]", (CityComponentPieces.Alley) cityComponent1);
                    cityComponentMap.put("[" + (chunkX - 1) + ", " + (chunkZ - 1) + "]", (CityComponentPieces.Alley) cityComponent2);
                    cityComponentMap.put("[" + (chunkX - 1) + ", " + (chunkZ + 1) + "]", (CityComponentPieces.Alley) cityComponent3);
                    cityComponentMap.put("[" + (chunkX + 1) + ", " + (chunkZ - 1) + "]", (CityComponentPieces.Alley) cityComponent4);
                    break;
                }
            }
        }

        @Override
        public void buildComponent(CityComponent start, Random random, int xShift, int zShift){
            int buildX = (this.getChunkPosition().getX() >> 4) + xShift;
            int buildZ = (this.getChunkPosition().getZ() >> 4) + zShift;
            String newChunkKey = "[" + buildX + ", " + buildZ + "]";
            CityComponent cityComponent = getNextCityComponentXZ((CityComponentPieces.Start) start, random, xShift, zShift, 1, baseY, false);
            if (cityComponent != null){
                cityComponentMap.put(newChunkKey, (CityComponentPieces.Metropolis) cityComponent);
            }
            else LogHelper.debug("Failed to build new component at " + newChunkKey);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }

    }

    public static class CityPark extends CityComponentPieces.Metropolis {

        private boolean joinedPark;
        @SuppressWarnings("unused")
        public CityPark() {}

        protected CityPark(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random, MetropolisCityPlan boundingBox){
            super(tileTypeID, start);
            this.joinedPark = false;
            this.typeVariant = tileVariant;
            this.boundingBox = boundingBox;
            this.maxBuildingLevels = 11;
        }

        public static CityPark getNewCityPark(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random,
                                                  int minX, int minY, int minZ, int maxX, int maxY, int maxZ){

            MetropolisCityPlan dimensions = new MetropolisCityPlan(minX, minY, minZ, maxX, maxY, maxZ, start.coordBaseMode, "CityPark");
            return new CityPark(tileTypeID, tileVariant, start, random, dimensions);
        }

        @Override
        public void buildComponent(CityComponent start, Random random){
            int chunkX, chunkZ;
            CityComponent cityComponent1;

            chunkX = this.getChunkPosition().getX() >> 4;
            chunkZ = this.getChunkPosition().getZ() >> 4;

            this.joinedTileMap.getIntersections(chunkX, chunkZ);
            this.joinedTileMap.getHasRequiredTile(CityComponentPieces.CityPark.class);
            if (this.joinedTileMap.requiredTile) joinedPark = true;

            List buildList = new ArrayList();
            for (int i = 0; i < this.joinedTileMap.intersections.size(); ++i){
                CityComponent component = this.joinedTileMap.intersections.get(i);
                if (component == null){
                    buildList.add(i);
                }
            }
            while (!buildList.isEmpty()){
                int working = random.nextInt(buildList.size());
                int buildX = 0;
                int buildZ = 0;
                int newTile = (int) buildList.get(working);
                switch(newTile){
                    case 0:
                        buildZ = 1;
                        break;
                    case 1:
                        buildX = -1;
                        break;
                    case 2:
                        buildZ = -1;
                        break;
                    case 3:
                        buildX = 1;
                        break;
                }
                if (!joinedPark){
                    cityComponent1 = getNextCityComponentP((CityComponentPieces.Start)start, CityComponentPieces.CityPark.class, random, chunkX + buildX, chunkZ + buildZ, 1, startPiece.baseY, false);
                    this.joinedTileMap.requiredTile = false;
                    this.joinedTileMap.requiredTileLocation = 0;
                    this.joinedTileMap.getHasRequiredTile(CityComponentPieces.Avenue.class);
                    this.joinedTileMap.getHasRequiredTile(CityComponentPieces.Street.class);
                } else if (!this.joinedTileMap.requiredTile){
                    float check = random.nextFloat();
                    cityComponent1 = (check < 25.0F) ? getNextCityComponentP((CityComponentPieces.Start) start, CityComponentPieces.Avenue.class, random, chunkX + buildX, chunkZ + buildZ, 1, startPiece.baseY, false) :
                            getNextCityComponentP((CityComponentPieces.Start) start, CityComponentPieces.Street.class, random, chunkX + buildX, chunkZ + buildZ, 1, startPiece.baseY, false);
                    if (cityComponent1 != null) this.joinedTileMap.requiredTile = true;
                }else {
                    cityComponent1 = getNextCityComponentXZ((CityComponentPieces.Start)start, random, buildX, buildZ, 1, startPiece.baseY, false);
                }
                if (cityComponent1 != null){
                    buildList.remove(working);
                    String mapKey = "[" + (chunkX + buildX) + ", " + (chunkZ + buildZ) + "]";
                    this.joinedTileMap.intersections.set(newTile, (CityComponentPieces.Metropolis) cityComponent1);
                    this.startPiece.cityComponentMap.put(mapKey, (CityComponentPieces.Metropolis) cityComponent1);
                } else LogHelper.debug("Failed to build new tile adjacent to City Park");
            }

        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Street extends CityComponentPieces.Metropolis {

        public Street() {}

        protected Street(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random, MetropolisCityPlan boundingBox){
            super(tileTypeID, start);
            if (tileVariant < 0) {
                switch (tileVariant){
                    case -1: break;
                    default:
                        this.typeVariant = (random.nextInt(10) < 2) ? 1 : 0;
                        this.boundingBox = boundingBox;
                }

            }
            else {
                if (tileVariant > -1) {
                    this.typeVariant = tileVariant;
                    this.boundingBox = boundingBox;
                }
            }
            this.maxBuildingLevels = 21;
        }

        public static Street getNewCityStreet(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random,
                                                  int minX, int minY, int minZ, int maxX, int maxY, int maxZ){

            MetropolisCityPlan dimensions = new MetropolisCityPlan(minX, minY, minZ, maxX, maxY, maxZ, start.coordBaseMode, "CityStreet");
            return new Street(tileTypeID, tileVariant, start, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class CityPlaza extends CityComponentPieces.CitySquare {

        public CityPlaza() {}

        protected CityPlaza(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random, MetropolisCityPlan boundingBox){
            super(tileTypeID, -1, start, random, boundingBox);
            if (tileVariant < 0) {
                switch (tileVariant){
                    case -1: break;
                    default:
                        this.typeVariant = (random.nextInt(10) < 2) ? 1 : 0;
                        this.boundingBox = boundingBox;
                }

            }
            else {
                if (tileVariant > -1) {
                    this.typeVariant = tileVariant;
                    this.boundingBox = boundingBox;
                }
            }
        }

        public static CityPlaza getNewCityPlaza(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random,
                                                  int minX, int minY, int minZ, int maxX, int maxY, int maxZ){

            MetropolisCityPlan dimensions = new MetropolisCityPlan(minX, minY, minZ, maxX, maxY, maxZ, start.coordBaseMode, "CityPlaza");
            return new CityPlaza(tileTypeID, tileVariant, start, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Avenue extends CityComponentPieces.Street {

        @SuppressWarnings("unused")
        public Avenue() {}

        protected Avenue(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random, MetropolisCityPlan boundingBox){
            super(tileTypeID, -1, start, random, boundingBox);
            this.typeVariant = tileVariant;
            this.boundingBox = boundingBox;
        }

        public static Avenue getNewCityAvenue(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random,
                                                  int minX, int minY, int minZ, int maxX, int maxY, int maxZ){

            MetropolisCityPlan dimensions = new MetropolisCityPlan(minX, minY, minZ, maxX, maxY, maxZ, start.coordBaseMode, "CityAvenue");
            return new Avenue(tileTypeID, tileVariant, start, random, dimensions);
        }

        @Override
        public void buildComponent(CityComponent start, Random random){
            int chunkX, chunkZ;
            CityComponent cityComponent1;

            chunkX = this.getChunkPosition().getX() >> 4;
            chunkZ = this.getChunkPosition().getZ() >> 4;

            this.joinedTileMap.getIntersections(chunkX, chunkZ);
            this.joinedTileMap.getHasRequiredTile(CityComponentPieces.Avenue.class);
            this.joinedTileMap.getRightOfWay(CityComponentPieces.Avenue.class);

            List buildList = new ArrayList();
            for (int i = 0; i < this.joinedTileMap.intersections.size(); ++i){
                CityComponent component = this.joinedTileMap.intersections.get(i);
                if (component == null){
                    buildList.add(i);
                }
            }
            while (!buildList.isEmpty()){
                int working = random.nextInt(buildList.size());
                int buildX = 0;
                int buildZ = 0;
                int newTile = (int) buildList.get(working);
                switch(newTile){
                    case 0:
                        buildZ = 1;
                        break;
                    case 1:
                        buildX = -1;
                        break;
                    case 2:
                        buildZ = -1;
                        break;
                    case 3:
                        buildX = 1;
                        break;
                }
                if (!this.joinedTileMap.requiredTile){
                    if (this.joinedTileMap.rightOfWay == BuildMap.ROW_ALL ||
                            (((this.joinedTileMap.rightOfWay == BuildMap.ROW_EW) && (newTile == 1 || newTile == 3)) ||
                                    ((this.joinedTileMap.rightOfWay == BuildMap.ROW_NS) && (newTile == 0 || newTile == 2)))){
                        cityComponent1 = getNextCityComponentP((CityComponentPieces.Start) start, CityComponentPieces.Avenue.class, random, chunkX + buildX, chunkZ + buildZ, 1, startPiece.baseY, false);
                        if (cityComponent1 != null) this.joinedTileMap.requiredTile = true;
                    } else cityComponent1 = getNextCityComponentXZ((CityComponentPieces.Start)start, random, buildX, buildZ, 1, startPiece.baseY, false);
                }else {
                    cityComponent1 = getNextCityComponentXZ((CityComponentPieces.Start)start, random, buildX, buildZ, 1, startPiece.baseY, false);
                }
                if (cityComponent1 != null){
                    buildList.remove(working);
                    String mapKey = "[" + (chunkX + buildX) + ", " + (chunkZ + buildZ) + "]";
                    this.joinedTileMap.intersections.set(newTile, (CityComponentPieces.Metropolis) cityComponent1);
                    this.startPiece.cityComponentMap.put(mapKey, (CityComponentPieces.Metropolis) cityComponent1);
                } else LogHelper.debug("Failed to build new tile adjacent to Avenue");
            }

        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Alley extends CityComponentPieces.Street {

        @SuppressWarnings("unused")
        public Alley() {}

        protected Alley(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random, MetropolisCityPlan boundingBox){
            super(tileTypeID, -1, start, random, boundingBox);
            this.typeVariant = tileVariant;
            this.boundingBox = boundingBox;
        }

        public static Alley getNewCityAlley(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random,
                                                  int minX, int minY, int minZ, int maxX, int maxY, int maxZ){

            MetropolisCityPlan dimensions = new MetropolisCityPlan(minX, minY, minZ, maxX, maxY, maxZ, start.coordBaseMode, "CityAlley");
            return new Alley(tileTypeID, tileVariant, start, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Monument extends CityComponentPieces.CityPlaza {

        @SuppressWarnings("unused")
        public Monument() {}

        protected Monument(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random, MetropolisCityPlan boundingBox){
            super(tileTypeID, -1, start, random, boundingBox);
            this.typeVariant = tileVariant;
            this.boundingBox = boundingBox;
            this.maxBuildingLevels = 5;
        }

        public static Monument getNewCityMonument(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random,
                                                  int minX, int minY, int minZ, int maxX, int maxY, int maxZ){

            MetropolisCityPlan dimensions = new MetropolisCityPlan(minX, minY, minZ, maxX, maxY, maxZ, start.coordBaseMode, "CityMonument");
            return new Monument(tileTypeID, tileVariant, start, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class NeighborhoodDistrict extends CityComponentPieces.Street {

        public NeighborhoodDistrict() {}

        protected NeighborhoodDistrict(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random, MetropolisCityPlan boundingBox) {
            super(tileTypeID, -1, start, random, boundingBox);
            if (tileVariant < 0) {
                switch (tileVariant){
                    case -1: break;
                    default:
                        this.typeVariant = (random.nextInt(10) < 2) ? 1 : 0;
                        this.boundingBox = boundingBox;
                }

            }
            else {
                if (tileVariant > -1) {
                    this.typeVariant = tileVariant;
                    this.boundingBox = boundingBox;
                }
            }
            this.maxBuildingLevels = 3;
            this.minBuildingLevels = 1;
        }

        public static NeighborhoodDistrict getNewNeighborhood(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random,
                                                  int minX, int minY, int minZ, int maxX, int maxY, int maxZ){

            MetropolisCityPlan dimensions = new MetropolisCityPlan(minX, minY, minZ, maxX, maxY, maxZ, start.coordBaseMode, "CityNeighborhood");
            return new NeighborhoodDistrict(tileTypeID, tileVariant, start, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }

    }

    public static class NeighborhoodAlley extends CityComponentPieces.NeighborhoodDistrict {

        @SuppressWarnings("unused")
        public NeighborhoodAlley() {}

        protected NeighborhoodAlley(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random, MetropolisCityPlan boundingBox) {
            super(tileTypeID, -1, start, random, boundingBox);
            this.typeVariant = tileVariant;
            this.boundingBox = boundingBox;
        }

        public static NeighborhoodAlley getNewNeighborhoodAlley(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random,
                                                  int minX, int minY, int minZ, int maxX, int maxY, int maxZ){

            MetropolisCityPlan dimensions = new MetropolisCityPlan(minX, minY, minZ, maxX, maxY, maxZ, start.coordBaseMode, "NeighborhoodAlley");
            return new NeighborhoodAlley(tileTypeID, tileVariant, start, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class MunicipalDistrict extends CityComponentPieces.CityPlaza {

        @SuppressWarnings("unused")
        public MunicipalDistrict() {}

        protected MunicipalDistrict(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random, MetropolisCityPlan boundingBox){
            super(tileTypeID, -1, start, random, boundingBox);
            this.typeVariant = tileVariant;
            this.boundingBox = boundingBox;
            this.maxBuildingLevels = 4;
        }

        public static MunicipalDistrict getNewMunicipalDistrict(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random,
                                                  int minX, int minY, int minZ, int maxX, int maxY, int maxZ){

            MetropolisCityPlan dimensions = new MetropolisCityPlan(minX, minY, minZ, maxX, maxY, maxZ, start.coordBaseMode, "MunicipalDistrict");
            return new MunicipalDistrict(tileTypeID, tileVariant, start, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public abstract static class Building extends CityComponent {

        protected int totalLevels;
        protected CityComponentPieces.Start startPiece;
        protected boolean isRuins;
        private ArrayList floorPlan;
        private CityComponentPieces.Metropolis baseTile;

        public Building() {}

        protected Building(int tileTypeID, CityComponentPieces.Start start, CityComponentPieces.Metropolis base, Random random){
            super(tileTypeID);
            this.floorPlan = new ArrayList();
            this.startPiece = start;
            this.isRuins = start.isRuins;
            this.baseTile = base;
            this.totalLevels = random.nextInt(this.baseTile.maxBuildingLevels - this.baseTile.minBuildingLevels) + this.baseTile.minBuildingLevels;

        }

        protected CityComponent getNextBuldingLevel() {

            return null;
        }

        @Override
        public void buildComponent(CityComponent cityTile, Random random){

        }

        @Override
        public void buildComponent(CityComponent cityTile, Random random, int chunkX, int chunkZ){

        }

        @Override
        public boolean addComponentParts(World world, Random random){
            return false;
        }
    }

    public static class Tower extends CityComponentPieces.Building {

        public Tower() {}

        protected Tower(int tileTypeID, CityComponentPieces.Start start, CityComponentPieces.Metropolis base, Random random, BuildingFloorPlan floorPlan){
            super(tileTypeID, start, base, random);
        }

        public static Tower getNewTower(int tileTypeID, CityComponentPieces.Start start, Random random, int minX, int minY, int minZ,
                                        int maxX, int maxY, int maxZ){
            ChunkCoordIntPair baseCoords = new ChunkCoordIntPair(minX >> 4, minZ >> 4);
            CityComponentPieces.Metropolis baseTile = start.cityComponentMap.get(baseCoords.toString());
            BuildingFloorPlan dimensions = new BuildingFloorPlan(minX, minY, minZ, maxX, maxY, maxZ, baseTile.coordBaseMode, "Tower");
            return new Tower(tileTypeID, start, baseTile, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Institution extends CityComponentPieces.Building {

        public Institution() {}

        protected Institution(int tileTypeID, CityComponentPieces.Start start, CityComponentPieces.Metropolis base, Random random, BuildingFloorPlan floorPlan){
            super(tileTypeID, start, base, random);
        }

        public static Institution getNewInstitution(int tileTypeID, CityComponentPieces.Start start, Random random, int minX, int minY, int minZ,
                                        int maxX, int maxY, int maxZ){
            ChunkCoordIntPair baseCoords = new ChunkCoordIntPair(minX >> 4, minZ >> 4);
            CityComponentPieces.Metropolis baseTile = start.cityComponentMap.get(baseCoords.toString());
            BuildingFloorPlan dimensions = new BuildingFloorPlan(minX, minY, minZ, maxX, maxY, maxZ, baseTile.coordBaseMode, "Institution");
            return new Institution(tileTypeID, start, baseTile, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Industrial extends CityComponentPieces.Building {

        @SuppressWarnings("unused")
        public Industrial() {}

        protected Industrial(int tileTypeID, CityComponentPieces.Start start, CityComponentPieces.Metropolis base, Random random, BuildingFloorPlan floorPlan) {
            super(tileTypeID, start, base, random);
        }

        public static Industrial getNewIndustrial(int tileTypeID, CityComponentPieces.Start start, Random random, int minX, int minY, int minZ,
                                        int maxX, int maxY, int maxZ){
            ChunkCoordIntPair baseCoords = new ChunkCoordIntPair(minX >> 4, minZ >> 4);
            CityComponentPieces.Metropolis baseTile = start.cityComponentMap.get(baseCoords.toString());
            BuildingFloorPlan dimensions = new BuildingFloorPlan(minX, minY, minZ, maxX, maxY, maxZ, baseTile.coordBaseMode, "Industrial");
            return new Industrial(tileTypeID, start, baseTile, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Residential extends CityComponentPieces.Building {

        public Residential() {}

        protected Residential(int tileTypeID, CityComponentPieces.Start start, CityComponentPieces.Metropolis base, Random random, BuildingFloorPlan floorPlan) {
            super(tileTypeID, start, base, random);
        }

        public static Residential getNewResidential(int tileTypeID, CityComponentPieces.Start start, Random random, int minX, int minY, int minZ,
                                        int maxX, int maxY, int maxZ){
            ChunkCoordIntPair baseCoords = new ChunkCoordIntPair(minX >> 4, minZ >> 4);
            CityComponentPieces.Metropolis baseTile = start.cityComponentMap.get(baseCoords.toString());
            BuildingFloorPlan dimensions = new BuildingFloorPlan(minX, minY, minZ, maxX, maxY, maxZ, baseTile.coordBaseMode, "Residential");
            return new Residential(tileTypeID, start, baseTile, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class nFlat extends CityComponentPieces.Residential {

        @SuppressWarnings("unused")
        public nFlat() {}

        protected nFlat(int tileTypeID, CityComponentPieces.Start start, CityComponentPieces.Metropolis base, Random random, BuildingFloorPlan floorPlan, int flats) {
            super(tileTypeID, start, base, random, floorPlan);
        }

        public static nFlat getNewFlat(int tileTypeID, CityComponentPieces.Start start, Random random, int minX, int minY, int minZ,
                                        int maxX, int maxY, int maxZ){
            ChunkCoordIntPair baseCoords = new ChunkCoordIntPair(minX >> 4, minZ >> 4);
            CityComponentPieces.Metropolis baseTile = start.cityComponentMap.get(baseCoords.toString());
            BuildingFloorPlan dimensions = new BuildingFloorPlan(minX, minY, minZ, maxX, maxY, maxZ, baseTile.coordBaseMode, "nFlat");
            int flats = random.nextInt(baseTile.maxBuildingLevels) + 1;
            return new nFlat(tileTypeID, start, baseTile, random, dimensions, flats);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Prison extends CityComponentPieces.Tower {

        @SuppressWarnings("unused")
        public Prison() {}

        protected Prison(int tileTypeID, CityComponentPieces.Start start, CityComponentPieces.Metropolis base, Random random, BuildingFloorPlan floorPlan) {
            super(tileTypeID, start, base, random, floorPlan);
        }

        public static Prison getNewPrison(int tileTypeID, CityComponentPieces.Start start, Random random, int minX, int minY, int minZ,
                                        int maxX, int maxY, int maxZ){
            ChunkCoordIntPair baseCoords = new ChunkCoordIntPair(minX >> 4, minZ >> 4);
            CityComponentPieces.Metropolis baseTile = start.cityComponentMap.get(baseCoords.toString());
            BuildingFloorPlan dimensions = new BuildingFloorPlan(minX, minY, minZ, maxX, maxY, maxZ, baseTile.coordBaseMode, "Prison");
            return new Prison(tileTypeID, start, baseTile, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Municipal extends CityComponentPieces.Institution {

        @SuppressWarnings("unused")
        public Municipal() {}

        protected Municipal(int tileTypeID, CityComponentPieces.Start start, CityComponentPieces.Metropolis base, Random random, BuildingFloorPlan floorPlan) {
            super(tileTypeID, start, base, random, floorPlan);
        }

        public static Municipal getNewMunicipal(int tileTypeID, CityComponentPieces.Start start, Random random, int minX, int minY, int minZ,
                                        int maxX, int maxY, int maxZ){
            ChunkCoordIntPair baseCoords = new ChunkCoordIntPair(minX >> 4, minZ >> 4);
            CityComponentPieces.Metropolis baseTile = start.cityComponentMap.get(baseCoords.toString());
            BuildingFloorPlan dimensions = new BuildingFloorPlan(minX, minY, minZ, maxX, maxY, maxZ, baseTile.coordBaseMode, "Municipal");
            return new Municipal(tileTypeID, start, baseTile, random, dimensions);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }
}
