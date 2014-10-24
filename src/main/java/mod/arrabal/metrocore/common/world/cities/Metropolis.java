package mod.arrabal.metrocore.common.world.cities;

import mod.arrabal.metrocore.api.StatsHelper;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.handlers.world.WorldGenerationHandler;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.library.ModOptions;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
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

    private List spawnList;

    private static BiomeDictionary.Type[] allowedBiomeTypes = {
            BiomeDictionary.Type.MESA, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS,
            BiomeDictionary.Type.SANDY, BiomeDictionary.Type.SNOWY,BiomeDictionary.Type.WASTELAND};
    private static BiomeDictionary.Type[] disallowedBiomeTypes = {BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.SWAMP,
            BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END,
            BiomeDictionary.Type.MUSHROOM, BiomeDictionary.Type.HILLS};
    private static List allowedBiomeList;
    private static List disallowedBiomeTypeList;

    private static int minDistanceBetween;
    private static int minGenRadius;
    private static int maxGenRadius;
    private static int spawnBlockRadius;
    private static double genDensity;
    private static double genRarity;
    public static MetropolisBaseBB spawnPointBlock;

    public enum StructureType {URBAN, ROAD, METROPOLIS, CITY, TOWN, HIGHWAY, PAVED, COUNTRY_ROAD}

    private static final int RAND_SEED = 15456985;

    public Metropolis() {
        minDistanceBetween = ModOptions.BASE_DIST_BETWEEN_CITY;
        minGenRadius = ConfigHandler.metropolisMinGenRadius;
        maxGenRadius = ConfigHandler.metropolisMaxGenRadius;
        if (ConfigHandler.metropolisGenDensity >= 1d){
            genDensity = 1d;
        }
        if (ConfigHandler.metropolisGenDensity < 0d){
            genDensity = 0d;
        } else{
            genDensity = 1d / ConfigHandler.metropolisGenDensity;
        }
        genRarity = ConfigHandler.metropolisGenRarity;
        if (genRarity > 1d) {
            genRarity = 1.0d;
        } else if (genRarity < 0d){
            genRarity = 0d;
        }
        spawnBlockRadius = ConfigHandler.metropolisSpawnBlockRadius;
        initBiomeLists();
        spawnList = new ArrayList();
        spawnPointBlock = null;
    }



    //Called prior to initial world generation to block out the area around the spawn point to prevent city generation too close to spawn.
    private static MetropolisBaseBB blockSpawnArea(World world, int radius){
        int spawnMinX = world.getSpawnPoint().posX - radius;
        int spawnMinZ = world .getSpawnPoint().posZ - radius;
        return new MetropolisBaseBB(spawnMinX, spawnMinZ, spawnMinX + 2*radius, spawnMinZ + 2*radius, "SpawnPointBlock");
    }

    // Called during world generation to calculate necessary variables and test location for suitability.  If spawning criteria are met
    // will call doGenerateMetropolisStart()
    public static boolean generateMetropolis(Random random, int chunkX, int chunkZ,  World world, MetropolisGenerationContainer handler){

        int densityFactor = ModOptions.metropolisMinDistanceBetween;
        if ((genDensity == 0 || ((chunkX % densityFactor != 0 && chunkZ % densityFactor != 0)))){
            LogHelper.trace("Kicking out generation attempt due to density factor");
            return false;
        }


        random = getNewRandom(world, chunkX, chunkZ);

        EntityPlayer entityplayer = world.getClosestPlayer(chunkX << 4, 64d, chunkZ << 4, (ModOptions.metropolisCenterSpawnShift + maxGenRadius) << 4);

        int checkX, checkZ;
        if (entityplayer != null) {
            checkX = chunkX - entityplayer.chunkCoordX > 0 ? chunkX + random.nextInt(ModOptions.metropolisCenterSpawnShift) : chunkX - random.nextInt(ModOptions.metropolisCenterSpawnShift);
            checkZ = chunkZ - entityplayer.chunkCoordZ > 0 ? chunkZ + random.nextInt(ModOptions.metropolisCenterSpawnShift) : chunkZ - random.nextInt(ModOptions.metropolisCenterSpawnShift);
        }
        else {
            checkX = (random.nextInt(2) == 0 ? chunkX - random.nextInt(ModOptions.metropolisCenterSpawnShift) : chunkX + random.nextInt(ModOptions.metropolisCenterSpawnShift));
            checkZ = (random.nextInt(2) == 0 ? chunkZ - random.nextInt(ModOptions.metropolisCenterSpawnShift) : chunkZ + random.nextInt(ModOptions.metropolisCenterSpawnShift));
        }
        if (checkForSpawnConflict(world, checkX, checkZ)){
            LogHelper.trace("Spawn point conflict at chunk [" + checkX + ", " + checkZ + "]");
            return false;
        }
        if (isBiomeValid(world, (checkX << 4) + 8, (checkZ << 4) + 8, (minGenRadius << 4)/2 + 8)){

            int genRadiusX = random.nextInt(maxGenRadius - minGenRadius + 1) + minGenRadius;
            int genRadiusZ = random.nextInt(maxGenRadius - minGenRadius + 1) + minGenRadius;
            int genMinX = (checkX << 4) - (genRadiusX << 4);
            int genMaxX = (checkX << 4) + 15 + (genRadiusX << 4);
            int genMinZ = (checkZ << 4) - (genRadiusZ << 4);
            int genMaxZ = (checkZ << 4) + 15 + (genRadiusZ << 4);
            if (checkForGenerationConflict(genMinX, genMinZ, genMaxX, genMaxZ, handler)){
                LogHelper.trace("Existing urban area intersects with or is within " + densityFactor + " chunks of [" + genMinX + ", " + genMinZ + "] to [" +
                        genMaxX + ", " + genMaxZ + "]");
                return false;
            }
            int[] heightMap = getGroundHeightMap(world, genMinX, genMinZ, genMaxX, genMaxZ, 100);
            int checkY = StatsHelper.getStaticMean(heightMap);
            int devY = StatsHelper.getStaticStandardDeviation(heightMap, checkY);
            Random rarityCheck = new Random(world.getWorldTime());
            if (devY < 4 && rarityCheck.nextDouble() <= genRarity) {
                LogHelper.debug("Successful generation check centered at chunk [" + checkX + ", " + checkZ + "], position [" + genMinX + ", " + genMinZ + "] to [" +
                        genMaxX + ", " + genMaxZ + "]. Mean Height:  " + checkY + ".  Ground height deviation:  " + devY);
                handler.addToGenerationMap(new MetropolisBaseBB(genMinX, genMinZ, genMaxX, genMaxZ, "BlacklistZone"));
                handler.doGenerateMetropolisStart(world, checkX, checkZ, checkY, genRadiusX, genRadiusZ);
                return true;
            }
            else {
                String sString = "";
                if (devY < 4) { sString = "  Failure due to genRarity check.";}
                LogHelper.debug("Failed generation check centered at chunk [" + checkX + ", " + checkZ + "], position [" + genMinX + ", " + genMinZ + "] to [" +
                        genMaxX + ", " + genMaxZ + "]. Ground height deviation:  " + devY + sString);
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public void generateMetropolisStart(World world, int chunkX, int chunkZ, int avgY, int xGenRadius, int zGenRadius){
        if (this.spawnList.isEmpty()){
            this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 100, 4, 4));
            this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityZombie.class, 100, 4, 4));
            this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntitySpider.class, 100, 4, 4));
            this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityCreeper.class, 100, 4, 4));
            this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityEnderman.class, 10, 1, 4));
        }
        MetropolisStart start = new MetropolisStart(world, chunkX, chunkZ, avgY, xGenRadius, zGenRadius, this.spawnList);
        WorldGenerationHandler.getGenContainerFromWorld(world).addToStartMap(start);
        start.generate(world);
    }

    private static boolean checkForSpawnConflict(World world, int chunkX, int chunkZ){
        if (spawnPointBlock == null){
            spawnPointBlock = blockSpawnArea(world, spawnBlockRadius);
        }
        return spawnPointBlock.isVecInside(chunkX << 4, 64, chunkZ << 4);
    }

    private static boolean checkForGenerationConflict(int minPosX, int minPosZ, int maxPosX, int maxPosZ, MetropolisGenerationContainer handler){
        return handler.doConflictCheck(new MetropolisBaseBB(minPosX, minPosZ,maxPosX, maxPosZ,"GenerationCheck"));
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

    public static int[] getGroundHeightMap(World world, int minX, int minZ, int maxX, int maxZ, int sampleSize){
        //Attempting to improve efficiency by only taking a sample of the height map
        int[] heightMap;
        if (sampleSize > 0) {
            heightMap = new int[sampleSize];
            Random heightCheck = new Random(world.getWorldTime());
            for (int i = 0; i < sampleSize; i++) {
                heightMap[i] = world.getTopSolidOrLiquidBlock(heightCheck.nextInt(maxX - minX) + minX,
                        heightCheck.nextInt(maxZ - minZ) + minZ) - 1;
            }
        } else {
            int blocks = (maxX - minX) * (maxZ - minZ);
            heightMap = new int[blocks];
            int index = 0;
            for (int i = minX; i < maxX; i++) {
                for (int j = minZ; j < maxZ; j++) {
                    heightMap[index] = world.getTopSolidOrLiquidBlock(i, j) - 1;
                    index += 1;
                }
            }
        }
        return heightMap;
    }

    private static Random getNewRandom(World world, int chunkX, int chunkZ){
        Random random = new Random(world.getSeed());
        long l = (chunkX / ModOptions.metropolisMinDistanceBetween) * (random.nextLong()+ 1L);
        long l2 = (chunkZ / ModOptions.metropolisMinDistanceBetween) * (random.nextLong() + 1L);
        long l3 = (l * l2) * (random.nextLong()+1L);
        random.setSeed(l3 ^ world.getSeed());
        return random;
    }

}


