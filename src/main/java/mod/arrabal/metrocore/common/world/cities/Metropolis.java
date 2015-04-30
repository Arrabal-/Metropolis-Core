package mod.arrabal.metrocore.common.world.cities;

import mod.arrabal.metrocore.common.library.StatsHelper;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.handlers.world.WorldGenerationHandler;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.library.ModOptions;
import net.minecraft.entity.monster.*;
import net.minecraft.util.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
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
    public static CityLayoutPlan spawnPointBlock;

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
        //initBiomeLists();
        spawnList = new ArrayList();
        spawnPointBlock = null;
    }

    // Called during world generation to calculate necessary variables and test location for suitability.  If spawning criteria are met
    // will call doGenerateMetropolisStart()
    public static boolean generateMetropolis(Random random, int chunkX, int chunkZ,  World world, MetropolisGenerationContainer handler){

        /*double densityFactor = (double) ModOptions.metropolisMinDistanceBetween;
        double xDensityCheck, zDensityCheck;
        xDensityCheck = (double)Math.abs(chunkX) % densityFactor;
        zDensityCheck = (double)Math.abs(chunkZ) % densityFactor;
        if (genDensity == 0 || ((double)Math.abs(chunkX) % densityFactor != 0.0d) || ((double)Math.abs(chunkZ) % densityFactor != 0.0d)){
            LogHelper.trace("Kicking out generation attempt at " + chunkX + ", " + chunkZ + " due to density factor");
            return false;
        }


        random = getNewRandom(world, chunkX, chunkZ);

        //EntityPlayer entityplayer = world.getClosestPlayer(chunkX << 4, 64d, chunkZ << 4, (ModOptions.metropolisCenterSpawnShift + maxGenRadius) << 4);

        int checkX, checkZ;
        *//*if (entityplayer != null) {
            checkX = chunkX - entityplayer.chunkCoordX > 0 ? chunkX + random.nextInt(ModOptions.metropolisCenterSpawnShift) : chunkX - random.nextInt(ModOptions.metropolisCenterSpawnShift);
            checkZ = chunkZ - entityplayer.chunkCoordZ > 0 ? chunkZ + random.nextInt(ModOptions.metropolisCenterSpawnShift) : chunkZ - random.nextInt(ModOptions.metropolisCenterSpawnShift);

        }
        else {
            checkX = (random.nextInt(2) == 0 ? chunkX - random.nextInt(ModOptions.metropolisCenterSpawnShift) : chunkX + random.nextInt(ModOptions.metropolisCenterSpawnShift));
            checkZ = (random.nextInt(2) == 0 ? chunkZ - random.nextInt(ModOptions.metropolisCenterSpawnShift) : chunkZ + random.nextInt(ModOptions.metropolisCenterSpawnShift));
        }*//*
        checkX = chunkX;
        checkZ = chunkZ;
        if (checkForSpawnConflict(world, checkX, checkZ)){
            LogHelper.trace("Spawn point conflict at chunk [" + checkX + ", " + checkZ + "]");
            return false;
        }
        if (isBiomeValid(world, (checkX << 4) + 8, (checkZ << 4) + 8, (minGenRadius << 4)/2 + 8)){

            int genRadiusX = random.nextInt(maxGenRadius - minGenRadius + 1) + minGenRadius;
            int genRadiusZ = random.nextInt(maxGenRadius - minGenRadius + 1) + minGenRadius;
            genRadiusX = (genRadiusZ > 3 && genRadiusX < 4) ? genRadiusX + 1 : genRadiusX;
            genRadiusZ = (genRadiusX > 3 && genRadiusZ < 4) ? genRadiusZ + 1 : genRadiusZ;
            int genMinX = (checkX << 4) - (genRadiusX << 4);
            int genMaxX = (checkX << 4) + 15 + (genRadiusX << 4);
            int genMinZ = (checkZ << 4) - (genRadiusZ << 4);
            int genMaxZ = (checkZ << 4) + 15 + (genRadiusZ << 4);
            if (checkForGenerationConflict(genMinX, genMinZ, genMaxX, genMaxZ, handler)){
                LogHelper.trace("Existing urban area intersects with or is within " + densityFactor + " chunks of [" + genMinX + ", " + genMinZ + "] to [" +
                        genMaxX + ", " + genMaxZ + "]");
                return false;
            }
            int[] heightMap = getGroundHeightMap(world, genMinX, genMinZ, genMaxX, genMaxZ, 50);
            int checkY = StatsHelper.getStaticMean(heightMap);
            int devY = StatsHelper.getStaticStandardDeviation(heightMap, checkY);
            Random rarityCheck = new Random(world.getTotalWorldTime());
            if (devY <= ConfigHandler.metropolisMaxHeightVariation && rarityCheck.nextDouble() <= genRarity) {
                LogHelper.debug("Successful generation check centered at chunk [" + checkX + ", " + checkZ + "], position [" + genMinX + ", " + genMinZ + "] to [" +
                        genMaxX + ", " + genMaxZ + "]. Mean Height:  " + checkY + ".  Ground height deviation:  " + devY);
                //handler.addToGenerationMap(new MetropolisBaseBB(genMinX, genMinZ, genMaxX, genMaxZ, "BlacklistZone"));
                handler.doGenerateMetropolisStart(world, checkX, checkZ, checkY, genRadiusX, genRadiusZ);
                handler.currentStart = new ChunkCoordIntPair(checkX, checkZ);
                return true;
            }
            else {
                String sString = "";
                if (devY <= ConfigHandler.metropolisMaxHeightVariation) { sString = "  Failure due to genRarity check.";}
                LogHelper.debug("Failed generation check centered at chunk [" + checkX + ", " + checkZ + "], position [" + genMinX + ", " + genMinZ + "] to [" +
                        genMaxX + ", " + genMaxZ + "]. Ground height deviation:  " + devY + sString);
            }
        }*/
        return false;
    }
}


