package mod.arrabal.metrocore.common.world.gen;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.handlers.data.CityBoundsSaveData;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.library.ModOptions;
import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import mod.arrabal.metrocore.common.world.biome.CityBiomeManager;
import mod.arrabal.metrocore.common.world.cities.MetropolisStart;
import mod.arrabal.metrocore.common.world.structure.CityComponent;
import mod.arrabal.metrocore.common.world.structure.CityComponentPieces;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;

import java.util.*;

/**
 * Created by Evan on 4/14/2015.
 */
public class MapGenMetropolis extends MapGenBase {

    private static double genDensity;
    private static double genRarity;
    private static int spawnBlockRadius;
    private static int minDistanceBetween;
    private static int minGenRadius;
    private static int maxGenRadius;
    private CityBoundsSaveData cityMap;
    private MetropolisStart currentGeneratingStart;
    private BiomeGenBase[] biomesToGen;
    private CityComponent lastBuiltComponent;
    private MapGenStructureIO dataHandler;
    private MetropolisBoundingBox blockedSpawnZone;
    //private int cityTilesToGen;

    public MetropolisBoundingBox generatingZone;
    public ChunkCoordIntPair currentStart;




    public MapGenMetropolis(){
        super();
        minDistanceBetween = ModOptions.BASE_DIST_BETWEEN_CITY;
        minGenRadius = ConfigHandler.metropolisMinGenRadius;
        maxGenRadius = ConfigHandler.metropolisMaxGenRadius;
        this.range = maxGenRadius - minGenRadius + 1;
        if (ConfigHandler.metropolisGenDensity >= 1d){
            genDensity = 1d;
        }
        if (ConfigHandler.metropolisGenDensity < 0d){
            genDensity = 0d;
        } else{
            genDensity = 1d / ConfigHandler.metropolisGenDensity;
        }
        spawnBlockRadius = ConfigHandler.metropolisSpawnBlockRadius;
        genRarity = ConfigHandler.metropolisGenRarity;
        this.dataHandler = new MapGenStructureIO();
        this.blockedSpawnZone = null;
    }

    /**
     * Get the stored city layout for the passed in chunk coordinates and construct the referenced city tile.
     * This method initiates placing blocks in the world.
     *
     * @param world = current world
     * @param random = random number generator
     * @param chunkCoords = coordinates of chunk to construct city tile within
     * @return = boolean flag indicating success/failure of constructing city tile in world
     */
    public boolean buildMetropolis(World world, Random random, ChunkCoordIntPair chunkCoords){

        //LogHelper.debug("CALL TO MapGenMetropolis.buildMetropolis TO RUN START GENERATOR");
        //if the chunk contains a city start then this is easy
        //this needs to take into account where it is not possible to build cities
        if (this.dataHandler.startMapContainsKey(chunkCoords.toString())){
            MetropolisStart start = this.dataHandler.getStartFromKey(chunkCoords.toString());
            if (!start.getCurrentlyBuilding()){
                boolean flag = start.generate(world, random, chunkCoords);
                if (flag){
                    LogHelper.debug("Placing blocks in Start chunk");
                    this.currentGeneratingStart = start;
                }
                return flag;
            }
        }
        // since the chunk does not contain a city start, need to find the closest city start and build the correct tile
        else {
            MetropolisStart start = null;
            boolean validGenChunk = false;
            Iterator iterator = this.dataHandler.startMap.entrySet().iterator();
            while (iterator.hasNext() && !validGenChunk){
                Map.Entry entry = (Map.Entry) iterator.next();
                start = (MetropolisStart) entry.getValue();
                if (start.getMaxBBIntersection(chunkCoords.getXStart(), chunkCoords.getZStart(), chunkCoords.getXEnd(), chunkCoords.getZEnd())){
                    validGenChunk = true;
                }
            }
            if (validGenChunk && !start.getCurrentlyBuilding()) {
                boolean flag = false;
                flag = start.generate(world, random, chunkCoords);
                if (flag){
                    this.currentGeneratingStart = start;
                }
                return flag;
            }
        }
        return false;
    }

