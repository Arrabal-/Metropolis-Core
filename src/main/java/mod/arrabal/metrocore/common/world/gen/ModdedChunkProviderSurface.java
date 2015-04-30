package mod.arrabal.metrocore.common.world.gen;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.*;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.*;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;

/**
 * Created by Evan on 4/21/2015.
 */
public class ModdedChunkProviderSurface extends ChunkProviderGenerate {

    /** RNG. */
    private Random rand;
    private NoiseGeneratorOctaves field_147431_j;
    private NoiseGeneratorOctaves field_147432_k;
    private NoiseGeneratorOctaves field_147429_l;
    private NoiseGeneratorPerlin field_147430_m;

    private World worldObj;
    /** are map structures going to be generated (e.g. strongholds) */
    private final boolean mapFeaturesEnabled;
    private WorldType field_177475_o;
    private final double[] field_147434_q;
    private final float[] parabolicField;
    private ChunkProviderSettings settings;
    private Block field_177476_s;
    private double[] stoneNoise;
    private MapGenBase caveGenerator;
    /** Holds Stronghold Generator */
    private MapGenStronghold strongholdGenerator;
    /** Holds Village Generator */
    private MapGenVillage villageGenerator;
    /** Holds Mineshaft Generator */
    private MapGenMineshaft mineshaftGenerator;
    private MapGenScatteredFeature scatteredFeatureGenerator;
    /** Holds ravine generator */
    private MapGenBase ravineGenerator;
    private StructureOceanMonument oceanMonumentGenerator;
    private MapGenMetropolis cityGenerator;
    /** The biomes that are used to generate the chunk */
    private BiomeGenBase[] biomesForGeneration;
    double[] field_147427_d;
    double[] field_147428_e;
    double[] field_147425_f;
    double[] field_147426_g;

    private boolean useCities;
    private boolean canGenCity;
    //public static MetropolisBoundingBox cityBoundsGenChecker;

    public ModdedChunkProviderSurface(World worldIn, long seed, boolean bMapFeatures, String generatorSettings) {

        super(worldIn, seed, bMapFeatures, generatorSettings);
        this.field_177476_s = Blocks.water;
        this.stoneNoise = new double[256];
        this.caveGenerator = new ModdedMapGenCaves();
        this.strongholdGenerator = new MapGenStronghold();
        this.villageGenerator = new ModdedMapGenVillage();
        this.mineshaftGenerator = new MapGenMineshaft();
        this.scatteredFeatureGenerator = new ModdedMapGenScatteredFeature();
        this.ravineGenerator = new ModdedMapGenRavine();
        this.oceanMonumentGenerator = new StructureOceanMonument();
        this.cityGenerator = new MapGenMetropolis();
        {
            caveGenerator = TerrainGen.getModdedMapGen(caveGenerator, CAVE);
            strongholdGenerator = (MapGenStronghold)TerrainGen.getModdedMapGen(strongholdGenerator, STRONGHOLD);
            villageGenerator = (MapGenVillage)TerrainGen.getModdedMapGen(villageGenerator, VILLAGE);
            mineshaftGenerator = (MapGenMineshaft)TerrainGen.getModdedMapGen(mineshaftGenerator, MINESHAFT);
            scatteredFeatureGenerator = (MapGenScatteredFeature)TerrainGen.getModdedMapGen(scatteredFeatureGenerator, SCATTERED_FEATURE);
            ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator, RAVINE);
            oceanMonumentGenerator = (StructureOceanMonument)TerrainGen.getModdedMapGen(oceanMonumentGenerator, OCEAN_MONUMENT);
            cityGenerator = (MapGenMetropolis) TerrainGen.getModdedMapGen(cityGenerator, InitMapGenEvent.EventType.CUSTOM);
        }
        this.worldObj = worldIn;
        this.mapFeaturesEnabled = bMapFeatures;
        this.field_177475_o = worldIn.getWorldInfo().getTerrainType();
        this.rand = new Random(seed);
        this.field_147431_j = new NoiseGeneratorOctaves(this.rand, 16);
        this.field_147432_k = new NoiseGeneratorOctaves(this.rand, 16);
        this.field_147429_l = new NoiseGeneratorOctaves(this.rand, 8);
        this.field_147430_m = new NoiseGeneratorPerlin(this.rand, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.field_147434_q = new double[825];
        this.parabolicField = new float[25];

        for (int j = -2; j <= 2; ++j)
        {
            for (int k = -2; k <= 2; ++k)
            {
                float f = 10.0F / MathHelper.sqrt_float((float) (j * j + k * k) + 0.2F);
                this.parabolicField[j + 2 + (k + 2) * 5] = f;
            }
        }

        if (generatorSettings != null)
        {
            this.settings = ChunkProviderSettings.Factory.func_177865_a(generatorSettings).func_177864_b();
            this.field_177476_s = this.settings.useLavaOceans ? Blocks.lava : Blocks.water;
        }
        this.useCities = ConfigHandler.enableCityCreation;
        this.canGenCity = false;

        NoiseGenerator[] noiseGens = {field_147431_j, field_147432_k, field_147429_l, field_147430_m, noiseGen5, noiseGen6, mobSpawnerNoise};
        noiseGens = TerrainGen.getModdedNoiseGenerators(worldIn, this.rand, noiseGens);
        this.field_147431_j = (NoiseGeneratorOctaves)noiseGens[0];
        this.field_147432_k = (NoiseGeneratorOctaves)noiseGens[1];
        this.field_147429_l = (NoiseGeneratorOctaves)noiseGens[2];
        this.field_147430_m = (NoiseGeneratorPerlin)noiseGens[3];
        this.noiseGen5 = (NoiseGeneratorOctaves)noiseGens[4];
        this.noiseGen6 = (NoiseGeneratorOctaves)noiseGens[5];
        this.mobSpawnerNoise = (NoiseGeneratorOctaves)noiseGens[6];
    }

