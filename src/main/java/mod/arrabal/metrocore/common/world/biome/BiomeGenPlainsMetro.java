package mod.arrabal.metrocore.common.world.biome;

import mod.arrabal.metrocore.common.block.BlockCement;
import mod.arrabal.metrocore.common.init.Biomes;
import mod.arrabal.metrocore.common.init.ModBlocks;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenPlains;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

/**
 * Created by Evan on 7/10/2015.
 */
public class BiomeGenPlainsMetro extends BiomeGenPlains {

    public BiomeGenPlainsMetro(int biomeID) {
        super(biomeID);
        this.setHeight(Biomes.height_FlatPlains);
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        //this.topBlock = ModBlocks.blockCement.getDefaultState();
        //this.fillerBlock = ModBlocks.blockCement.getDefaultState();
        //this.theBiomeDecorator.flowersPerChunk = 0;
        //this.theBiomeDecorator.grassPerChunk = 0;
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random random, ChunkPrimer chunkPrimer, int x, int z, double height)
    {
        this.newGenerateBiomeTerrain(worldIn, random, chunkPrimer, x, z, height);
    }

    public final void newGenerateBiomeTerrain(World worldIn, Random random, ChunkPrimer chunkPrimer, int x, int z, double biomeHeight)
    {
        boolean flag = true;
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate1 = this.fillerBlock;
        int k = -1;
        int l = (int)(biomeHeight / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        int i1 = x & 15;
        int j1 = z & 15;

        for (int k1 = 255; k1 >= 0; --k1)
        {
            if (k1 <= random.nextInt(5))
            {
                chunkPrimer.setBlockState(j1, k1, i1, Blocks.bedrock.getDefaultState());
            }
            else
            {
                IBlockState iblockstate2 = chunkPrimer.getBlockState(j1, k1, i1);

                if (iblockstate2.getBlock().getMaterial() == Material.air)
                {
                    k = -1;
                }
                else if (iblockstate2.getBlock() == Blocks.stone)
                {
                    if (k == -1)
                    {
                        if (l <= 0)
                        {
                            iblockstate = this.topBlock; // testing //null;
                            iblockstate1 = this.fillerBlock; //Blocks.stone.getDefaultState();
                        }
                        else if (k1 >= 59 && k1 <= 64)
                        {
                            iblockstate = this.topBlock;
                            iblockstate1 = this.fillerBlock;
                        }

                        if (k1 < 63 && (iblockstate == null || iblockstate.getBlock().getMaterial() == Material.air))
                        {
                            /*if (this.getFloatTemperature(new BlockPos(x, k1, z)) < 0.15F)
                            {
                                iblockstate = Blocks.ice.getDefaultState();
                            }
                            else
                            {
                                iblockstate = Blocks.water.getDefaultState();
                            }*/
                            iblockstate = ModBlocks.blockCement.getDefaultState().withProperty(BlockCement.VARIANT_PROP, BlockCement.CementType.RED); // eliminate water/ice pools due to elevation
                        }

                        k = l;

                        if (k1 >= 62)
                        {
                            chunkPrimer.setBlockState(j1, k1, i1, iblockstate);
                        }
                        else if (k1 < 56 - l)
                        {
                            iblockstate = null;
                            iblockstate1 = Blocks.stone.getDefaultState();
                            chunkPrimer.setBlockState(j1, k1, i1, Blocks.gravel.getDefaultState());
                        }
                        else
                        {
                            chunkPrimer.setBlockState(j1, k1, i1, iblockstate1);
                        }
                    }
                    else if (k > 0)
                    {
                        --k;
                        chunkPrimer.setBlockState(j1, k1, i1, iblockstate1);

                        if (k == 0 && iblockstate1.getBlock() == Blocks.sand)
                        {
                            k = random.nextInt(4) + Math.max(0, k1 - 63);
                            iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState();
                        }
                    }
                }
            }
        }
    }

    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return new MetropolisBiomeDecorator(); //getModdedBiomeDecorator(new BiomeDecorator());
    }
}
