package mod.arrabal.metrocore.common.world.biome;

import mod.arrabal.metrocore.common.handlers.world.TerrainGenEventHandler;
import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import mod.arrabal.metrocore.common.world.cities.MetropolisStart;
import mod.arrabal.metrocore.common.world.gen.MapGenMetropolis;
import mod.arrabal.metrocore.common.world.gen.MetropolisChunkProviderGenerate;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.*;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_LAVA;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_WATER;

/**
 * Created by Arrabal on 7/14/2015.
 */
public class MetropolisBiomeDecorator extends BiomeDecorator{

    private boolean inCityArea;

    public MetropolisBiomeDecorator(){
        super();
        this.inCityArea = false;
    }

    public void setInCityArea(boolean inCity){
        this.inCityArea = inCity;
    }

    @Override
    public void decorate(World worldIn, Random random, BiomeGenBase biomeGenBase, BlockPos blockPos)
    {
        if (this.currentWorld != null)
        {
            throw new RuntimeException("Already decorating");
        }
        else
        {
            this.currentWorld = worldIn;
            String s = worldIn.getWorldInfo().getGeneratorOptions();

            if (s != null)
            {
                this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory(s).func_177864_b();
            }
            else
            {
                this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory("").func_177864_b();
            }

            this.randomGenerator = random;
            this.field_180294_c = blockPos;
            this.dirtGen = new WorldGenMinable(Blocks.dirt.getDefaultState(), this.chunkProviderSettings.dirtSize);
            this.gravelGen = new WorldGenMinable(Blocks.gravel.getDefaultState(), this.chunkProviderSettings.gravelSize);
            this.graniteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
            this.dioriteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
            this.andesiteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
            this.coalGen = new WorldGenMinable(Blocks.coal_ore.getDefaultState(), this.chunkProviderSettings.coalSize);
            this.ironGen = new WorldGenMinable(Blocks.iron_ore.getDefaultState(), this.chunkProviderSettings.ironSize);
            this.goldGen = new WorldGenMinable(Blocks.gold_ore.getDefaultState(), this.chunkProviderSettings.goldSize);
            this.redstoneGen = new WorldGenMinable(Blocks.redstone_ore.getDefaultState(), this.chunkProviderSettings.redstoneSize);
            this.diamondGen = new WorldGenMinable(Blocks.diamond_ore.getDefaultState(), this.chunkProviderSettings.diamondSize);
            this.lapisGen = new WorldGenMinable(Blocks.lapis_ore.getDefaultState(), this.chunkProviderSettings.lapisSize);

            if (this.inCityArea){
                this.bigMushroomsPerChunk = 0;
                this.cactiPerChunk = 0;
                this.deadBushPerChunk = 0;
                this.flowersPerChunk = 0;
                this.generateLakes = false;
                this.grassPerChunk = 0;
                this.mushroomsPerChunk = 0;
                this.reedsPerChunk = 0;
                this.treesPerChunk = -999;
                this.waterlilyPerChunk = 0;
            }
            this.genDecorations(biomeGenBase);
            this.currentWorld = null;
            this.randomGenerator = null;
            this.inCityArea = false;
        }
    }