    @Override
    public Chunk provideChunk(int x, int z)
    {
        this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.canGenCity = cityGenerator.canGenerateMetropolis(this.worldObj, this.rand, x, z);
        this.setBlocksInChunk(x, z, chunkprimer);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        this.replaceBlocksForBiome(x, z, chunkprimer, this.biomesForGeneration);

        if(this.useCities && this.canGenCity){
            this.cityGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useCaves)
        {
            this.caveGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useRavines)
        {
            this.ravineGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
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

        if (this.settings.useTemples && this.mapFeaturesEnabled)
        {
            this.scatteredFeatureGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useMonuments && this.mapFeaturesEnabled)
        {
            this.oceanMonumentGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
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
    public void populate(IChunkProvider chunkProvider, int x, int z)
    {
        BlockFalling.fallInstantly = true;
        int k = x * 16;
        int l = z * 16;
        BlockPos blockpos = new BlockPos(k, 0, l);
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.worldObj.getSeed());
        long i1 = this.rand.nextLong() / 2L * 2L + 1L;
        long j1 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)x * i1 + (long)z * j1 ^ this.worldObj.getSeed());
        boolean flag = false;
        boolean cityGenerated = false;
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(x, z);

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(chunkProvider, worldObj, rand, x, z, flag));

        if (this.useCities){
            cityGenerated = this.cityGenerator.buildMetropolis(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useMineShafts && this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.func_175794_a(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useVillages && this.mapFeaturesEnabled)
        {
            flag = this.villageGenerator.func_175794_a(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useStrongholds && this.mapFeaturesEnabled)
        {
            this.strongholdGenerator.func_175794_a(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useTemples && this.mapFeaturesEnabled)
        {
            this.scatteredFeatureGenerator.func_175794_a(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useMonuments && this.mapFeaturesEnabled)
        {
            this.oceanMonumentGenerator.func_175794_a(this.worldObj, this.rand, chunkcoordintpair);
        }

        flag = flag || cityGenerated;

        int k1;
        int l1;
        int i2;

        if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0
                && TerrainGen.populate(chunkProvider, worldObj, rand, x, z, flag, LAKE))
        {
            k1 = this.rand.nextInt(16) + 8;
            l1 = this.rand.nextInt(256);
            i2 = this.rand.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.rand, blockpos.add(k1, l1, i2));
        }

        if (TerrainGen.populate(chunkProvider, worldObj, rand, x, z, flag, LAVA) && !flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes)
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
            boolean doGen = TerrainGen.populate(chunkProvider, worldObj, rand, x, z, flag, DUNGEON);
            for (k1 = 0; doGen && k1 < this.settings.dungeonChance; ++k1)
            {
                l1 = this.rand.nextInt(16) + 8;
                i2 = this.rand.nextInt(256);
                int j2 = this.rand.nextInt(16) + 8;
                (new WorldGenDungeons()).generate(this.worldObj, this.rand, blockpos.add(l1, i2, j2));
            }
        }

        biomegenbase.decorate(this.worldObj, this.rand, new BlockPos(k, 0, l));
        if (TerrainGen.populate(chunkProvider, worldObj, rand, x, z, flag, ANIMALS))
        {
            SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
        }
        blockpos = blockpos.add(8, 0, 8);

        boolean doGen = TerrainGen.populate(chunkProvider, worldObj, rand, x, z, flag, ICE);
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

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(chunkProvider, worldObj, rand, x, z, flag));

        BlockFalling.fallInstantly = false;
    }

