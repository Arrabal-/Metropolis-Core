package mod.arrabal.metrocore.common.world.gen;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.handlers.data.CityBoundsSaveData;
import mod.arrabal.metrocore.common.handlers.data.MetropolisDataHandler;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.library.ModOptions;
import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
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

    public MetropolisBoundingBox spawnPointBlock;
    public MetropolisBoundingBox generatingZone;
    public ConcurrentHashMap<String, MetropolisBoundingBox> generatedCities = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, MetropolisBoundingBox> genCheckedZones;





    public MapGenMetropolis(){
        super();
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
        spawnBlockRadius = ConfigHandler.metropolisSpawnBlockRadius;
        genRarity = ConfigHandler.metropolisGenRarity;
        this.dataHandler = new MetropolisDataHandler();
        this.initBiomeLists();
    }

    public boolean buildMetropolis(World world, Random random, ChunkCoordIntPair chunkCoords){

        return false;
    }

    public boolean canGenerateMetropolis(World world, Random random, int chunkX, int chunkZ){

        if (this.checkForSpawnConflict(world, chunkX, chunkZ)){
            LogHelper.trace("Spawn point conflict at chunk [" + chunkX + ", " + chunkZ + "]");
            return false;
        }
        this.generatingZone = new MetropolisBoundingBox(new BlockPos((chunkX - maxGenRadius) << 4, 1, (chunkZ - maxGenRadius) << 4), new BlockPos(((chunkX + maxGenRadius) << 4) + 15, 255, ((chunkZ + maxGenRadius) << 4) + 15));
        if (this.checkGenerationConflict(this.generatingZone)){
            // city already exists within this area
            LogHelper.trace("Found overlap with existing generated city within area " + this.generatingZone.toString());
            this.generatingZone = null;
            return false;
        }
        double densityFactor = (double) ModOptions.metropolisMinDistanceBetween;
        double xDensityCheck, zDensityCheck;
        xDensityCheck = (double)Math.abs(chunkX) % densityFactor;
        zDensityCheck = (double)Math.abs(chunkZ) % densityFactor;
        if (genDensity == 0 || (xDensityCheck != 0.0d) || (zDensityCheck != 0.0d)){
            LogHelper.trace("Kicking out generation attempt at " + chunkX + ", " + chunkZ + " due to density factor");
            return false;
        }
        Random rarityCheck = new Random(world.getTotalWorldTime());
        if (rarityCheck.nextDouble() <= genRarity){
            return true;
        }
        LogHelper.trace("Kicking out generation attempt at " + chunkX + ", " + chunkZ + " due to rarity check");
        return false;
    }

    private boolean checkForSpawnConflict(World world, int chunkX, int chunkZ){
        if (this.spawnPointBlock == null){
            this.spawnPointBlock = this.blockSpawnArea(world, spawnBlockRadius);
        }
        return this.spawnPointBlock.isVecInside(chunkX << 4, 64, chunkZ << 4);
    }

    public boolean checkGenerationConflict(MetropolisBoundingBox cityBounds){
        boolean conflict = false;
        if (!this.generatedCities.isEmpty()){
            Iterator iterator = this.generatedCities.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                MetropolisBoundingBox value = (MetropolisBoundingBox) entry.getValue();
                conflict = value.intersectsWith(cityBounds) ||
                        value.getSquaredDistance(cityBounds, false) < (ModOptions.metropolisMinDistanceBetween * ModOptions.metropolisMinDistanceBetween);
            }
        }
        return conflict;
    }

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
                this.generatedCities = this.cityMap.getBoundingBoxMap();
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

    public boolean isBiomeValid(World worldIn, int posX, int posZ, int radius) {
        return worldIn.getWorldChunkManager().areBiomesViable(posX, posZ, radius, allowedBiomeList);
    }

    public int getDefaultGenRadius(boolean getMax){
        if (getMax) return maxGenRadius;
        return minGenRadius;
    }

    @Override
    protected void func_180701_a(World worldIn, int genChunkX, int genChunkZ, int centerChunkX, int centerChunkZ, ChunkPrimer chunkPrimer) {

        this.initializeCityMapData(worldIn);
    }
}
