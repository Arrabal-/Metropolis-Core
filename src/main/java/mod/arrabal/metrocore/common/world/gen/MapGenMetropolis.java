package mod.arrabal.metrocore.common.world.gen;

import mod.arrabal.metrocore.common.library.StatsHelper;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.handlers.data.CityBoundsSaveData;
import mod.arrabal.metrocore.common.handlers.data.MetropolisDataHandler;
import mod.arrabal.metrocore.common.handlers.world.WorldGenerationHandler;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.library.ModOptions;
import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import mod.arrabal.metrocore.common.world.cities.MetropolisStart;
import mod.arrabal.metrocore.common.world.structure.CityComponent;
import net.minecraft.entity.monster.*;
import net.minecraft.util.BlockPos;
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
    private static List allowedBiomeList;
    private static List disallowedBiomeTypeList;
    private static BiomeDictionary.Type[] allowedBiomeTypes = {
            BiomeDictionary.Type.MESA, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS,
            BiomeDictionary.Type.SANDY, BiomeDictionary.Type.SNOWY,BiomeDictionary.Type.WASTELAND};
    private static BiomeDictionary.Type[] disallowedBiomeTypes = {BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.SWAMP,
            BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END,
            BiomeDictionary.Type.MUSHROOM, BiomeDictionary.Type.HILLS};

    private MetropolisDataHandler dataHandler;
    private CityBoundsSaveData cityMap;
    private List spawnList = new ArrayList();
    private ChunkCoordIntPair currentStart;

    public MetropolisBoundingBox spawnPointBlock;
    public MetropolisBoundingBox generatingZone;
    //public ConcurrentHashMap<String, MetropolisBoundingBox> generatedCities = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, MetropolisStart> startMap = new ConcurrentHashMap<>();