    /**
     * Checks to see if the current generating chunk meets the criteria for generating a city.  Checks to make sure that the city
     * bounds will not overlap with the blocked spawn area and is sufficiently far away from other city locations
     *
     * @param world = current world
     * @param random = random number generator
     * @param chunkX = generating chunk X coord
     * @param chunkZ = generating chunk Z coord
     * @return true if city can generate, false if city generation is not allowed
     */
    public boolean canGenerateMetropolis(World world, Random random, int chunkX, int chunkZ){

        this.initializeCityMapData(world);
        this.generatingZone = new MetropolisBoundingBox(new BlockPos(chunkX << 4, 1, chunkZ << 4), new BlockPos((chunkX << 4) + 15, 255, (chunkZ << 4) + 15));
        if (this.checkGenerationConflict(this.generatingZone)){
            // Failed distance check
            LogHelper.trace("Too close to existing city " + this.generatingZone.toString());
            this.generatingZone = null;
            return false;
        }
        LogHelper.debug("Can generate Metropolis at [" + chunkX + ", " + chunkZ + "]");
        return true;
    }

    /**
     * Wrapper method to use configured minimum city spawn distance as default parameter for conflict check
     *
     * @param cityBounds = MetropolisBoundingBox to check for conflict with other city
     * @return true if conflict with other city, false if no conflict
     */
    public boolean checkGenerationConflict(MetropolisBoundingBox cityBounds){
        return checkGenerationConflict(cityBounds,  ModOptions.metropolisMinDistanceBetween); //blocks
    }

    /**
     * Check new generating city for conflicts with existing city layouts.
     *
     * @param cityBounds = MetropolisBoundingBox to check for conflicts with other city
     * @param blocks = minimum distance in blocks of one axis for checking for conflict.  Checks blocks squared for distance
     *               edge to edge between city layouts
     * @return true if conflict, false in no conflict
     */
    public boolean checkGenerationConflict(MetropolisBoundingBox cityBounds, int blocks){
        boolean conflict = false;
        if (!this.dataHandler.isGenMapEmpty()){
            Iterator iterator = this.dataHandler.urbanGenerationMap.entrySet().iterator();
            while (iterator.hasNext() && !conflict){
                Map.Entry entry = (Map.Entry) iterator.next();
                MetropolisBoundingBox value = (MetropolisBoundingBox) entry.getValue();
                conflict = value.intersectsWith(cityBounds);
                if (blocks > 0) conflict = conflict || value.getSquaredDistance(cityBounds, false) < (blocks * blocks);
            }
        }
        return conflict;
    }

    private void initializeCityMapData(World worldIn){
        // check to see if cityMap is initialized
        if (this.cityMap == null){
            // load data from disk
            this.cityMap = (CityBoundsSaveData) worldIn.getPerWorldStorage().loadData(CityBoundsSaveData.class, "generatedCities");
            // check to see if there was anything to load
            if (this.cityMap == null){
                // no saved data.  Create new empty cityMap save file
                this.cityMap = new CityBoundsSaveData("generatedCities");
                worldIn.getPerWorldStorage().setData("generatedCities", this.cityMap);
            }
            else{
                // get saved cityMap data
                this.dataHandler.urbanGenerationMap = this.cityMap.getBoundingBoxMap();
            }
        }
    }

    public int getDefaultGenRadius(boolean getMax){
        if (getMax) return maxGenRadius;
        return minGenRadius;
    }

