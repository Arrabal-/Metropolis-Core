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

        LogHelper.debug("CALL TO DEPRECATED Metropolis.generateMetropolis!");
        return false;
    }
}


