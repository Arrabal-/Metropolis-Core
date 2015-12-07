package mod.arrabal.metrocore.common.world.gen;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.init.Biomes;
import mod.arrabal.metrocore.common.library.LogHelper;
import net.minecraft.block.BlockFalling;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.*;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;

/**
 * Created by Arrabal on 7/15/2015.
 */
public class MetropolisChunkProviderGenerate extends ChunkProviderGenerate {

    /** are map structures going to be generated (e.g. strongholds) */
    private final boolean mapFeaturesEnabled;
    private MapGenBase caveGenerator2;
    private MapGenScatteredFeature scatteredFeatureGenerator2;
    private MapGenBase ravineGenerator2;
    private MapGenMetropolis cityGenerator;

    private List<BiomeGenBase> cityBiomes;
    private final boolean useCities;
    private boolean canGenCity;

    public MetropolisChunkProviderGenerate(World worldIn, long worldSeed, boolean bMapFeaturesEnabled, String customWorldType) {

        super(worldIn, worldSeed, bMapFeaturesEnabled, customWorldType);
        this.cityBiomes = new ArrayList<>();
        this.cityBiomes.add(Biomes.plainsMetro);
        this.caveGenerator2 = new ModdedMapGenCaves();
        this.ravineGenerator2 = new ModdedMapGenRavine();
        this.scatteredFeatureGenerator2 = new ModdedMapGenScatteredFeature();
        this.cityGenerator = new MapGenMetropolis();
        this.mapFeaturesEnabled = bMapFeaturesEnabled;

        /*if (customWorldType != null)
        {
            this.settings = ChunkProviderSettings.Factory.func_177865_a(customWorldType).func_177864_b();
            this.field_177476_s = this.settings.useLavaOceans ? Blocks.lava : Blocks.water;
        }*/

        this.useCities = ConfigHandler.enableCityCreation;
        this.canGenCity = false;
    }

    public MapGenMetropolis getCityGenerator(){
        return this.cityGenerator;
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
                break;
            }