    /**
     * Attempts to generate new MetropolisStart near center of valid city biome and if a valid location is found initiates
     * the full generation of the city layout
     *
     * @param world = current world
     * @param chunkX = offset X chunk generation coordinate
     * @param chunkZ = offset Z chunk generation coordinate
     * @param xGen = max chunk distance on x axis to generate city within
     * @param zGen = max chunk distance on z axis to generate city within
     * @param maxRadius = max chunk generation radius around start chunk to perform generation of city layout
     * @param maxComponents = max number of city tiles to generate in this city layout
     * @return = newly generated MetropolisStart
     */
    @SuppressWarnings("unchecked")
    public MetropolisStart generateMetropolisStart(World world, int chunkX, int chunkZ, int xGen, int zGen, int maxRadius, int maxComponents){
        LogHelper.trace("CALL TO MepGenMetropolis.generateMetropolisStart TO INSTANTIATE NEW METROPOLIS START");
        MetropolisStart start = new MetropolisStart(world, chunkX, chunkZ, 63, xGen, zGen, maxRadius, maxComponents);
        this.currentStart = new ChunkCoordIntPair(chunkX, chunkZ);
        String hashKey = start.getStartKey();
        LogHelper.debug("Starting build city map with start at " + hashKey);
        LogHelper.debug("Max gen radius (x,z): " + start.getMaxGenRadius(true) + " " + start.getMaxGenRadius(false));
        LogHelper.debug("BaseY: " + start.getBaseY());
        LogHelper.debug("City Class: " + start.cityLayoutStart.citySize + " " + "Road Grid: " + start.cityLayoutStart.roadGrid);
        this.dataHandler.addToStartMap(start);
        start.cityLayoutStart.cityComponentMap.put(hashKey, start.cityLayoutStart);
        //currently this does not check for appropriate biomes it just kicks of the changed generation in adjacent chunks.
        start.cityLayoutStart.buildComponent(start.cityLayoutStart, this.rand);
        return start;
    }

    private Vec3 getVectorFromClosestPlayer(World world, double posX, double posZ){
        EntityPlayer closestPlayer = world.getClosestPlayer(posX, 64.0d, posZ, 320.0d);
        if (closestPlayer != null) {
            float yaw = closestPlayer.rotationYaw;
            float f1 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
            float f2 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
            return new Vec3((double) -f2, 0.0d, (double) -f1);
        }

        return new Vec3(0d, 0d, 0d);
    }

    private ChunkCoordIntPair getValidOffsetStart(int chunkX, int chunkZ, Vec3 heading, int walkback){
        int offsetX = MathHelper.ceiling_double_int(heading.xCoord * (double) ConfigHandler.metropolisMaxGenRadius);
        int offsetZ = MathHelper.ceiling_double_int(heading.zCoord * (double) ConfigHandler.metropolisMaxGenRadius);
        return new ChunkCoordIntPair(offsetX + chunkX, offsetZ + chunkZ);
    }

    public ChunkCoordIntPair getStartGenCoords(World world, int chunkXIn, int chunkZIn){
        Vec3 playerHeading = getVectorFromClosestPlayer(world, (chunkXIn << 4) + 8, (chunkZIn << 4) + 8);
        int genChunkX, genChunkZ;
            ChunkCoordIntPair offsetChunk = getValidOffsetStart(chunkXIn, chunkZIn, playerHeading, 0);
            genChunkX = offsetChunk.chunkXPos;
            genChunkZ = offsetChunk.chunkZPos;

        return new ChunkCoordIntPair(genChunkX, genChunkZ);
    }

    public boolean checkInSpawnZone(World worldIn, int chunkX, int chunkZ){
        BlockPos spawnPoint = worldIn.getSpawnPoint();
        if (this.blockedSpawnZone == null || blockedSpawnZone.intersectsWith(new MetropolisBoundingBox(chunkX << 4, chunkZ << 4, (chunkX << 4) + 15, (chunkZ << 4) + 15))){
            this.blockedSpawnZone = new MetropolisBoundingBox(spawnPoint.add(-this.spawnBlockRadius, 1, -this.spawnBlockRadius), spawnPoint.add(this.spawnBlockRadius, 255, this.spawnBlockRadius));
            return true;
        }
        return false;
    }

