package mod.arrabal.metrocore.common.world.structure;

import mod.arrabal.metrocore.common.world.cities.MetropolisBaseBB;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 6/30/2014.
 *
 * Base class for structures and tiles used for generating and building cities.  Class
 * is extended as part of the individual subclasses within CityComponentPieces
 */
public abstract class CityComponent {

    protected MetropolisBaseBB boundingBox;
    protected int typeID;
    protected int typeVariant;
    protected int coordBaseMode;

    public CityComponent() {}

    protected CityComponent(int componentType){
        this.typeID = componentType;
        this.coordBaseMode = -1;
    }

    public abstract void buildComponent(CityComponent cityTile, Random random);

    public abstract void buildComponent(CityComponent cityTile, Random random, int chunkX, int chunkZ);

    public abstract boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox);

    public MetropolisBaseBB getBoundingBox(){
        return this.boundingBox;
    }

    public int getTypeID(){
        return this.typeID;
    }

    public int getTypeVariant() {
        return this.typeVariant;
    }

    public static CityComponent findIntersecting(List tileList, MetropolisBaseBB boundingBox){
        Iterator iterator = tileList.iterator();
        CityComponent cityComponent;

        do{
            if (!iterator.hasNext()){
                return null;
            }
            cityComponent = (CityComponent) iterator.next();
        } while (cityComponent.getBoundingBox() == null || !cityComponent.getBoundingBox().intersectsWith(boundingBox));

        return cityComponent;
    }

    public BlockPos getChunkPosition(){
        return new BlockPos(this.boundingBox.getCenterX(), this.boundingBox.getCenterY(), this.boundingBox.getCenterZ());
    }

    protected boolean isLiquidInCityBoundingBox(World world, MetropolisBaseBB boundingBox)
    {
        int i = Math.max(this.boundingBox.minX - 1, boundingBox.minX);
        int j = Math.max(this.boundingBox.minY - 1, boundingBox.minY);
        int k = Math.max(this.boundingBox.minZ - 1, boundingBox.minZ);
        int l = Math.min(this.boundingBox.maxX + 1, boundingBox.maxX);
        int i1 = Math.min(this.boundingBox.maxY + 1, boundingBox.maxY);
        int j1 = Math.min(this.boundingBox.maxZ + 1, boundingBox.maxZ);
        int k1;
        int l1;

        for (k1 = i; k1 <= l; ++k1)
        {
            for (l1 = k; l1 <= j1; ++l1)
            {
                if (world.getBlockState(new BlockPos(k1, j, l1)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }

                if (world.getBlockState(new BlockPos(k1, i1, l1)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
            }
        }

        for (k1 = i; k1 <= l; ++k1)
        {
            for (l1 = j; l1 <= i1; ++l1)
            {
                if (world.getBlockState(new BlockPos(k1,l1, k)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }

                if (world.getBlockState(new BlockPos(k1,l1,j1)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        for (k1 = k; k1 <= j1; ++k1)
        {
            for (l1 = j; l1 <= i1; ++l1)
            {
                if (world.getBlockState(new BlockPos(i,l1,k1)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }

                if (world.getBlockState(new BlockPos(l,l1,k1)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        return false;
    }

    // Gets a relative x position in the bounding box based on offset values for x, z, and the coordBaseMode
    protected int getXWithOffset(int offsetX, int offsetZ)
    {
        switch (this.coordBaseMode)
        {
            case 0:
            case 2:
                return this.boundingBox.minX + offsetX;
            case 1:
                return this.boundingBox.maxX - offsetZ;
            case 3:
                return this.boundingBox.minX + offsetZ;
            default:
                return offsetX;
        }
    }

    // Gets a relative y position in the bounding box based on offset value for y and the coordBaseMode
    protected int getYWithOffset(int offsetY)
    {
        return this.coordBaseMode == -1 ? offsetY : offsetY + this.boundingBox.minY;
    }

    // Gets a relative z position in the bounding box based on offset values for x, z, and the coordBaseMode
    protected int getZWithOffset(int offsetX, int offsetZ)
    {

        return 0;
    }

    protected void placeBlockAtCurrentPosition(World world, IBlockState blockstate, int meta, int posX, int posY, int posZ, MetropolisBaseBB boundingBox)
    {
        int i1 = this.getXWithOffset(posX, posZ);
        int j1 = this.getYWithOffset(posY);
        int k1 = this.getZWithOffset(posX, posZ);

        if (boundingBox.isVecInside(i1, j1, k1))
        {
            world.setBlockState(new BlockPos(i1, j1, k1), blockstate, 2);
        }
    }

    protected Block getBlockAtCurrentPosition(World world, int posX, int posY, int posZ, MetropolisBaseBB boundingBox)
    {
        int l = this.getXWithOffset(posX, posZ);
        int i1 = this.getYWithOffset(posY);
        int j1 = this.getZWithOffset(posX, posZ);
        return !boundingBox.isVecInside(l, i1, j1) ? Blocks.air : world.getBlockState(new BlockPos(l,i1,j1)).getBlock();
    }

    protected void fillWithAir(World world, MetropolisBaseBB boundingBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
    {
        for (int k1 = minY; k1 <= maxY; ++k1)
        {
            for (int l1 = minX; l1 <= maxX; ++l1)
            {
                for (int i2 = minZ; i2 <= maxZ; ++i2)
                {
                    this.placeBlockAtCurrentPosition(world, Blocks.air.getDefaultState(), 0, l1, k1, i2, boundingBox);
                }
            }
        }
    }

    protected void fillWithBlocks(World world, MetropolisBaseBB boundingBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
                                  IBlockState placeBlock, IBlockState replaceBlock, boolean alwaysReplace)
    {
        for (int k1 = minY; k1 <= maxY; ++k1)
        {
            for (int l1 = minX; l1 <= maxX; ++l1)
            {
                for (int i2 = minZ; i2 <= maxZ; ++i2)
                {
                    if (!alwaysReplace || this.getBlockAtCurrentPosition(world, l1, k1, i2, boundingBox).getMaterial() != Material.air)
                    {
                        if (k1 != minY && k1 != maxY && l1 != minX && l1 != maxX && i2 != minZ && i2 != maxZ)
                        {
                            this.placeBlockAtCurrentPosition(world, replaceBlock, 0, l1, k1, i2, boundingBox);
                        }
                        else
                        {
                            this.placeBlockAtCurrentPosition(world, placeBlock, 0, l1, k1, i2, boundingBox);
                        }
                    }
                }
            }
        }
    }

    protected void fillWithMetadataBlocks(World world, MetropolisBaseBB boundingBox, int minX, int minY, int minZ, int maxX, int maxY,
                                          int maxZ, IBlockState placeBlock, int placeBlockMetadata, IBlockState replaceBlock, int replaceBlockMetadata,
                                          boolean alwaysReplace)
    {
        for (int i2 = minY; i2 <= maxY; ++i2)
        {
            for (int j2 = minX; j2 <= maxX; ++j2)
            {
                for (int k2 = minZ; k2 <= maxZ; ++k2)
                {
                    if (!alwaysReplace || this.getBlockAtCurrentPosition(world, j2, i2, k2, boundingBox).getMaterial() != Material.air)
                    {
                        if (i2 != minY && i2 != maxY && j2 != minX && j2 != maxX && k2 != minZ && k2 != maxZ)
                        {
                            this.placeBlockAtCurrentPosition(world, replaceBlock, replaceBlockMetadata, j2, i2, k2, boundingBox);
                        }
                        else
                        {
                            this.placeBlockAtCurrentPosition(world, placeBlock, placeBlockMetadata, j2, i2, k2, boundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * arguments: World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ, boolean alwaysreplace, Random rand, BlockSelector blockselector
     */
    protected void fillWithRandomizedBlocks(World world, MetropolisBaseBB boundingBox, int minX, int minY, int minZ,
                                            int maxX, int maxY, int maxZ, boolean alwaysreplace, Random random,
                                            CityComponent.BlockSelector blockSelector)
    {
        for (int k1 = minY; k1 <= maxY; ++k1)
        {
            for (int l1 = minX; l1 <= maxX; ++l1)
            {
                for (int i2 = minZ; i2 <= maxZ; ++i2)
                {
                    if (!alwaysreplace || this.getBlockAtCurrentPosition(world, l1, k1, i2, boundingBox).getMaterial() != Material.air)
                    {
                        blockSelector.selectBlocks(random, l1, k1, i2, k1 == minY || k1 == maxY || l1 == minX || l1 == maxX || i2 == minZ || i2 == maxZ);
                        this.placeBlockAtCurrentPosition(world, blockSelector.getSelectedBlock(), blockSelector.getSelectedBlockMetaData(), l1, k1, i2, boundingBox);
                    }
                }
            }
        }
    }

    /**
     * arguments: World worldObj, StructureBoundingBox structBB, Random rand, float randLimit, int minX, int minY, int
     * minZ, int maxX, int maxY, int maxZ, Block placeBlock, Block replaceBlock, boolean alwaysreplace
     */
    protected void randomlyFillWithBlocks(World world, MetropolisBaseBB boundingBox, Random random, float randLimit, int minX, int minY, int minZ,
                                          int maxX, int maxY, int maxZ, IBlockState placeBlock, IBlockState replaceBlock, boolean alwaysreplace)
    {
        for (int k1 = minY; k1 <= maxY; ++k1)
        {
            for (int l1 = minX; l1 <= maxX; ++l1)
            {
                for (int i2 = minZ; i2 <= maxZ; ++i2)
                {
                    if (random.nextFloat() <= randLimit && (!alwaysreplace || this.getBlockAtCurrentPosition(world, l1, k1, i2, boundingBox).getMaterial() != Material.air))
                    {
                        if (k1 != minY && k1 != maxY && l1 != minX && l1 != maxX && i2 != minZ && i2 != maxZ)
                        {
                            this.placeBlockAtCurrentPosition(world, replaceBlock, 0, l1, k1, i2, boundingBox);
                        }
                        else
                        {
                            this.placeBlockAtCurrentPosition(world, placeBlock, 0, l1, k1, i2, boundingBox);
                        }
                    }
                }
            }
        }
    }

    protected void randomlyPlaceBlockAtPostion(World world, MetropolisBaseBB boundingBox, Random random, float randLimit, int posX, int posY, int posZ, IBlockState placeBlock, int meta)
    {
        if (random.nextFloat() < randLimit)
        {
            this.placeBlockAtCurrentPosition(world, placeBlock, meta, posX, posY, posZ, boundingBox);
        }
    }

    protected void fillSphereWithBlocks(World world, MetropolisBaseBB boundingBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState placeBlock, boolean alwaysreplace)
    {
        float f = (float)(maxX - minX + 1);
        float f1 = (float)(maxY - minY + 1);
        float f2 = (float)(maxZ - minZ + 1);
        float f3 = (float)minX + f / 2.0F;
        float f4 = (float)minZ + f2 / 2.0F;

        for (int k1 = minY; k1 <= maxY; ++k1)
        {
            float f5 = (float)(k1 - minY) / f1;

            for (int l1 = minX; l1 <= maxX; ++l1)
            {
                float f6 = ((float)l1 - f3) / (f * 0.5F);

                for (int i2 = minZ; i2 <= maxZ; ++i2)
                {
                    float f7 = ((float)i2 - f4) / (f2 * 0.5F);

                    if (!alwaysreplace || this.getBlockAtCurrentPosition(world, l1, k1, i2, boundingBox).getMaterial() != Material.air)
                    {
                        float f8 = f6 * f6 + f5 * f5 + f7 * f7;

                        if (f8 <= 1.05F)
                        {
                            this.placeBlockAtCurrentPosition(world, placeBlock, 0, l1, k1, i2, boundingBox);
                        }
                    }
                }
            }
        }
    }

    protected void clearCurrentPositionBlocksUpwards(World world, int posX, int posY, int posZ, MetropolisBaseBB boundingBox)
    {
        int l = this.getXWithOffset(posX, posZ);
        int i1 = this.getYWithOffset(posY);
        int j1 = this.getZWithOffset(posX, posZ);

        BlockPos blockpos = new BlockPos(l, i1, j1);

        if (boundingBox.isVecInside(l, i1, j1))
        {
            while (!world.isAirBlock(blockpos) && blockpos.getY() < 255)
            {
                world.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
                blockpos.up();
            }
        }
    }

    protected void fillCurrentPositionBlocksDownward(World world, IBlockState placeBlock, int meta, int posX, int posY, int posZ,
                                 MetropolisBaseBB boundingBox)
    {
        int i1 = this.getXWithOffset(posX, posZ);
        int j1 = this.getYWithOffset(posY);
        int k1 = this.getZWithOffset(posX, posZ);

        if (boundingBox.isVecInside(i1, j1, k1))
        {
            while ((world.isAirBlock(new BlockPos(i1,j1,k1)) || world.getBlockState(new BlockPos(i1,j1,k1)).getBlock().getMaterial().isLiquid()) && j1 > 1)
            {
                world.setBlockState(new BlockPos(i1,j1,k1), placeBlock, 2);
                --j1;
            }
        }
    }

    protected boolean generateStructureChestContents(World world, MetropolisBaseBB boundingBox, Random random,
                                                     int posX, int posY, int posZ, List chestContents,
                                                     int maxIterations)
    {
        int i1 = this.getXWithOffset(posX, posZ);
        int j1 = this.getYWithOffset(posY);
        int k1 = this.getZWithOffset(posX, posZ);

        BlockPos blockpos = new BlockPos(i1,j1,k1);

        if (boundingBox.isVecInside(i1, j1, k1) && world.getBlockState(blockpos).getBlock() != Blocks.chest)
        {
            IBlockState iblockstate = Blocks.chest.getDefaultState();
            world.setBlockState(new BlockPos(i1, j1, k1),Blocks.chest.correctFacing(world, blockpos, iblockstate), 2);
            TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(blockpos);

            if (tileentitychest != null)
            {
                WeightedRandomChestContent.generateChestContents(random, chestContents, tileentitychest, maxIterations);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean generateStructureDispenserContents(World world, MetropolisBaseBB boundingBox, Random random, int posX, int posY,
                                                         int posZ, int meta, WeightedRandomChestContent[] dispenserContents, int maxIterations)
    {
        int j1 = this.getXWithOffset(posX, posZ);
        int k1 = this.getYWithOffset(posY);
        int l1 = this.getZWithOffset(posX, posZ);

        BlockPos blockpos = new BlockPos(j1,k1,l1);

        if (boundingBox.isVecInside(j1, k1, l1) && world.getBlockState(blockpos).getBlock() != Blocks.dispenser)
        {

            //need to fix
            return true;
        }
        else
        {
            return false;
        }
    }

    protected void placeDoorAtCurrentPosition(World world, MetropolisBaseBB boundingBox, Random random, int posX, int posY, int posZ,
                                              EnumFacing facing, Block doorBlock)
    {
        int i1 = this.getXWithOffset(posX, posZ);
        int j1 = this.getYWithOffset(posY);
        int k1 = this.getZWithOffset(posX, posZ);

        BlockPos blockpos = new BlockPos(i1,j1,k1);

        if (boundingBox.isVecInside(i1, j1, k1))
        {
            ItemDoor.placeDoor(world, blockpos, facing.rotateYCCW(), doorBlock);
        }
    }

    public String getHashKey(){
        return this.boundingBox.minX + " " + this.boundingBox.minY  + " " + this.boundingBox.minZ + " " +
                this.boundingBox.maxX + " " + this.boundingBox.maxY + " " + this.boundingBox.maxZ;
    }

    public abstract static class BlockSelector{

        protected IBlockState block;
        protected int selectedBlockMetaData;

        protected BlockSelector()
        {
            this.block = Blocks.air.getDefaultState();
        }


        public abstract void selectBlocks(Random random, int posX, int posY, int posZ, boolean doBlockSelect);

        public IBlockState getSelectedBlock()
        {
            return this.block;
        }

        public int getSelectedBlockMetaData()
        {
            return this.selectedBlockMetaData;
        }
    }
}
