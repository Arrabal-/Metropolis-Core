package mod.arrabal.metrocore.common.world.gen;

import mod.arrabal.metrocore.common.init.Biomes;
import mod.arrabal.metrocore.common.library.StatsHelper;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.handlers.data.CityBoundsSaveData;
import mod.arrabal.metrocore.common.handlers.data.MetropolisDataHandler;
import mod.arrabal.metrocore.common.handlers.world.WorldGenerationHandler;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.library.ModOptions;
import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import mod.arrabal.metrocore.common.world.biome.MetropolisBiomeDecorator;
import mod.arrabal.metrocore.common.world.cities.MetropolisStart;
import mod.arrabal.metrocore.common.world.structure.CityComponent;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import net.minecraftforge.common.BiomeDictionary;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private ChunkCoordIntPair currentStart;
    private BiomeGenBase[] biomesToGen;
    private List<BiomeGenBase> cityBiomes;
    private CityComponent lastBuiltComponent;
    //private int cityTilesToGen;

    public MetropolisBoundingBox generatingZone;
    public static MetropolisStart currentGeneratingStart = null;





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
        this.cityBiomes = new ArrayList<>();
        this.cityBiomes.add(Biomes.plainsMetro);
    }

    public boolean buildMetropolis(World world, Random random, ChunkCoordIntPair chunkCoords){

        //LogHelper.debug("CALL TO MapGenMetropolis.buildMetropolis TO RUN START GENERATOR");
        if (this.dataHandler.startMapContainsKey(chunkCoords.toString())){
            MetropolisStart start = this.dataHandler.getStartFromKey(chunkCoords.toString());
            if (!start.getCurrentlyBuilding()){
                boolean flag = start.generate(world, random, chunkCoords);
                if (flag){
                    LogHelper.debug("Building Start chunk");
                    MapGenStructureIO.currentBuildCity.remove(start.getStartKey());
                    MapGenMetropolis.currentGeneratingStart = start;
                }
                return flag;
            }
        }else {
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
                LogHelper.debug("Building non-start chunk");
                /* Should just generate loading chunk city tile
                flag = start.generate(world, random, chunkCoords);
                if (flag){
                    MapGenStructureIO.currentBuildCity.remove(chunkCoords.toString());
                    MapGenMetropolis.currentGeneratingStart = start;
                }
                */
                return flag;
            }
        }
        return false;
    }

    public boolean canGenerateMetropolis(World world, Random random, int chunkX, int chunkZ){

        this.initializeCityMapData(world);
        this.generatingZone = new MetropolisBoundingBox(new BlockPos(chunkX << 4, 1, chunkZ << 4), new BlockPos((chunkX << 4) + 15, 255, (chunkZ << 4) + 15));
        MetropolisBoundingBox spawnArea = new MetropolisBoundingBox(world.getSpawnPoint(), world.getSpawnPoint().add(16, 0, 16));
        if ((this.generatingZone.getSquaredDistance(spawnArea, true) < (spawnBlockRadius * spawnBlockRadius)) || this.checkGenerationConflict(this.generatingZone)){
            // Failed distance check
            LogHelper.trace("Too close to existing city or spawn zone " + this.generatingZone.toString());
            this.generatingZone = null;
            return false;
        }
        LogHelper.debug("Can generate Metropolis at [" + chunkX + ", " + chunkZ + "]");
        return true;
    }

    public boolean checkGenerationConflict(MetropolisBoundingBox cityBounds){
        return checkGenerationConflict(cityBounds,  ModOptions.metropolisMinDistanceBetween); //blocks
    }

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

    public boolean checkGenerationConflict(ChunkCoordIntPair chunkPos,  int range){
        boolean intersection = false;
        MetropolisBoundingBox boundingBox = new MetropolisBoundingBox(chunkPos.getXStart(), chunkPos.getZStart(), chunkPos.getXEnd() + range * 16, chunkPos.getZEnd() + range * 16);
        if (!this.dataHandler.isGenMapEmpty()){
            Iterator iterator = this.dataHandler.urbanGenerationMap.entrySet().iterator();
            while (iterator.hasNext() && !intersection){
                Map.Entry entry = (Map.Entry) iterator.next();
                MetropolisBoundingBox value = (MetropolisBoundingBox) entry.getValue();
                intersection = value.intersectsWith(boundingBox);
            }
        }
        return intersection;
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

    public static int[] getGroundHeightMap(World world, int minX, int minZ, int maxX, int maxZ, int sampleSize){
        //Attempting to improve efficiency by only taking a sample of the height map
        int[] heightMap;
        Chunk testChunk;
        if (sampleSize > 0) {
            heightMap = new int[sampleSize];
            Random heightCheck = new Random(world.getTotalWorldTime());
            for (int i = 0; i < sampleSize; i++) {
                int newX = heightCheck.nextInt(maxX - minX) + minX;
                int newZ = heightCheck.nextInt(maxZ - minZ) + minZ;
                BlockPos newPos = new BlockPos(newX, 0, newZ);
                //testChunk = world.getChunkFromBlockCoords(newPos);
                if (world.isBlockLoaded(newPos, false)){
                    heightMap[i] = world.getChunkFromBlockCoords(newPos).getHeight(newPos) - 1;
                } else LogHelper.info("Chunk not loaded when trying to check heightmap");
                //world.getTopSolidOrLiquidBlock(newPos).getY() - 1;
            }
        } else {
            int blocks = (maxX - minX + 1) * (maxZ - minZ + 1);
            heightMap = new int[blocks];
            int index = 0;
            for (int i = minX; i < maxX + 1; i++) {
                for (int j = minZ; j < maxZ + 1; j++) {
                    BlockPos newPos = new BlockPos(i,0,j);
                    //testChunk = world.getChunkFromBlockCoords(newPos);
                    if (world.isBlockLoaded(newPos, false)){
                        heightMap[index] =  world.getChunkFromBlockCoords(newPos).getHeight(newPos) - 1;
                    } else LogHelper.info("Chunk not loaded when trying to check heightmap");
                    index += 1;
                }
            }
        }
        return heightMap;
    }

    /**
     *
     * @param world
     * @param chunkX
     * @param chunkZ
     * @param xGen
     * @param zGen
     * @param maxRadius
     * @param maxComponents
     * @return
     */
    @SuppressWarnings("unchecked")
    public MetropolisStart generateMetropolisStart(World world, int chunkX, int chunkZ, int xGen, int zGen, int maxRadius, int maxComponents){
        LogHelper.debug("CALL TO MepGenMetropolis.generateMetropolisStart TO INSTANTIATE NEW METROPOLIS START");
        Vec3 playerHeading = getVectorFromClosestPlayer(world, (chunkX << 4) + 8, (chunkZ << 4) + 8);
        int genChunkX, genChunkZ;
        if (playerHeading.equals(new Vec3(0d, 0d, 0d))){
            genChunkX = chunkX;
            genChunkZ = chunkZ;
        }
        else{
            ChunkCoordIntPair offsetChunk = getValidOffsetStart(chunkX, chunkZ, playerHeading, 0);
            genChunkX = offsetChunk.chunkXPos;
            genChunkZ = offsetChunk.chunkZPos;
        }
        MetropolisStart start = new MetropolisStart(world, genChunkX, genChunkZ, 63, xGen, zGen, maxComponents);
        this.currentStart = new ChunkCoordIntPair(genChunkX, genChunkZ);
        String hashKey = start.getStartKey();
//        start.cityLayoutStart.cityComponentMap.put(hashKey,start.cityLayoutStart);
//        MapGenStructureIO.currentBuildCity.put(hashKey, start.cityLayoutStart);
//        start.cityLayoutStart.reduceTilesToBuild();
//        start.cityLayoutStart.buildComponent(start.cityLayoutStart, this.rand);
        LogHelper.debug("Starting build city map with start at " + hashKey);
        LogHelper.debug("Max gen radius (x,z): " + start.getMaxGenRadius(true) + " " + start.getMaxGenRadius(false));
        LogHelper.debug("BaseY: " + start.getBaseY());
        LogHelper.debug("City Class: " + start.cityLayoutStart.citySize + " " + "Road Grid: " + start.cityLayoutStart.roadGrid);
        this.dataHandler.addToStartMap(start);
        return start;
    }

    private Vec3 getVectorFromClosestPlayer(World world, double posX, double posZ){
        EntityPlayer closestPlayer = world.getClosestPlayer(posX, 64.0d, posZ, 320.0d);
        if (closestPlayer != null) {
            float yaw = closestPlayer.rotationYaw;
            float f1 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
            float f2 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
            return new Vec3((double) -f2, 0.0d, (double) - f1);
        }

        return new Vec3(0d, 0d, 0d);
    }

    private ChunkCoordIntPair getValidOffsetStart(int chunkX, int chunkZ, Vec3 heading, int walkback){
        int offsetX = MathHelper.ceiling_double_int(heading.xCoord * (double)ConfigHandler.metropolisMaxGenRadius);
        int offsetZ = MathHelper.ceiling_double_int(heading.zCoord * (double)ConfigHandler.metropolisMaxGenRadius);
        return new ChunkCoordIntPair(offsetX + chunkX, offsetZ + chunkZ);
    }

    private ChunkCoordIntPair getCoordsToBuild(Map stageMap, int mapIndex){
        //Integer mapKey = new Integer(mapIndex);
        if (stageMap.containsKey(mapIndex)){
            StageCoords buildCoords = (StageCoords) stageMap.get(mapIndex);
            stageMap.remove(mapIndex);
            return new ChunkCoordIntPair(buildCoords.chunkX, buildCoords.chunkZ);
        }
        return null;
    }

    @Override
    public void func_175792_a(IChunkProvider chunkProvider, World worldIn, int chunkX, int chunkZ, ChunkPrimer chunkPrimer){
        LogHelper.debug("CALL TO MapGenMetropolis.func_175792_a TO INSTANTIATE NEW METROPOLIS START");
        int k = this.range;
        this.worldObj = worldIn;
        this.rand.setSeed(worldIn.getSeed());
        int genXDim = this.rand.nextInt((maxGenRadius * 2) - (minGenRadius * 2)) + (minGenRadius * 2);
        int genZDim = this.rand.nextInt((maxGenRadius * 2) - (minGenRadius * 2)) + (minGenRadius * 2);
        int cityTilesToGen = genXDim * genZDim;
        // (minGenRadius * 2) * (minGenRadius * 2) + this.rand.nextInt((maxGenRadius * 2) * (maxGenRadius * 2));
        int genRadiusX = maxGenRadius;
        int genRadiusZ = maxGenRadius;
        long l = this.rand.nextLong();
        long i1 = this.rand.nextLong();
        // set the city start first
        MetropolisStart start = this.generateMetropolisStart(worldIn, chunkX, chunkZ, genXDim, genZDim, maxGenRadius, cityTilesToGen);

        LogHelper.info("Completed building city layout with start at " + start.getStartKey());
        this.dataHandler.addToBoundingBoxMap(start.cityLayoutStart.cityPlan);
        this.cityMap.saveBoundingBoxData(start.cityLayoutStart.cityPlan);
    }

    protected boolean generateCityTile(World worldIn, MetropolisStart start, int buildX, int buildZ, Integer startChunkX, Integer startChunkZ, ChunkPrimer chunkPrimer) {

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
            if (!this.cityBiomes.contains(this.biomesToGen[i])) invalidBiome = true;
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

    private class StageCoords {

        public int chunkX;
        public int chunkZ;

        StageCoords(int x, int z){
            this.chunkX = x;
            this.chunkZ = z;
        }
    }
}
