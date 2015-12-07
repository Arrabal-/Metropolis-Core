package mod.arrabal.metrocore.common.world.gen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenRavine;

/**
 * Created by Evan on 4/14/2015.
 */
public class ModdedMapGenRavine extends MapGenRavine {

    /*@Override
    public void generate(IChunkProvider p_175792_1_, World worldIn, int p_175792_3_, int p_175792_4_, ChunkPrimer p_175792_5_)
    {
        int k = this.range;
        IChunkProvider chunkProvider = (ModdedChunkProviderSurface) p_175792_1_;
        if (((ModdedChunkProviderSurface) p_175792_1_).doCityGenerationCheck(new ChunkCoordIntPair(p_175792_3_, p_175792_4_), k)){
            // if intersects with existing city do not generate
            return;
        }
        this.worldObj = worldIn;
        this.rand.setSeed(worldIn.getSeed());
        long l = this.rand.nextLong();
        long i1 = this.rand.nextLong();

        for (int j1 = p_175792_3_ - k; j1 <= p_175792_3_ + k; ++j1)
        {
            for (int k1 = p_175792_4_ - k; k1 <= p_175792_4_ + k; ++k1)
            {
                long l1 = (long)j1 * l;
                long i2 = (long)k1 * i1;
                this.rand.setSeed(l1 ^ i2 ^ worldIn.getSeed());
                this.func_180701_a(worldIn, j1, k1, p_175792_3_, p_175792_4_, p_175792_5_);
            }
        }
    }*/

    //Exception biomes to make sure we generate like vanilla
    private boolean isExceptionBiome(net.minecraft.world.biome.BiomeGenBase biome)
    {
        if (biome == net.minecraft.world.biome.BiomeGenBase.beach) return true;
        if (biome == net.minecraft.world.biome.BiomeGenBase.desert) return true;
        if (biome == net.minecraft.world.biome.BiomeGenBase.mushroomIsland) return true;
        if (biome == net.minecraft.world.biome.BiomeGenBase.mushroomIslandShore) return true;
        return false;
    }

    @Override
    protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop)
    {
        net.minecraft.world.biome.BiomeGenBase biome = worldObj.getBiomeGenForCoords(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
        IBlockState state = data.getBlockState(x, y, z);
        IBlockState top = isExceptionBiome(biome) ? Blocks.grass.getDefaultState() : biome.topBlock;
        IBlockState filler = isExceptionBiome(biome) ? Blocks.dirt.getDefaultState() : biome.fillerBlock;

        int maxRavineY = 60;

        if (state.getBlock() == Blocks.stone || state.getBlock() == top.getBlock() || state.getBlock() == filler.getBlock())
        {
            if (y < 10)
            {
                data.setBlockState(x, y, z, Blocks.lava.getDefaultState());
            }
            else if (y < maxRavineY)
            {
                data.setBlockState(x, y, z, Blocks.air.getDefaultState());

                if (foundTop && data.getBlockState(x, y - 1, z).getBlock() != filler.getBlock())
                {
                    data.setBlockState(x, y - 1, z, filler.getBlock().getDefaultState());
                }
            }
        }
    }
}
