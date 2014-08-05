package mod.arrabal.metrocore.common.world.structure;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.common.BiomeDictionary;

import java.util.*;

/**
 * Created by Arrabal on 12/26/13.
 */

//Defines this particular world generator procedure
public class MapGenUrban extends MapGenStructure{

    private static List spawnList;

    private static BiomeDictionary.Type[] allowedBiomeTypes = {
            BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.DESERT,
            BiomeDictionary.Type.FROZEN, BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.MAGICAL};
    private static BiomeDictionary.Type[] disallowedBiomeTypes = {BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.SWAMP,
            BiomeDictionary.Type.WATER, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END,
            BiomeDictionary.Type.MUSHROOM};
    private static List allowedBiomeList;
    private static List disallowedBiomeTypeList;

    private int minDistanceBetween;
    private int maxDistanceBetween;
    private int minGenRadius;
    private int maxGenRadius;
    private boolean isRuin;
    private int terrainType;
    //private double genChance = 0.25D;
    private int maxHeight;
    private int minHeight;
    private int avgHeight;
    private int xGen;
    private int zGen;

    private static final String __OBFID = "CL_A0000001";
    private static final int RAND_SEED = 15456985;


    public enum StructureType {URBAN, ROAD, METROPOLIS, CITY, TOWN, HIGHWAY, PAVED, COUNTRY_ROAD}

    public MapGenUrban(){
        this.allowedBiomeList = new ArrayList();
        this.disallowedBiomeTypeList = new ArrayList();
        this.minHeight = 256;
        this.maxHeight = -1;
        for (int l = 0; l < disallowedBiomeTypes.length; l++){
            this.disallowedBiomeTypeList.add(disallowedBiomeTypes[l]);
        }
        // Initialize biome list
        initializeBiomeListWithVanillaBiomes();
        for (int i = 0; i < this.allowedBiomeTypes.length; i++){
            BiomeGenBase[] biomes = BiomeDictionary.getBiomesForType(this.allowedBiomeTypes[i]);
            for (int j = 0; j < biomes.length; j++){
                if (!this.allowedBiomeList.contains(biomes[j])){
                    BiomeDictionary.Type[] biomeTypes = BiomeDictionary.getTypesForBiome(biomes[j]);
                    boolean flag = true;
                    for (int k = 0; k < biomeTypes.length; k++){
                        if (this.disallowedBiomeTypeList.contains(biomeTypes[k])){
                            flag = false;
                        }
                    }
                    if (flag){
                        this.allowedBiomeList.add(biomes[j]);
                    }

                }
            }
        }

        //Populate spawn list
        this.spawnList = new ArrayList();
        //this.spawnList.add(new SpawnListEntry(Entity<...>.class, weight, minGroup, maxGroup));

        this.minDistanceBetween = 10; //32
        this.maxDistanceBetween = 20;  //75
        this.minGenRadius = 10;
        this.maxGenRadius = 20;
        this.isRuin = false;
        this.xGen = 0;
        this.zGen = 0;

    }

    private void initializeBiomeListWithVanillaBiomes() {

        BiomeGenBase[] vanillaBiomes = BiomeGenBase.getBiomeGenArray();

        for (int j = 0; j < vanillaBiomes.length; j++){
            if (vanillaBiomes[j] == null) {break;}
            BiomeDictionary.Type[] biomeTypes = BiomeDictionary.getTypesForBiome(vanillaBiomes[j]);
            boolean pass = true;
            for (int k = 0; k < biomeTypes.length; k++){
                if (this.disallowedBiomeTypeList.contains(biomeTypes[k])){
                    pass = false;
                }
            }
            if (pass){
                this.allowedBiomeList.add(vanillaBiomes[j]);
            }
        }

    }

    public MapGenUrban(Map map){
        this();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry)iterator.next();

