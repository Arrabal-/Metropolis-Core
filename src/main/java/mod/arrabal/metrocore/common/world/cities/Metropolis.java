package mod.arrabal.metrocore.common.world.cities;

import mod.arrabal.metrocore.api.StatsHelper;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.handlers.data.MetropolisDataHandler;
import mod.arrabal.metrocore.common.handlers.world.WorldGenerationHandler;
import mod.arrabal.metrocore.common.library.LogHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 6/6/2014.
 */
public final class Metropolis {

    private static List spawnList;

    private static BiomeDictionary.Type[] allowedBiomeTypes = {
            BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.DESERT, BiomeDictionary.Type.FROZEN,
            BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.MAGICAL};
    private static BiomeDictionary.Type[] disallowedBiomeTypes = {BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.SWAMP,
            BiomeDictionary.Type.WATER, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END,
            BiomeDictionary.Type.MUSHROOM, BiomeDictionary.Type.HILLS};
    private static List allowedBiomeList;
    private static List disallowedBiomeTypeList;

    private static int minDistanceBetween;
    private static int maxDistanceBetween;
    private static int minGenRadius;
    private static int maxGenRadius;
    private static int spawnBlockRadius;
    private static double genDensity;
    private static double genRarity;
    public static MetropolisBaseBB spawnPointBlock;

    public enum StructureType {URBAN, ROAD, METROPOLIS, CITY, TOWN, HIGHWAY, PAVED, COUNTRY_ROAD}

    private static final int RAND_SEED = 15456985;

    public Metropolis() {
        minDistanceBetween = ConfigHandler.metropolisMinDistanceBetween;
        maxDistanceBetween = ConfigHandler.metropolisMaxDistanceBetween;
        minGenRadius = ConfigHandler.metropolisMinGenRadius;
        maxGenRadius = ConfigHandler.metropolisMaxGenRadius;
        if (ConfigHandler.metropolisGenDensity >= 1d){
            genDensity = 0;
        }
        if (ConfigHandler.metropolisGenDensity < 0d){
            genDensity = 1;
        }
        else{
            genDensity = 1d / (1d - ConfigHandler.metropolisGenDensity);
        }
        genRarity = ConfigHandler.metropolisGenRarity;
        spawnBlockRadius = ConfigHandler.metropolisSpawnBlockRadius;
        initBiomeLists();
        spawnList = new ArrayList();
    }



    //Called prior to initial world generation to block out the area around the spawn point to prevent city generation too close to spawn.
    private static MetropolisBaseBB blockSpawnArea(World world, int radius){
        int spawnMinX = world.getSpawnPoint().posX - radius;
        int spawnMinZ = world .getSpawnPoint().posZ - radius;
        return new MetropolisBaseBB(world, spawnMinX, spawnMinZ, spawnMinX + 2*radius, spawnMinZ + 2*radius, "SpawnPointBlock");
    }

    public static MetropolisBaseBB doBlockSpawnArea(World world){
        return blockSpawnArea(world, spawnBlockRadius);
    }