        if (this.settings.useCaves)
        {
            if (inCityBiome) this.caveGenerator2.generate(this, this.worldObj, x, z, chunkprimer);
            else this.caveGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useRavines)
        {
            if (inCityBiome) this.ravineGenerator2.generate(this, this.worldObj, x, z, chunkprimer);
            else this.ravineGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useMineShafts && this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useVillages && this.mapFeaturesEnabled)
        {
            this.villageGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useStrongholds && this.mapFeaturesEnabled)
        {
            this.strongholdGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useTemples && this.mapFeaturesEnabled && !inCityBiome)
        {
            this.scatteredFeatureGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useMonuments && this.mapFeaturesEnabled)
        {
            this.oceanMonumentGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.useCities){
            LogHelper.trace("CALL IN CHUNK PROVIDER provideChunk to INSTANTIATE METROPOLIS START");
            this.cityGenerator.generate(this, this.worldObj, x, z, chunkprimer);
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
    public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ)
    {
        BlockFalling.fallInstantly = true;
        int k = chunkX * 16;
        int l = chunkZ * 16;
        BlockPos blockpos = new BlockPos(k, 0, l);
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.worldObj.getSeed());
        long i1 = this.rand.nextLong() / 2L * 2L + 1L;
        long j1 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long) chunkX * i1 + (long) chunkZ * j1 ^ this.worldObj.getSeed());
        boolean flag = false;
        boolean cityGenerated = false;
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(chunkX, chunkZ);

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(chunkProvider, worldObj, rand, chunkX, chunkZ, flag));

        if (this.settings.useMineShafts && this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useVillages && this.mapFeaturesEnabled)
        {
            flag = this.villageGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useStrongholds && this.mapFeaturesEnabled)
        {
            this.strongholdGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useTemples && this.mapFeaturesEnabled)
        {
            this.scatteredFeatureGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useMonuments && this.mapFeaturesEnabled)
        {
            this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.useCities) {
            cityGenerated = this.cityGenerator.buildMetropolis(this.worldObj, this.rand, chunkcoordintpair);
        }

        int k1;
        int l1;
        int i2;

        if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0
                && TerrainGen.populate(chunkProvider, worldObj, rand, chunkX, chunkZ, flag, LAKE))
        {
            k1 = this.rand.nextInt(16) + 8;
            l1 = this.rand.nextInt(256);
            i2 = this.rand.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.rand, blockpos.add(k1, l1, i2));
        }

        if (TerrainGen.populate(chunkProvider, worldObj, rand, chunkX, chunkZ, flag, LAVA) && !flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes)
        {
            k1 = this.rand.nextInt(16) + 8;
            l1 = this.rand.nextInt(this.rand.nextInt(248) + 8);
            i2 = this.rand.nextInt(16) + 8;

            if (l1 < 63 || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0)
            {
                (new WorldGenLakes(Blocks.lava)).generate(this.worldObj, this.rand, blockpos.add(k1, l1, i2));
            }
        }

        if (this.settings.useDungeons)
        {
            boolean doGen = TerrainGen.populate(chunkProvider, worldObj, rand, chunkX, chunkZ, flag, DUNGEON);
            for (k1 = 0; doGen && k1 < this.settings.dungeonChance; ++k1)
            {
                l1 = this.rand.nextInt(16) + 8;
                i2 = this.rand.nextInt(256);
                int j2 = this.rand.nextInt(16) + 8;
                (new WorldGenDungeons()).generate(this.worldObj, this.rand, blockpos.add(l1, i2, j2));
            }
        }

        biomegenbase.decorate(this.worldObj, this.rand, new BlockPos(k, 0, l));
        if (TerrainGen.populate(chunkProvider, worldObj, rand, chunkX, chunkZ, flag, ANIMALS))
        {
            SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
        }
        blockpos = blockpos.add(8, 0, 8);

        boolean doGen = TerrainGen.populate(chunkProvider, worldObj, rand, chunkX, chunkZ, flag, ICE);
        for (k1 = 0; doGen && k1 < 16; ++k1)
        {
            for (l1 = 0; l1 < 16; ++l1)
            {
                BlockPos blockpos1 = this.worldObj.getPrecipitationHeight(blockpos.add(k1, 0, l1));
                BlockPos blockpos2 = blockpos1.down();

                if (this.worldObj.canBlockFreezeWater(blockpos2))
                {
                    this.worldObj.setBlockState(blockpos2, Blocks.ice.getDefaultState(), 2);
                }

                if (this.worldObj.canSnowAt(blockpos1, true))
                {
                    this.worldObj.setBlockState(blockpos1, Blocks.snow_layer.getDefaultState(), 2);
                }
            }
        }

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(chunkProvider, worldObj, rand, chunkX, chunkZ, flag));

        if (this.cityGenerator.generatingZone != null) this.cityGenerator.generatingZone = null;
        BlockFalling.fallInstantly = false;
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
            this.mineshaftGenerator.generate(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer) null);
        }

        if (this.settings.useVillages && this.mapFeaturesEnabled)
        {
            this.villageGenerator.generate(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer) null);
        }

        if (this.settings.useStrongholds && this.mapFeaturesEnabled)
        {
            this.strongholdGenerator.generate(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer) null);
        }

        if (this.settings.useTemples && this.mapFeaturesEnabled)
        {
            if (inCityBiome) this.scatteredFeatureGenerator2.generate(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer) null);
            else this.scatteredFeatureGenerator.generate(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer) null);
        }

        if (this.settings.useMonuments && this.mapFeaturesEnabled)
        {
            this.oceanMonumentGenerator.generate(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer)null);
        }

        if (this.useCities && validForCity){
            LogHelper.debug("CALL IN CHUNK PROVIDER recreateStructures to INSTANTIATE METROPOLIS START");
            this.cityGenerator.generate(this, this.worldObj, chunkX, chunkZ, (ChunkPrimer) null);
        }
    }

}
