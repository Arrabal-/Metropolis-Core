package mod.arrabal.metrocore.common.world.gen;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.init.Biomes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.structure.*;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.*;

/**
 * Created by Arrabal on 7/15/2015.
 */
public class MetropolisChunkProviderGenerate extends ChunkProviderGenerate {

    /** RNG. */
    //private Random rand;
    //private World worldObj;
    /** are map structures going to be generated (e.g. strongholds) */
    private final boolean mapFeaturesEnabled;
    //private ChunkProviderSettings settings;
    //private Block field_177476_s;
    //private MapGenBase caveGenerator;
    private MapGenBase caveGenerator2;
    /** Holds Stronghold Generator */
    //private MapGenStronghold strongholdGenerator;
    /** Holds Village Generator */
    //private MapGenVillage villageGenerator;
    /** Holds Mineshaft Generator */
    //private MapGenMineshaft mineshaftGenerator;
    //private MapGenScatteredFeature scatteredFeatureGenerator;
    private MapGenScatteredFeature scatteredFeatureGenerator2;
    /** Holds ravine generator */
    //private MapGenBase ravineGenerator;
    private MapGenBase ravineGenerator2;
    //private StructureOceanMonument oceanMonumentGenerator;
    private MapGenMetropolis cityGenerator;
    /** The biomes that are used to generate the chunk */
    //private BiomeGenBase[] biomesForGeneration;
    private List<BiomeGenBase> cityBiomes;
    private final boolean useCities;
    private boolean canGenCity;

    public MetropolisChunkProviderGenerate(World worldIn, long worldSeed, boolean bMapFeaturesEnabled, String customWorldType) {

        super(worldIn, worldSeed, bMapFeaturesEnabled, customWorldType);
        this.cityBiomes = new ArrayList<>();
        this.cityBiomes.add(Biomes.plainsMetro);
        //this.caveGenerator = new MapGenCaves();
        //this.strongholdGenerator = new MapGenStronghold();
        //this.villageGenerator = new MapGenVillage();
        //this.mineshaftGenerator = new MapGenMineshaft();
        //this.scatteredFeatureGenerator = new MapGenScatteredFeature();
        //this.ravineGenerator = new MapGenRavine();
        //this.oceanMonumentGenerator = new StructureOceanMonument();
        this.caveGenerator2 = new ModdedMapGenCaves();
        this.ravineGenerator2 = new ModdedMapGenRavine();
        this.scatteredFeatureGenerator2 = new ModdedMapGenScatteredFeature();
        this.cityGenerator = new MapGenMetropolis();
        /*{
            caveGenerator = TerrainGen.getModdedMapGen(caveGenerator, CAVE);
            strongholdGenerator = (MapGenStronghold)TerrainGen.getModdedMapGen(strongholdGenerator, STRONGHOLD);
            villageGenerator = (MapGenVillage)TerrainGen.getModdedMapGen(villageGenerator, VILLAGE);
            mineshaftGenerator = (MapGenMineshaft)TerrainGen.getModdedMapGen(mineshaftGenerator, MINESHAFT);
            scatteredFeatureGenerator = (MapGenScatteredFeature)TerrainGen.getModdedMapGen(scatteredFeatureGenerator, SCATTERED_FEATURE);
            ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator, RAVINE);
            oceanMonumentGenerator = (StructureOceanMonument)TerrainGen.getModdedMapGen(oceanMonumentGenerator, OCEAN_MONUMENT);
        }*/
        //this.worldObj = worldIn;
        this.mapFeaturesEnabled = bMapFeaturesEnabled;
        //this.rand = new Random(worldSeed);

        /*if (customWorldType != null)
        {
            this.settings = ChunkProviderSettings.Factory.func_177865_a(customWorldType).func_177864_b();
            this.field_177476_s = this.settings.useLavaOceans ? Blocks.lava : Blocks.water;
        }*/

        this.useCities = ConfigHandler.enableCityCreation;
        this.canGenCity = false;
    }

    @Override
    public Chunk provideChunk(int x, int z)
    {
        this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.setBlocksInChunk(x, z, chunkprimer);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        this.replaceBlocksForBiome(x, z, chunkprimer, this.biomesForGeneration);
        boolean inCityBiome = false;
        for (int i = 0; i < this.biomesForGeneration.length; i++)
            if (this.cityBiomes.contains(this.biomesForGeneration[i])){
                inCityBiome = true;
                this.canGenCity = cityGenerator.canGenerateMetropolis(this.worldObj, this.rand, x, z);
                break;
            } else this.canGenCity = false;


        if (this.settings.useCaves)
        {
            if (inCityBiome) this.caveGenerator2.func_175792_a(this, this.worldObj, x, z, chunkprimer);
            else this.caveGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useRavines)
        {
            if (inCityBiome) this.ravineGenerator2.func_175792_a(this, this.worldObj, x, z, chunkprimer);
            else this.ravineGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useMineShafts && this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useVillages && this.mapFeaturesEnabled)
        {
            this.villageGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useStrongholds && this.mapFeaturesEnabled)
        {
            this.strongholdGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useTemples && this.mapFeaturesEnabled && !inCityBiome)
        {
            this.scatteredFeatureGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useMonuments && this.mapFeaturesEnabled)
        {
            this.oceanMonumentGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.useCities && this.canGenCity){
            this.cityGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
        }

        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int k = 0; k < abyte.length; ++k)
        {
            abyte[k] = (byte)this.biomesForGeneration[k].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void recreateStructures(Chunk chunk, int chunkX, int chunkZ)
    {
        BiomeGenBase[] biomesforgeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
        boolean inCityBiome = false;
        boolean validForCity = false;
        for (int i = 0; i < biomesforgeneration.length; i++)
            if (this.cityBiomes.contains(biomesforgeneration[i])){
                inCityBiome = true;
                validForCity = cityGenerator.canGenerateMetropolis(this.worldObj, this.rand, chunkX, chunkZ);
                break;
            } else validForCity = false;

        if (this.settings.useMineShafts && this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.func_175792_a(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer)null);
        }

        if (this.settings.useVillages && this.mapFeaturesEnabled)
        {
            this.villageGenerator.func_175792_a(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer)null);
        }

        if (this.settings.useStrongholds && this.mapFeaturesEnabled)
        {
            this.strongholdGenerator.func_175792_a(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer)null);
        }

        if (this.settings.useTemples && this.mapFeaturesEnabled)
        {
            if (inCityBiome) this.scatteredFeatureGenerator2.func_175792_a(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer)null);
            else this.scatteredFeatureGenerator.func_175792_a(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer)null);
        }

        if (this.settings.useMonuments && this.mapFeaturesEnabled)
        {
            this.oceanMonumentGenerator.func_175792_a(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer)null);
        }

        if (this.useCities && validForCity){
            this.cityGenerator.func_175792_a(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer)null);
        }
    }
}