    private void func_147423_a(int p_147423_1_, int p_147423_2_, int p_147423_3_)
    {
        int genChunkX = p_147423_1_ / 4;
        int genChunkZ = p_147423_3_ / 4;
//        int defaultMaxGenRadius = this.cityGenerator.getDefaultGenRadius(true);
        float f, f1;
//        this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, (genChunkX - defaultMaxGenRadius) << 4, (genChunkZ - defaultMaxGenRadius) << 4,
//                defaultMaxGenRadius * 32, defaultMaxGenRadius * 32);
        // check for valid city generation area
        boolean dampenNoise = false;
        if (this.canGenCity && this.cityGenerator.isBiomeValid(this.worldObj, (genChunkX << 4) + 8, (genChunkZ << 4) + 8, (this.cityGenerator.getDefaultGenRadius(true) << 4) + 8)){
            // generate less noisy terrain
            //TODO:  figure out how to generate less noisy terrain
            dampenNoise = true;
        }
        else if (this.cityGenerator.checkGenerationConflict(new MetropolisBoundingBox(genChunkX << 4, genChunkZ << 4, (genChunkX << 4) + 15, (genChunkZ << 4) + 15))){
            dampenNoise = true;
            this.canGenCity = false;
        }
        if (dampenNoise){
            this.field_147426_g = this.noiseGen6.generateNoiseOctaves(this.field_147426_g, p_147423_1_, p_147423_3_, 5, 5, (double)this.settings.depthNoiseScaleX, (double)this.settings.depthNoiseScaleZ, (double)this.settings.depthNoiseScaleExponent);
            f = this.settings.coordinateScale;
            f1 = this.settings.heightScale;
            this.field_147427_d = this.field_147429_l.generateNoiseOctaves(this.field_147427_d, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, (double)(f / this.settings.mainNoiseScaleX), (double)(f1 / this.settings.mainNoiseScaleY), (double)(f / this.settings.mainNoiseScaleZ));
            this.field_147428_e = this.field_147431_j.generateNoiseOctaves(this.field_147428_e, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, (double)f, (double)f1, (double)f);
            this.field_147425_f = this.field_147432_k.generateNoiseOctaves(this.field_147425_f, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, (double)f, (double)f1, (double)f);
        }
        else {
            // use default noise generation
            this.canGenCity = false;
            this.field_147426_g = this.noiseGen6.generateNoiseOctaves(this.field_147426_g, p_147423_1_, p_147423_3_, 5, 5, (double)this.settings.depthNoiseScaleX, (double)this.settings.depthNoiseScaleZ, (double)this.settings.depthNoiseScaleExponent);
            f = this.settings.coordinateScale;
            f1 = this.settings.heightScale;
            this.field_147427_d = this.field_147429_l.generateNoiseOctaves(this.field_147427_d, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, (double)(f / this.settings.mainNoiseScaleX), (double)(f1 / this.settings.mainNoiseScaleY), (double)(f / this.settings.mainNoiseScaleZ));
            this.field_147428_e = this.field_147431_j.generateNoiseOctaves(this.field_147428_e, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, (double)f, (double)f1, (double)f);
            this.field_147425_f = this.field_147432_k.generateNoiseOctaves(this.field_147425_f, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, (double)f, (double)f1, (double)f);
        }
        boolean flag1 = false;
        boolean flag = false;
        int l = 0;
        int i1 = 0;

        for (int j1 = 0; j1 < 5; ++j1)
        {
            for (int k1 = 0; k1 < 5; ++k1)
            {
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                byte b0 = 2;
                BiomeGenBase biomegenbase = this.biomesForGeneration[j1 + 2 + (k1 + 2) * 10];

                for (int l1 = -b0; l1 <= b0; ++l1)
                {
                    for (int i2 = -b0; i2 <= b0; ++i2)
                    {
                        BiomeGenBase biomegenbase1 = this.biomesForGeneration[j1 + l1 + 2 + (k1 + i2 + 2) * 10];
                        float f5 = this.settings.biomeDepthOffSet + biomegenbase1.minHeight * this.settings.biomeDepthWeight;
                        float f6 = this.settings.biomeScaleOffset + biomegenbase1.maxHeight * this.settings.biomeScaleWeight;

                        if (this.field_177475_o == WorldType.AMPLIFIED && f5 > 0.0F)
                        {
                            f5 = 1.0F + f5 * 2.0F;
                            f6 = 1.0F + f6 * 4.0F;
                        }

                        float f7 = this.parabolicField[l1 + 2 + (i2 + 2) * 5] / (f5 + 2.0F);

                        if (biomegenbase1.minHeight > biomegenbase.minHeight)
                        {
                            f7 /= 2.0F;
                        }

                        f2 += f6 * f7;
                        f3 += f5 * f7;
                        f4 += f7;
                    }
                }

                f2 /= f4;
                f3 /= f4;
                f2 = f2 * 0.9F + 0.1F;
                f3 = (f3 * 4.0F - 1.0F) / 8.0F;
                double d7 = this.field_147426_g[i1] / 8000.0D;

                if (d7 < 0.0D)
                {
                    d7 = -d7 * 0.3D;
                }

                d7 = d7 * 3.0D - 2.0D;

                if (d7 < 0.0D)
                {
                    d7 /= 2.0D;

                    if (d7 < -1.0D)
                    {
                        d7 = -1.0D;
                    }

                    d7 /= 1.4D;
                    d7 /= 2.0D;
                }
                else
                {
                    if (d7 > 1.0D)
                    {
                        d7 = 1.0D;
                    }

                    d7 /= 8.0D;
                }

                ++i1;
                double d8 = (double)f3;
                double d9 = (double)f2;
                d8 += d7 * 0.2D;
                d8 = d8 * (double)this.settings.baseSize / 8.0D;
                double d0 = (double)this.settings.baseSize + d8 * 4.0D;

                for (int j2 = 0; j2 < 33; ++j2)
                {
                    double d1 = ((double)j2 - d0) * (double)this.settings.stretchY * 128.0D / 256.0D / d9;

                    if (d1 < 0.0D)
                    {
                        d1 *= 4.0D;
                    }

                    double d2 = this.field_147428_e[l] / (double)this.settings.lowerLimitScale;
                    double d3 = this.field_147425_f[l] / (double)this.settings.upperLimitScale;
                    double d4 = (this.field_147427_d[l] / 10.0D + 1.0D) / 2.0D;
                    double d5 = MathHelper.denormalizeClamp(d2, d3, d4) - d1;

                    if (j2 > 29)
                    {
                        double d6 = (double)((float)(j2 - 29) / 3.0F);
                        d5 = d5 * (1.0D - d6) + -10.0D * d6;
                    }

                    this.field_147434_q[l] = d5;
                    ++l;
                }
            }
        }
    }
}