    @Override
    protected void genDecorations(BiomeGenBase p_150513_1_)
    {
        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(currentWorld, randomGenerator, field_180294_c));
        this.generateOres();
        int i;
        int j;
        int k;

        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, SAND);
        for (i = 0; doGen && i < this.sandPerChunk2; ++i)
        {
            j = this.randomGenerator.nextInt(16) + 8;
            k = this.randomGenerator.nextInt(16) + 8;
            this.sandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(j, 0, k)));
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, CLAY);
        for (i = 0; doGen && i < this.clayPerChunk; ++i)
        {
            j = this.randomGenerator.nextInt(16) + 8;
            k = this.randomGenerator.nextInt(16) + 8;
            this.clayGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(j, 0, k)));
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, SAND_PASS2);
        for (i = 0; doGen && i < this.sandPerChunk; ++i)
        {
            j = this.randomGenerator.nextInt(16) + 8;
            k = this.randomGenerator.nextInt(16) + 8;
            this.gravelAsSandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(j, 0, k)));
        }

        i = this.treesPerChunk;

        if (this.randomGenerator.nextInt(10) == 0)
        {
            ++i;
        }

        int l;
        BlockPos blockpos;

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, TREE);
        for (j = 0; doGen && j < i; ++j)
        {
            k = this.randomGenerator.nextInt(16) + 8;
            l = this.randomGenerator.nextInt(16) + 8;
            WorldGenAbstractTree worldgenabstracttree = p_150513_1_.genBigTreeChance(this.randomGenerator);
            worldgenabstracttree.func_175904_e();
            blockpos = this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l));

            if (worldgenabstracttree.generate(this.currentWorld, this.randomGenerator, blockpos))
            {
                worldgenabstracttree.func_180711_a(this.currentWorld, this.randomGenerator, blockpos);
            }
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, BIG_SHROOM);
        for (j = 0; doGen && j < this.bigMushroomsPerChunk; ++j)
        {
            k = this.randomGenerator.nextInt(16) + 8;
            l = this.randomGenerator.nextInt(16) + 8;
            this.bigMushroomGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l)));
        }

        int i1;

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, FLOWERS);
        for (j = 0; doGen && j < this.flowersPerChunk; ++j)
        {
            k = this.randomGenerator.nextInt(16) + 8;
            l = this.randomGenerator.nextInt(16) + 8;
            i1 = nextInt(this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l)).getY() + 32);
            blockpos = this.field_180294_c.add(k, i1, l);
            BlockFlower.EnumFlowerType enumflowertype = p_150513_1_.pickRandomFlower(this.randomGenerator, blockpos);
            BlockFlower blockflower = enumflowertype.getBlockType().getBlock();

            if (blockflower.getMaterial() != Material.air)
            {
                this.yellowFlowerGen.setGeneratedBlock(blockflower, enumflowertype);
                this.yellowFlowerGen.generate(this.currentWorld, this.randomGenerator, blockpos);
            }
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, GRASS);
        for (j = 0; doGen && j < this.grassPerChunk; ++j)
        {
            k = this.randomGenerator.nextInt(16) + 8;
            l = this.randomGenerator.nextInt(16) + 8;
            i1 = nextInt(this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l)).getY() * 2);
            p_150513_1_.getRandomWorldGenForGrass(this.randomGenerator).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(k, i1, l));
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, DEAD_BUSH);
        for (j = 0; doGen && j < this.deadBushPerChunk; ++j)
        {
            k = this.randomGenerator.nextInt(16) + 8;
            l = this.randomGenerator.nextInt(16) + 8;
            i1 = nextInt(this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l)).getY() * 2);
            (new WorldGenDeadBush()).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(k, i1, l));
        }

        j = 0;

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, LILYPAD);
        while (doGen && j < this.waterlilyPerChunk)
        {
            k = this.randomGenerator.nextInt(16) + 8;
            l = this.randomGenerator.nextInt(16) + 8;
            i1 = nextInt(this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l)).getY() * 2);
            blockpos = this.field_180294_c.add(k, i1, l);

            while (true)
            {
                if (blockpos.getY() > 0)
                {
                    BlockPos blockpos3 = blockpos.down();

                    if (this.currentWorld.isAirBlock(blockpos3))
                    {
                        blockpos = blockpos3;
                        continue;
                    }
                }

                this.waterlilyGen.generate(this.currentWorld, this.randomGenerator, blockpos);
                ++j;
                break;
            }
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, SHROOM);
        for (j = 0; doGen && j < this.mushroomsPerChunk; ++j)
        {
            if (this.randomGenerator.nextInt(4) == 0)
            {
                k = this.randomGenerator.nextInt(16) + 8;
                l = this.randomGenerator.nextInt(16) + 8;
                BlockPos blockpos2 = this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l));
                this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, blockpos2);
            }

            if (this.randomGenerator.nextInt(8) == 0)
            {
                k = this.randomGenerator.nextInt(16) + 8;
                l = this.randomGenerator.nextInt(16) + 8;
                i1 = nextInt(this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l)).getY() * 2);
                blockpos = this.field_180294_c.add(k, i1, l);
                this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, blockpos);
            }
        }

        if (doGen && this.randomGenerator.nextInt(4) == 0 && !this.inCityArea)
        {
            j = this.randomGenerator.nextInt(16) + 8;
            k = this.randomGenerator.nextInt(16) + 8;
            l = nextInt(this.currentWorld.getHeight(this.field_180294_c.add(j, 0, k)).getY() * 2);
            this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j, l, k));
        }

        if (doGen && this.randomGenerator.nextInt(8) == 0 && !this.inCityArea)
        {
            j = this.randomGenerator.nextInt(16) + 8;
            k = this.randomGenerator.nextInt(16) + 8;
            l = nextInt(this.currentWorld.getHeight(this.field_180294_c.add(j, 0, k)).getY() * 2);
            this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j, l, k));
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, REED);
        for (j = 0; doGen && j < this.reedsPerChunk; ++j)
        {
            k = this.randomGenerator.nextInt(16) + 8;
            l = this.randomGenerator.nextInt(16) + 8;
            i1 = nextInt(this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l)).getY() * 2);
            this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(k, i1, l));
        }

        if (!this.inCityArea) {
            for (j = 0; doGen && j < 10; ++j) {
                k = this.randomGenerator.nextInt(16) + 8;
                l = this.randomGenerator.nextInt(16) + 8;
                i1 = nextInt(this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l)).getY() * 2);
                this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(k, i1, l));
            }
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, PUMPKIN);
        if (doGen && this.randomGenerator.nextInt(32) == 0 && !this.inCityArea)
        {
            j = this.randomGenerator.nextInt(16) + 8;
            k = this.randomGenerator.nextInt(16) + 8;
            l = nextInt(this.currentWorld.getHeight(this.field_180294_c.add(j, 0, k)).getY() * 2);
            (new WorldGenPumpkin()).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j, l, k));
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, CACTUS);
        for (j = 0; doGen && j < this.cactiPerChunk; ++j)
        {
            k = this.randomGenerator.nextInt(16) + 8;
            l = this.randomGenerator.nextInt(16) + 8;
            i1 = nextInt(this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l)).getY() * 2);
            this.cactusGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(k, i1, l));
        }

        if (this.generateLakes)
        {
            BlockPos blockpos1;

            doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, LAKE_WATER);
            for (j = 0; doGen && j < 50; ++j)
            {
                blockpos1 = this.field_180294_c.add(this.randomGenerator.nextInt(16) + 8, this.randomGenerator.nextInt(this.randomGenerator.nextInt(248) + 8), this.randomGenerator.nextInt(16) + 8);
                (new WorldGenLiquids(Blocks.flowing_water)).generate(this.currentWorld, this.randomGenerator, blockpos1);
            }

            doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, LAKE_LAVA);
            for (j = 0; doGen && j < 20; ++j)
            {
                blockpos1 = this.field_180294_c.add(this.randomGenerator.nextInt(16) + 8, this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8), this.randomGenerator.nextInt(16) + 8);
                (new WorldGenLiquids(Blocks.flowing_lava)).generate(this.currentWorld, this.randomGenerator, blockpos1);
            }
        }

        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(currentWorld, randomGenerator, field_180294_c));
    }

    private int nextInt(int i) { //Safety wrapper to prevent exceptions.
        if (i <= 1)
            return 0;
        return this.randomGenerator.nextInt(i);
    }
}