    @Override
    public void generate(IChunkProvider chunkProvider, World worldIn, int chunkX, int chunkZ, ChunkPrimer chunkPrimer){
        if (this.canGenerateMetropolis(worldIn, this.rand, chunkX,chunkZ)) {
            int k = this.range;
            this.worldObj = worldIn;
            this.rand.setSeed(worldIn.getSeed());
            int genXDim = this.rand.nextInt((maxGenRadius * 2) - (minGenRadius * 2)) + (minGenRadius * 2);
            int genZDim = this.rand.nextInt((maxGenRadius * 2) - (minGenRadius * 2)) + (minGenRadius * 2);
            int cityTilesToGen = genXDim * genZDim;
            genXDim = (int) Math.ceil((genXDim/2.0d));
            genZDim = (int) Math.ceil((genZDim/2.0d));
            // (minGenRadius * 2) * (minGenRadius * 2) + this.rand.nextInt((maxGenRadius * 2) * (maxGenRadius * 2));
            long l = this.rand.nextLong();
            long i1 = this.rand.nextLong();
            // set the city start first
            MetropolisStart start = this.generateMetropolisStart(worldIn, chunkX, chunkZ, genXDim, genZDim, Math.max(genXDim, genZDim), cityTilesToGen);
            LogHelper.info("Completed building city layout with start at " + start.getStartKey());
            this.dataHandler.addToBoundingBoxMap(start.cityLayoutStart.cityPlan);
            this.cityMap.saveBoundingBoxData(start.cityLayoutStart.cityPlan);
        }
    }

    protected boolean generateCityTile(World worldIn, MetropolisStart start, int buildX, int buildZ, Integer startChunkX, Integer startChunkZ, ChunkPrimer chunkPrimer) {
        //this needs to be totally rewritten.  determining actual gen coords is done elswhere and this should just kick off the generation of tile for the start piece
        //which will then initiate chained generation for all other tiles within the CityComponentPieces classes.
        LogHelper.debug("CALL TO MapGenMetropolis.generateCityTile TO INSTANTIATE CITY TILES.  GEN SHOULD BE HAPPENING IN CityComponentPieces");
        CityComponent cityComponent;
        int worldX, worldZ;
        if (startChunkX == null || startChunkZ == null) {
            worldX = start.getStartX() + buildX;
            worldZ = start.getStartZ() + buildZ;
        } else{
            worldX = startChunkX + buildX;
            worldZ = startChunkZ + buildZ;
        }
        String newChunkKey = "[" + worldX + ", " + worldZ + "]";
        this.biomesToGen = worldIn.getWorldChunkManager().loadBlockGeneratorData(this.biomesToGen, worldX * 16, worldZ * 16, 16, 16);
        boolean invalidBiome = false;
        for (int i = 0; i < this.biomesToGen.length; i++) {
            if (CityBiomeManager.getInvalidBiomes().contains(biomesToGen[i])){
                invalidBiome = true;
                break;
            }
        }
        if (invalidBiome || start.cityLayoutStart.getTilesLeftToBuild() <= 0) return false;
        if (start.cityLayoutStart.cityComponentMap.containsKey(newChunkKey)) {
            cityComponent = start.cityLayoutStart.cityComponentMap.get(newChunkKey);
            if (cityComponent.mappingComplete) return false;
            cityComponent.buildComponent(start.cityLayoutStart, this.rand);
            this.lastBuiltComponent = cityComponent;
        } else {
            // found empty city tile.  Need to generate new tile.
            LogHelper.debug("Found empty city tile during component build at " + newChunkKey);
            start.cityLayoutStart.buildComponent(start.cityLayoutStart, this.rand, buildX, buildZ);
            // start new chained generation
            this.generateCityTile(worldIn, start, 0, 0, worldX, worldZ, chunkPrimer);
        }
        return true;
    }

    public MetropolisStart getCurrentGeneratingStart(){
        return this.currentGeneratingStart;
    }

    public void clearCurrentGeneratingStart() {this.currentGeneratingStart = null;}

    public HashMap<String, CityComponentPieces.Metropolis> getCurrentBuildCityMap(){
        return this.dataHandler.currentBuildCity;
    }

    private class StageCoords {

        public int chunkX;
        public int chunkZ;

        StageCoords(int x, int z){
            this.chunkX = x;
            this.chunkZ = z;
        }
    }
}