//    public ConcurrentHashMap<String, MetropolisBoundingBox> genCheckedZones = new ConcurrentHashMap<>();





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
        this.dataHandler = new MetropolisDataHandler();
        this.initBiomeLists();
    }

    public boolean buildMetropolis(World world, Random random, ChunkCoordIntPair chunkCoords){

        if (this.dataHandler.startMapContainsKey(chunkCoords.toString())){
            MetropolisStart start = this.dataHandler.getStartFromKey(chunkCoords.toString());
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
                if (start.getMaxBBIntersection(chunkCoords.getXStart(), chunkCoords.getZStart(), chunkCoords.getXEnd(), chunkCoords.getZEnd())){
                    validGenChunk = true;
                }
            }
            if (validGenChunk && !start.getCurrentlyBuilding()) {
                return start.generate(world, random);
            }
        }
        return false;
    }

    public boolean canGenerateMetropolis(World world, Random random, int chunkX, int chunkZ){

        if (this.checkForSpawnConflict(world, chunkX, chunkZ)){
            LogHelper.trace("Spawn point conflict at chunk [" + chunkX + ", " + chunkZ + "]");
            return false;
        }
        this.initializeCityMapData(world);
        this.generatingZone = new MetropolisBoundingBox(new BlockPos((chunkX - maxGenRadius) << 4, 1, (chunkZ - maxGenRadius) << 4), new BlockPos(((chunkX + maxGenRadius) << 4) + 15, 255, ((chunkZ + maxGenRadius) << 4) + 15));
/*        if (this.checkFailedGenerationAttempt(this.generatingZone)){
            LogHelper.trace("Already checked parts of the area around [" + chunkX + ", " + chunkZ + "] for city generation");
            return false;
        }*/
        if (this.checkGenerationConflict(this.generatingZone)){
            // city already exists within this area
            LogHelper.trace("Found overlap with existing generated city within area " + this.generatingZone.toString());
            this.generatingZone = null;
            return false;
        }
        /*double densityFactor = (double) ModOptions.metropolisMinDistanceBetween;
        double xDensityCheck, zDensityCheck;
        int rarityFactor = (int) (1/genRarity);
        Random rarityCheck = new Random(world.getTotalWorldTime());
        xDensityCheck = (double)Math.abs(chunkX + rarityCheck.nextInt(rarityFactor)) % densityFactor;
        zDensityCheck = (double)Math.abs(chunkZ + rarityCheck.nextInt(rarityFactor)) % densityFactor;
        if (genDensity == 0 || ((xDensityCheck != 0.0d) && (zDensityCheck != 0.0d))){
            LogHelper.trace("Kicking out generation attempt at " + chunkX + ", " + chunkZ + " due to density factor");
            return false;
        }
        return true;*/
        return chunkX == 96 && chunkZ == 96;
    }

    private boolean checkForSpawnConflict(World world, int chunkX, int chunkZ){
        if (this.spawnPointBlock == null){
            this.spawnPointBlock = this.blockSpawnArea(world, spawnBlockRadius);
        }
        return this.spawnPointBlock.isVecInside(chunkX << 4, 64, chunkZ << 4);
    }

    public boolean checkGenerationConflict(MetropolisBoundingBox cityBounds){
        boolean conflict = false;
        if (!this.dataHandler.isGenMapEmpty()){
            Iterator iterator = this.dataHandler.urbanGenerationMap.entrySet().iterator();
            while (iterator.hasNext() && !conflict){
                Map.Entry entry = (Map.Entry) iterator.next();
                MetropolisBoundingBox value = (MetropolisBoundingBox) entry.getValue();
                conflict = value.intersectsWith(cityBounds) ||
                        value.getSquaredDistance(cityBounds, false) < (ModOptions.metropolisMinDistanceBetween * ModOptions.metropolisMinDistanceBetween);
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
/*
    private boolean checkFailedGenerationAttempt(MetropolisBoundingBox cityBounds){
        boolean conflict = false;
        if (!this.genCheckedZones.isEmpty()){
            Iterator iterator = this.genCheckedZones.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                MetropolisBoundingBox value = (MetropolisBoundingBox) entry.getValue();
                conflict = value.intersectsWith(cityBounds) ||
                        value.getSquaredDistance(cityBounds, false) < (ModOptions.metropolisMinDistanceBetween * ModOptions.metropolisMinDistanceBetween);
                if (conflict){
                    value.expandTo(cityBounds);
                    entry.setValue(value);
                }
            }
        }
        return conflict;
    }*/

    //Called prior to initial world generation to block out the area around the spawn point to prevent city generation too close to spawn.
    private MetropolisBoundingBox blockSpawnArea(World world, int radius){
        int spawnX = world.getSpawnPoint().getX();
        int spawnZ = world.getSpawnPoint().getZ();
        int spawnMinX = spawnX - radius;
        int spawnMinZ = spawnZ - radius;
        return new MetropolisBoundingBox(spawnMinX, spawnMinZ, spawnMinX + 2*radius, spawnMinZ + 2*radius, "SpawnPointBlock");
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

    @SuppressWarnings("unchecked")
    private void initializeBiomeListWithVanillaBiomes() {

        //Clean-up of vanilla biome registry
        if (!BiomeDictionary.isBiomeRegistered(BiomeGenBase.stoneBeach)){
            BiomeDictionary.registerBiomeType(BiomeGenBase.stoneBeach, BiomeDictionary.Type.BEACH);
        }
        if (!BiomeDictionary.isBiomeOfType(BiomeGenBase.iceMountains, BiomeDictionary.Type.MOUNTAIN)){
            BiomeDictionary.registerBiomeType(BiomeGenBase.iceMountains, BiomeDictionary.Type.MOUNTAIN);
        }
        if (!BiomeDictionary.isBiomeOfType(BiomeGenBase.coldBeach, BiomeDictionary.Type.BEACH)){
            BiomeDictionary.registerBiomeType(BiomeGenBase.coldBeach, BiomeDictionary.Type.BEACH);
        }
        if (!BiomeDictionary.isBiomeOfType(BiomeGenBase.extremeHillsPlus, BiomeDictionary.Type.MOUNTAIN)){
            BiomeDictionary.registerBiomeType(BiomeGenBase.extremeHillsPlus, BiomeDictionary.Type.MOUNTAIN);
        }

        BiomeGenBase[] vanillaBiomes = BiomeGenBase.getBiomeGenArray();


        for (int j = 0; j < vanillaBiomes.length; j++){
            if (vanillaBiomes[j] == null) {continue;}
            BiomeDictionary.Type[] biomeTypes = BiomeDictionary.getTypesForBiome(vanillaBiomes[j]);
            boolean pass = true;
            for (int k = 0; k < biomeTypes.length; k++){
                if (disallowedBiomeTypeList.contains(biomeTypes[k])){
                    pass = false;
                }
            }
            if (pass){
                allowedBiomeList.add(vanillaBiomes[j]);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private void  initBiomeLists(){
        allowedBiomeList = new ArrayList();
        disallowedBiomeTypeList = new ArrayList();
        Collections.addAll(disallowedBiomeTypeList, disallowedBiomeTypes);
        initializeBiomeListWithVanillaBiomes();
        for (int i = 0; i < allowedBiomeTypes.length; i++){
            BiomeGenBase[] biomes = BiomeDictionary.getBiomesForType(allowedBiomeTypes[i]);
            for (int j = 0; j < biomes.length; j++){
                if (!allowedBiomeList.contains(biomes[j])){
                    BiomeDictionary.Type[] biomeTypes = BiomeDictionary.getTypesForBiome(biomes[j]);
                    boolean flag = true;
                    for (int k = 0; k < biomeTypes.length; k++){
                        if (disallowedBiomeTypeList.contains(biomeTypes[k])){
                            flag = false;
                        }
                    }
                    if (flag){
                        allowedBiomeList.add(biomes[j]);
                    }

                }
            }
        }
    }

    public boolean isBiomeValid(World worldIn, int posX, int posZ, int radius) {
        return worldIn.getWorldChunkManager().areBiomesViable(posX, posZ, radius, allowedBiomeList);
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
                testChunk = world.getChunkFromBlockCoords(newPos);
                if (world.isBlockLoaded(newPos, false)){
                    heightMap[i] = testChunk.getHeight(newPos) - 1;
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
                    testChunk = world.getChunkFromBlockCoords(newPos);
                    if (world.isBlockLoaded(newPos, false)){
                        heightMap[index] =  world.getChunkFromBlockCoords(newPos).getHeight(newPos) - 1;
                    } else LogHelper.info("Chunk not loaded when trying to check heightmap");
                    index += 1;
                }
            }
        }
        return heightMap;
    }

    @SuppressWarnings("unchecked")
    public MetropolisStart generateMetropolisStart(World world, int chunkX, int chunkZ, int xGenRadius, int zGenRadius){
        if (this.spawnList.isEmpty()){
            this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 100, 4, 4));
            this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityZombie.class, 100, 4, 4));
            this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntitySpider.class, 100, 4, 4));
            this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityCreeper.class, 100, 4, 4));
            this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityEnderman.class, 10, 1, 4));
        }
        int[] heightMap = getGroundHeightMap(world, chunkX << 4, chunkZ << 4, (chunkX << 4) + 15, (chunkZ << 4) + 15, 0);
        MetropolisStart start = new MetropolisStart(world, chunkX, chunkZ, StatsHelper.getStaticMean(heightMap), xGenRadius, zGenRadius, this.spawnList);
        this.dataHandler.addToStartMap(start);
        return start;
    }

    @Override
    public void func_175792_a(IChunkProvider chunkProvider, World worldIn, int chunkX, int chunkZ, ChunkPrimer chunkPrimer){
        int k = this.range;
        this.worldObj = worldIn;
        this.rand.setSeed(worldIn.getSeed());
        int genRadiusX = this.rand.nextInt(k) + this.minGenRadius;
        int genRadiusZ = this.rand.nextInt(k) + this.minGenRadius;
        genRadiusX = (genRadiusZ > 3 && genRadiusX < 4) ? genRadiusX + 1 : genRadiusX;
        genRadiusZ = (genRadiusX > 3 && genRadiusZ < 4) ? genRadiusZ + 1 : genRadiusZ;
        long l = this.rand.nextLong();
        long i1 = this.rand.nextLong();
        // set the city start first
        MetropolisStart start = this.generateMetropolisStart(worldIn, chunkX, chunkZ, genRadiusX, genRadiusZ);
        this.currentStart = new ChunkCoordIntPair(chunkX, chunkZ);
        String hashKey = start.getStartKey();
        start.cityLayoutStart.cityComponentMap.put(hashKey,start.cityLayoutStart);
        start.cityLayoutStart.buildComponent(start.cityLayoutStart, this.rand);
        LogHelper.debug("Starting build city map with start at " + hashKey);
        LogHelper.debug("Max gen radius (x,z): " + start.getMaxGenRadius(true) + " " + start.getMaxGenRadius(false));
        LogHelper.debug("BaseY: " + start.getBaseY());
        LogHelper.debug("City Class: " + start.cityLayoutStart.citySize + " " + "Road Grid: " + start.cityLayoutStart.roadGrid);
        int radiusIterations = Math.max(start.getMaxGenRadius(true), start.getMaxGenRadius(false));
        for (int iteration = 2; iteration <= radiusIterations; iteration++) {
            for (int buildX = -iteration; buildX <= iteration; buildX++){
                for (int buildZ = -iteration; buildZ <= iteration; buildZ++){
                    if ((Math.abs(buildX) < iteration && Math.abs(buildZ) < iteration) || (Math.abs(buildX) > start.getMaxGenRadius(true)) || (Math.abs(buildZ) > start.getMaxGenRadius(false))) continue;
                    long l1 = (long)buildX * l;
                    long i2 = (long)buildZ * i1;
                    this.rand.setSeed(l1 ^ i2 ^ worldIn.getSeed());
                    this.generateCityTile(worldIn, start, buildX, buildZ, chunkX, chunkZ, chunkPrimer);
                }
            }
        }
        LogHelper.info("Completed building city layout with start at " + start.getStartKey());
        this.dataHandler.addToBoundingBoxMap(start.cityLayoutStart.cityPlan);
        this.cityMap. saveBoundingBoxData(start.cityLayoutStart.cityPlan);
    }

    protected void generateCityTile(World worldIn, MetropolisStart start, int buildX, int buildZ, int startChunkX, int startChunkZ, ChunkPrimer chunkPrimer){

        CityComponent cityComponent;
        int worldX, worldZ;
        worldX = start.getStartX() + buildX;
        worldZ = start.getStartZ() + buildZ;
        String newChunkKey = "[" + worldX + ", " + worldZ + "]";
        if (start.cityLayoutStart.cityComponentMap.containsKey(newChunkKey)) {
            cityComponent = start.cityLayoutStart.cityComponentMap.get(newChunkKey);
            cityComponent.buildComponent(start.cityLayoutStart, this.rand);
        }
        else{
            // found empty city tile.  Need to generate new tile.  Should take place after chained generation of tiles
            LogHelper.debug("Found empty city tile during component build at " + newChunkKey);
            start.cityLayoutStart.buildComponent(start.cityLayoutStart, this.rand, buildX, buildZ);
        }
    }
}
