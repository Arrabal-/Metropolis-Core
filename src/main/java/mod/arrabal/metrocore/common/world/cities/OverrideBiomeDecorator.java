package mod.arrabal.metrocore.common.world.cities;

import mod.arrabal.metrocore.common.handlers.world.WorldGenerationHandler;
import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.GeneratorBushFeature;
import net.minecraft.world.gen.feature.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Evan on 4/8/2015.
 */
public class OverrideBiomeDecorator extends BiomeDecorator {

    public OverrideBiomeDecorator(){
        this.sandGen = new WorldGenSand(Blocks.sand, 7);
        this.gravelAsSandGen = new WorldGenSand(Blocks.gravel, 6);
        this.yellowFlowerGen = new WorldGenFlowers(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION);
        this.mushroomBrownGen = new GeneratorBushFeature(Blocks.brown_mushroom);
        this.mushroomRedGen = new GeneratorBushFeature(Blocks.red_mushroom);
        this.bigMushroomGen = new WorldGenBigMushroom();
        this.reedGen = new WorldGenReed();
        this.cactusGen = new WorldGenCactus();
        this.waterlilyGen = new WorldGenWaterlily();
        this.flowersPerChunk = 2;
        this.grassPerChunk = 1;
        this.sandPerChunk = 1;
        this.sandPerChunk2 = 3;
        this.clayPerChunk = 1;
        this.generateLakes = true;
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
            if (!generatingInCityArea(worldIn, blockPos)) {
                super.decorate(worldIn, random, biomeGenBase, blockPos);
                return;
            }
            this.currentWorld = worldIn;
            this.sandGen = new EmptyWorldGen();
            this.gravelAsSandGen = new EmptyWorldGen();
            this.yellowFlowerGen = new EmptyFlowerGen(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION);
            this.mushroomBrownGen = new EmptyWorldGen();
            this.mushroomRedGen = new EmptyWorldGen();
            this.bigMushroomGen = new EmptyWorldGen();
            this.reedGen = new EmptyWorldGen();
            this.cactusGen = new EmptyWorldGen();
            this.waterlilyGen = new EmptyWorldGen();
            this.flowersPerChunk = 0;
            this.grassPerChunk = 1;
            this.sandPerChunk = 0;
            this.sandPerChunk2 = 0;
            this.clayPerChunk = 0;
            this.generateLakes = false;
            String s = worldIn.getWorldInfo().getGeneratorOptions();

            if (s != null)
            {
                this.chunkProviderSettings = ChunkProviderSettings.Factory.func_177865_a(s).func_177864_b();
            }
            else
            {
                this.chunkProviderSettings = ChunkProviderSettings.Factory.func_177865_a("").func_177864_b();
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
            this.genDecorations(biomeGenBase);
            this.currentWorld = null;
            this.randomGenerator = null;
        }
    }

    private boolean generatingInCityArea(World world, BlockPos blockPos){
        MetropolisGenerationContainer handler = WorldGenerationHandler.getGenContainerFromWorld(world);
        ConcurrentHashMap<String, MetropolisBoundingBox> mappedCities = handler.getUpdatedCityMap();
        Iterator iterator = mappedCities.entrySet().iterator();
        MetropolisBoundingBox boundingBox;
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            boundingBox = (MetropolisBoundingBox) entry.getValue();
            MetropolisBoundingBox clearTerrain = new MetropolisBoundingBox(boundingBox.minBlocKCoords.getX(), boundingBox.minBlocKCoords.getZ(), boundingBox.maxBlockCoords.getX(), boundingBox.maxBlockCoords.getZ());
            if (clearTerrain.isVecInside(blockPos)){
                return true;
            }
        }
        return false;
    }

    private class EmptyWorldGen extends WorldGenerator {

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
            return false;
        }
    }

    private class EmptyFlowerGen extends WorldGenFlowers{

        public EmptyFlowerGen(BlockFlower blockFlower, BlockFlower.EnumFlowerType flowerType){
            super(blockFlower, flowerType);
        }

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position){
            return false;
        }
    }
}