            if (((String)entry.getKey()).equals("distance"))
            {
                this.maxDistanceBetween = MathHelper.parseIntWithDefaultAndMax((String) entry.getValue(), this.maxDistanceBetween, this.minDistanceBetween + 1);
            }
            if (((String)entry.getKey()).equals("size")){
                this.terrainType = MathHelper.parseIntWithDefaultAndMax((String) entry.getValue(), this.terrainType, 0);
            }
            if (((String)entry.getKey()).equals("radius")){
                this.maxGenRadius = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.maxGenRadius, this.minGenRadius);
            }
        }
    }

    protected boolean isBiomeValid(int posX, int posZ, int radius) {
        return this.worldObj.getWorldChunkManager().areBiomesViable(posX, posZ, radius, allowedBiomeList);
    }

    @Override
    //TODO:  Rename function
    public String func_143025_a() {
        return "Metropolis";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        int k = chunkX;
        int l = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= this.maxDistanceBetween - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= this.maxDistanceBetween - 1;
        }

        int i1 = chunkX / this.maxDistanceBetween;
        int j1 = chunkZ / this.maxDistanceBetween;
        int i2 = chunkX / this.maxGenRadius;
        int j2 = chunkZ / this.maxGenRadius;
        Random random = this.worldObj.setRandomSeed(i1, j1, RAND_SEED);
        i1 *= this.maxDistanceBetween;
        j1 *= this.maxDistanceBetween;
        i1 += random.nextInt(this.maxDistanceBetween - this.minDistanceBetween);
        j1 += random.nextInt(this.maxDistanceBetween - this.minDistanceBetween);
        //random = this.worldObj.setRandomSeed(i2, j2, RAND_SEED);
        i2 = random.nextInt(this.maxGenRadius - this.minGenRadius) + this.minGenRadius;
        j2 = random.nextInt(this.maxGenRadius - this.minGenRadius) + this. minGenRadius;
        //DebugMessenger messenger = new DebugMessenger();
        //messenger.messageOnce("canSpawnAtCoords initial if test --> if (k[" + k + "] == i1["+ i1 + "] && l[" + l + "] == j1[" + j1 + "])");
        if (k == i1 && l == j1){
            //DebugMessenger.message("Distance check passed for [" + k + ", " + l + "]");
            if (isBiomeValid(k * 16 + 8, l * 16 + 8, 32)){
                //DebugMessenger.message("Biome check passed for [" + k + ", " + l + "]");
                int minY = getLowestTerrainValue(k, l);
                int heightDif = 0;
                for (int i = k - i2; i <= k + i2; i ++){
                    for (int j = l - j2; j <= l + j2; j++){
                        if (i == k && j == l ){continue;}
                        int difY = minY - getLowestTerrainValue(i, j);
                        if (difY > 0){
                            minY = minY - difY;
                            heightDif += difY;
                        }
                        else {
                            heightDif = (Math.abs(difY) > heightDif) ? Math.abs(difY) : heightDif;
                        }
                    }
                }
                if (heightDif <= 50){
                    //DebugMessenger.message("Heightfield check passed for [" + k + ", " + l + "]");
                    //if (random.nextDouble() < genChance){
                    //    DebugMessenger.message("Successful generation check for urban area at [" + k + ", " + l + "]");
                    //    return true;
                    //}
                    this.xGen = i2;
                    this.zGen = j2;
                    return true;
                }

            }
        }
        return false;
    }

    private int getLowestTerrainValue(int chunkX, int chunkZ) {
        Chunk chunk = this.worldObj.getChunkFromChunkCoords(chunkX, chunkZ);
        int height = chunk.heightMapMinimum;
        if (height == 0) {}
        return height;
        /*int minY = 256;
        int maxY = -1;
        int avgY = 0;
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 16; j++){
                int posY = this.worldObj.getTopSolidOrLiquidBlock(chunkX * 16 + i, chunkZ * 16 + j) - 1;
                avgY += posY;
                minY = (posY < minY) ? posY : minY;
                maxY = (posY > maxY) ? posY : maxY;
            }
        }
        avgY = avgY / 256;
        this.minHeight = minY;
        this.maxHeight = maxY;
        this.avgHeight = avgY;
        if (minY == 256 || maxY == -1) {DebugMessenger.message("ERROR getting height value");}
        return minY;*/
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new MapGenUrban.Start(this.worldObj, this.rand, chunkX, chunkZ, this.avgHeight, this.minHeight, this.maxHeight,
                this.xGen, this.zGen, this.isRuin, this.terrainType);
    }

    public static class Start extends StructureStart {

        private static final String __OBFID = "CL_A0000002";
        private boolean ruinedCity;
        private int baseHeight;
        private int minY;
        private int maxY;
        private int genRadiusX;
        private int genRadiusZ;

        public Start() {}

        public Start(World world, Random random, int chunkX, int chunkZ, int avgY, int bottomY, int topY,
                     int xGen, int zGen, boolean isRuins, int terrainType) {

            super(chunkX, chunkZ);
            this.ruinedCity = isRuins;
            this.baseHeight = avgY;
            this.maxY = topY;
            this.minY = bottomY;
            this.genRadiusX = xGen;
            this.genRadiusZ = zGen;

            List list = ComponentUrbanPieces.getComponentUrbanPiecesWeight(random, isRuins);
            ComponentUrbanPieces.Start start = new ComponentUrbanPieces.Start(world.getWorldChunkManager(), avgY, 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2,
                    xGen, zGen, list, terrainType);
            this.components.add(start);
            start.buildComponent(start, this.components, random);
            List buildings = start.buildingList;
            List roads = start.roadList;

            int l;

            while (!buildings.isEmpty() || !roads.isEmpty())
            {
                StructureComponent structurecomponent;

                if (buildings.isEmpty())
                {
                    l = random.nextInt(roads.size());
                    structurecomponent = (StructureComponent)roads.remove(l);
                    structurecomponent.buildComponent(start, this.components, random);
                }
                else
                {
                    l = random.nextInt(buildings.size());
                    structurecomponent = (StructureComponent)buildings.remove(l);
                    structurecomponent.buildComponent(start, this.components, random);
                }
            }

            this.updateBoundingBox();
        }

    }
}