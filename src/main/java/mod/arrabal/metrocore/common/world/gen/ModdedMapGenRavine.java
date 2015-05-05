package mod.arrabal.metrocore.common.world.gen;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenRavine;

/**
 * Created by Evan on 4/14/2015.
 */
public class ModdedMapGenRavine extends MapGenRavine {

    @Override
    public void func_175792_a(IChunkProvider p_175792_1_, World worldIn, int p_175792_3_, int p_175792_4_, ChunkPrimer p_175792_5_)
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
    }
}
