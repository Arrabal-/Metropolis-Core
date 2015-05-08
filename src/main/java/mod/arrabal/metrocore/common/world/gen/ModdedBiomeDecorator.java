package mod.arrabal.metrocore.common.world.gen;

import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.feature.*;

import java.util.Random;


/**
 * Created by Evan on 4/8/2015.
 */
public class ModdedBiomeDecorator extends BiomeDecorator {

    public MetropolisBoundingBox generatingCity;

    public ModdedBiomeDecorator(){
        super();
        generatingCity = null;
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

            boolean genInCityArea = false;
            if (this.generatingCity != null){
                genInCityArea = this.generatingCity.isVecInside(blockPos);
            }
            if (genInCityArea){
                this.bigMushroomsPerChunk = 0;
                this.cactiPerChunk = 0;
                this.deadBushPerChunk = 0;
                this.flowersPerChunk = 0;
                this.generateLakes = false;
                this.grassPerChunk = 0;
                this.mushroomsPerChunk = 0;
                this.reedsPerChunk = 0;
                this.treesPerChunk = 0;
                this.waterlilyPerChunk = 0;
            }
            this.genDecorations(biomeGenBase);
            this.currentWorld = null;
            this.randomGenerator = null;
        }
    }
}