    // Called during world generation to calculate necessary variables and test location for suitability.  If spawning criteria are met
    // will call doGenerateMetropolisStart()
    public static void generateMetropolis(Random random, int chunkX, int chunkZ,  World world, MetropolisDataHandler dataHandler){

        int densityFactor = (int) (genDensity * (minDistanceBetween/2));
        if ((genDensity != 0) && ((chunkX % densityFactor != 0 && chunkZ % densityFactor != 0) ||
                (genDensity == 1))) {
            //DebugMessenger.message("Kicking out generation attempt due to density factor");
            return;
        }
        random = getNewRandom(world, chunkX, chunkZ);

        int checkX = (random.nextInt(2) == 0 ? chunkX - random.nextInt(maxDistanceBetween/2) : chunkX + random.nextInt(maxDistanceBetween/2));
        int checkZ = (random.nextInt(2) == 0 ? chunkZ - random.nextInt(maxDistanceBetween/2) : chunkZ + random.nextInt(maxDistanceBetween/2));

        if (checkForSpawnConflict(world, checkX, checkZ)){
            LogHelper.debug("Spawn point conflict at chunk [" + checkX + ", " + checkZ + "]");
            return;
        }
        if (isBiomeValid(world, checkX * 16 + 8, checkZ * 16 + 8, (minGenRadius * 16)/2 + 8)){

            int genRadiusX = random.nextInt(maxGenRadius - minGenRadius + 1) + minGenRadius;
            int genRadiusZ = random.nextInt(maxGenRadius - minGenRadius + 1) + minGenRadius;
            int genMinX = (checkX * 16) - (genRadiusX * 16);
            int genMaxX = (checkX * 16) + 15 + (genRadiusX * 16);
            int genMinZ = (checkZ * 16) - (genRadiusZ * 16);
            int genMaxZ = (checkZ * 16) + 15 + (genRadiusZ + 16);
            if (checkForGenerationConflict(world, genMinX, genMinZ, genMaxX, genMaxZ, dataHandler)){
                LogHelper.debug("Existing urban area intersects with or is within " + minDistanceBetween + " chunks of [" + genMinX + ", " + genMinZ + "] to [" +
                        genMaxX + ", " + genMaxZ + "]");
                return;
            }
            int[] heightMap = getGroundHeightMap(world, genMinX, genMinZ, genMaxX, genMaxZ);
            int checkY = StatsHelper.getStaticMean(heightMap);
            int devY = StatsHelper.getStaticStandardDeviation(heightMap, checkY);
            // insert check against genRarity to see if valid location should be thrown out to make population more scarce.  Can't be a
            // straight randomly generated value since it will use the same value for each generation attempt.
            if (devY < 4 /*&& random.nextDouble() <= genRarity*/) {
                LogHelper.debug("Successful generation check centered at chunk [" + checkX + ", " + checkZ + "], position [" + genMinX + ", " + genMinZ + "] to [" +
                        genMaxX + ", " + genMaxZ + "]. Mean Height:  " + checkY + ".  Ground height deviation:  " + devY);
                //temporary for testing
                WorldGenerationHandler.addToBlacklistMap(new MetropolisBaseBB(world, genMinX, genMinZ, genMaxX, genMaxZ, "BlacklistZone"));
                doGenerateMetropolisStart(world, checkX, checkZ, checkY, genRadiusX, genRadiusZ);
            }
            else {
                String sString = "";
                if (devY < 4) { sString = "  Failure due to genRarity check.";}
                LogHelper.debug("Failed generation check centered at chunk [" + checkX + ", " + checkZ + "], position [" + genMinX + ", " + genMinZ + "] to [" +
                        genMaxX + ", " + genMaxZ + "]. Ground height deviation:  " + devY + sString);
            }
        }

    }

    private static void doGenerateMetropolisStart(World world, int chunkX, int chunkZ, int avgY, int xGenRadius, int zGenRadius){
        // determine spawn list
        MetropolisStart start = new MetropolisStart(world, chunkX, chunkZ, avgY, xGenRadius, zGenRadius, spawnList);
        WorldGenerationHandler.cityStartMap.put(start.getStartKey(), start);
    }

    private static boolean checkForSpawnConflict(World world, int chunkX, int chunkZ){
        MetropolisBaseBB checkSpawnBlock = blockSpawnArea(world, spawnBlockRadius);
        return checkSpawnBlock.isVecInside(chunkX * 16, 64, chunkZ * 16);
    }

    private static boolean checkForGenerationConflict(World world, int minPosX, int minPosZ, int maxPosX, int maxPosZ, MetropolisDataHandler dataHandler){
        return WorldGenerationHandler.checkUrbanGenerationConflict(world, new MetropolisBaseBB(world, minPosX, minPosZ,
                maxPosX, maxPosZ,"GenerationCheck"), dataHandler);
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
            if (vanillaBiomes[j] == null) {break;}
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

    protected static boolean isBiomeValid(World worldObj, int posX, int posZ, int radius) {
        return worldObj.getWorldChunkManager().areBiomesViable(posX, posZ, radius, allowedBiomeList);
    }

    @SuppressWarnings("unchecked")
    public static boolean isVillageNear(World world, int posX, int posY, int posZ, int genRadius){
        if(world.villageCollectionObj != null){
            for (Village village : (List<Village>) world.villageCollectionObj.getVillageList()){
                if (Math.sqrt(village.getCenter().getDistanceSquared(posX, posY, posZ)) < village.getVillageRadius() + genRadius) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int[] getGroundHeightMap(World world, int minX, int minZ, int maxX, int maxZ){
        int blocks = (maxX - minX) * (maxZ - minZ);
        int[] heightMap = new int[blocks];
        int index = 0;
        for (int i = minX; i < maxX; i++){
            for (int j = minZ; j < maxZ; j++){
                heightMap[index] = world.getTopSolidOrLiquidBlock(i, j) - 1;
                index += 1;
            }
        }
        return heightMap;
    }

    public static Random getNewRandom(World world, int chunkX, int chunkZ){
        Random random = new Random(world.getSeed());
        long l = (chunkX / maxDistanceBetween) * (random.nextLong()+ 1L);
        long l2 = (chunkZ / maxDistanceBetween) * (random.nextLong() + 1L);
        long l3 = (l * l2) * (random.nextLong()+1L);
        random.setSeed(l3 ^ world.getSeed());
        return random;
    }

}


