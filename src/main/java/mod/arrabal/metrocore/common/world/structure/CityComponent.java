package mod.arrabal.metrocore.common.world.structure;

import mod.arrabal.metrocore.common.world.cities.MetropolisBaseBB;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.Direction;
import net.minecraft.util.Facing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 6/30/2014.
 */
public abstract class CityComponent {

    protected MetropolisBaseBB boundingBox;
    protected int tileTypeID;
    protected int coordBaseMode;

    public CityComponent() {}

    protected CityComponent(int componentType){
        this.tileTypeID = componentType;
        this.coordBaseMode = -1;
    }

    public void buildComponent(CityComponent cityTile, List tileList, Random random) {}

    public abstract boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox);

    public MetropolisBaseBB getBoundingBox(){
        return this.boundingBox;
    }

    public int getTileTypeID(){
        return this.tileTypeID;
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

    public ChunkPosition getChunkPosition(){
        return new ChunkPosition(this.boundingBox.getCenterX(), this.getBoundingBox().getCenterY(), this.getBoundingBox().getCenterZ());
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
                if (world.getBlock(k1, j, l1).getMaterial().isLiquid())
                {
                    return true;
                }

                if (world.getBlock(k1, i1, l1).getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        for (k1 = i; k1 <= l; ++k1)
        {
            for (l1 = j; l1 <= i1; ++l1)
            {
                if (world.getBlock(k1, l1, k).getMaterial().isLiquid())
                {
                    return true;
                }

                if (world.getBlock(k1, l1, j1).getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        for (k1 = k; k1 <= j1; ++k1)
        {
            for (l1 = j; l1 <= i1; ++l1)
            {
                if (world.getBlock(i, l1, k1).getMaterial().isLiquid())
                {
                    return true;
                }

                if (world.getBlock(l, l1, k1).getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected int getXWithOffset(int posX, int posZ)
    {
        switch (this.coordBaseMode)
        {
            case 0:
            case 2:
                return this.boundingBox.minX + posX;
            case 1:
                return this.boundingBox.maxX - posZ;
            case 3:
                return this.boundingBox.minX + posZ;
            default:
                return posX;
        }
    }

    protected int getYWithOffset(int posY)
    {
        return this.coordBaseMode == -1 ? posY : posY + this.boundingBox.minY;
    }

    protected int getZWithOffset(int posX, int posZ)
    {
        switch (this.coordBaseMode)
        {
            case 0:
                return this.boundingBox.minZ + posZ;
            case 1:
            case 3:
                return this.boundingBox.minZ + posX;
            case 2:
                return this.boundingBox.maxZ - posZ;
            default:
                return posZ;
        }
    }

    protected int getMetadataWithOffset(Block block, int meta)
    {
        if (block == Blocks.rail)
        {
            if (this.coordBaseMode == 1 || this.coordBaseMode == 3)
            {
                if (meta == 1)
                {
                    return 0;
                }

                return 1;
            }
        }
        else if (block != Blocks.wooden_door && block != Blocks.iron_door)
        {
            if (block != Blocks.stone_stairs && block != Blocks.oak_stairs && block != Blocks.nether_brick_stairs && block != Blocks.stone_brick_stairs && block != Blocks.sandstone_stairs)
            {
                if (block == Blocks.ladder)
                {
                    if (this.coordBaseMode == 0)
                    {
                        if (meta == 2)
                        {
                            return 3;
                        }

                        if (meta == 3)
                        {
                            return 2;
                        }
                    }
                    else if (this.coordBaseMode == 1)
                    {
                        if (meta == 2)
                        {
                            return 4;
                        }

                        if (meta == 3)
                        {
                            return 5;
                        }

                        if (meta == 4)
                        {
                            return 2;
                        }

                        if (meta == 5)
                        {
                            return 3;
                        }
                    }
                    else if (this.coordBaseMode == 3)
                    {
                        if (meta == 2)
                        {
                            return 5;
                        }

                        if (meta == 3)
                        {
                            return 4;
                        }

                        if (meta == 4)
                        {
                            return 2;
                        }

                        if (meta == 5)
                        {
                            return 3;
                        }
                    }
                }
                else if (block == Blocks.stone_button)
                {
                    if (this.coordBaseMode == 0)
                    {
                        if (meta == 3)
                        {
                            return 4;
                        }

                        if (meta == 4)
                        {
                            return 3;
                        }
                    }
                    else if (this.coordBaseMode == 1)
                    {
                        if (meta == 3)
                        {
                            return 1;
                        }

                        if (meta == 4)
                        {
                            return 2;
                        }

                        if (meta == 2)
                        {
                            return 3;
                        }

                        if (meta == 1)
                        {
                            return 4;
                        }
                    }
                    else if (this.coordBaseMode == 3)
                    {
                        if (meta == 3)
                        {
                            return 2;
                        }

                        if (meta == 4)
                        {
                            return 1;
                        }

                        if (meta == 2)
                        {
                            return 3;
                        }

                        if (meta == 1)
                        {
                            return 4;
                        }
                    }
                }
                else if (block != Blocks.tripwire_hook && !(block instanceof BlockDirectional))
                {
                    if (block == Blocks.piston || block == Blocks.sticky_piston || block == Blocks.lever || block == Blocks.dispenser)
                    {
                        if (this.coordBaseMode == 0)
                        {
                            if (meta == 2 || meta == 3)
                            {
                                return Facing.oppositeSide[meta];
                            }
                        }
                        else if (this.coordBaseMode == 1)
                        {
                            if (meta == 2)
                            {
                                return 4;
                            }

                            if (meta == 3)
                            {
                                return 5;
                            }

                            if (meta == 4)
                            {
                                return 2;
                            }

                            if (meta == 5)
                            {
                                return 3;
                            }
                        }
                        else if (this.coordBaseMode == 3)
                        {
                            if (meta == 2)
                            {
                                return 5;
                            }

                            if (meta == 3)
                            {
                                return 4;
                            }

                            if (meta == 4)
                            {
                                return 2;
                            }

                            if (meta == 5)
                            {
                                return 3;
                            }
                        }
                    }
                }
                else if (this.coordBaseMode == 0)
                {
                    if (meta == 0 || meta == 2)
                    {
                        return Direction.rotateOpposite[meta];
                    }
                }
                else if (this.coordBaseMode == 1)
                {
                    if (meta == 2)
                    {
                        return 1;
                    }

                    if (meta == 0)
                    {
                        return 3;
                    }

                    if (meta == 1)
                    {
                        return 2;
                    }

                    if (meta == 3)
                    {
                        return 0;
                    }
                }
                else if (this.coordBaseMode == 3)
                {
                    if (meta == 2)
                    {
                        return 3;
                    }

                    if (meta == 0)
                    {
                        return 1;
                    }

                    if (meta == 1)
                    {
                        return 2;
                    }

                    if (meta == 3)
                    {
                        return 0;
                    }
                }
            }
            else if (this.coordBaseMode == 0)
            {
                if (meta == 2)
                {
                    return 3;
                }

                if (meta == 3)
                {
                    return 2;
                }
            }
            else if (this.coordBaseMode == 1)
            {
                if (meta == 0)
                {
                    return 2;
                }

                if (meta == 1)
                {
                    return 3;
                }

                if (meta == 2)
                {
                    return 0;
                }

                if (meta == 3)
                {
                    return 1;
                }
            }
            else if (this.coordBaseMode == 3)
            {
                if (meta == 0)
                {
                    return 2;
                }

                if (meta == 1)
                {
                    return 3;
                }

                if (meta == 2)
                {
                    return 1;
                }

                if (meta == 3)
                {
                    return 0;
                }
            }
        }
        else if (this.coordBaseMode == 0)
        {
            if (meta == 0)
            {
                return 2;
            }

            if (meta == 2)
            {
                return 0;
            }
        }
        else
        {
            if (this.coordBaseMode == 1)
            {
                return meta + 1 & 3;
            }

            if (this.coordBaseMode == 3)
            {
                return meta + 3 & 3;
            }
        }

        return meta;
    }

    protected void placeBlockAtCurrentPosition(World world, Block block, int meta, int posX, int posY, int posZ, MetropolisBaseBB boundingBox)
    {
        int i1 = this.getXWithOffset(posX, posZ);
        int j1 = this.getYWithOffset(posY);
        int k1 = this.getZWithOffset(posX, posZ);

        if (boundingBox.isVecInside(i1, j1, k1))
        {
            world.setBlock(i1, j1, k1, block, meta, 2);
        }
    }

    protected Block getBlockAtCurrentPosition(World world, int posX, int posY, int posZ, MetropolisBaseBB boundingBox)
    {
        int l = this.getXWithOffset(posX, posZ);
        int i1 = this.getYWithOffset(posY);
        int j1 = this.getZWithOffset(posX, posZ);
        return !boundingBox.isVecInside(l, i1, j1) ? Blocks.air : world.getBlock(l, i1, j1);
    }

    protected void fillWithAir(World world, MetropolisBaseBB boundingBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
    {
        for (int k1 = minY; k1 <= maxY; ++k1)
        {
            for (int l1 = minX; l1 <= maxX; ++l1)
            {
                for (int i2 = minZ; i2 <= maxZ; ++i2)
                {
                    this.placeBlockAtCurrentPosition(world, Blocks.air, 0, l1, k1, i2, boundingBox);
                }
            }
        }
    }

    protected void fillWithBlocks(World world, MetropolisBaseBB boundingBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
                                  Block placeBlock, Block replaceBlock, boolean alwaysReplace)
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
                                          int maxZ, Block placeBlock, int placeBlockMetadata, Block replaceBlock, int replaceBlockMetadata,
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
     * maxZ, boolean alwaysreplace, Random rand, StructurePieceBlockSelector blockselector
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
                                          int maxX, int maxY, int maxZ, Block placeBlock, Block replaceBlock, boolean alwaysreplace)
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

    protected void randomlyPlaceBlockAtPostion(World world, MetropolisBaseBB boundingBox, Random random, float randLimit, int posX, int posY, int posZ, Block placeBlock, int meta)
    {
        if (random.nextFloat() < randLimit)
        {
            this.placeBlockAtCurrentPosition(world, placeBlock, meta, posX, posY, posZ, boundingBox);
        }
    }

    protected void fillSphereWithBlocks(World world, MetropolisBaseBB boundingBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Block placeBlock, boolean alwaysreplace)
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

        if (boundingBox.isVecInside(l, i1, j1))
        {
            while (!world.isAirBlock(l, i1, j1) && i1 < 255)
            {
                world.setBlock(l, i1, j1, Blocks.air, 0, 2);
                ++i1;
            }
        }
    }

    protected void fillCurrentPositionBlocksDownward(World world, Block placeBlock, int meta, int posX, int posY, int posZ,
                                 MetropolisBaseBB boundingBox)
    {
        int i1 = this.getXWithOffset(posX, posZ);
        int j1 = this.getYWithOffset(posY);
        int k1 = this.getZWithOffset(posX, posZ);

        if (boundingBox.isVecInside(i1, j1, k1))
        {
            while ((world.isAirBlock(i1, j1, k1) || world.getBlock(i1, j1, k1).getMaterial().isLiquid()) && j1 > 1)
            {
                world.setBlock(i1, j1, k1, placeBlock, meta, 2);
                --j1;
            }
        }
    }

    protected boolean generateStructureChestContents(World world, MetropolisBaseBB boundingBox, Random random,
                                                     int posX, int posY, int posZ, WeightedRandomChestContent[] chestContents,
                                                     int maxIterations)
    {
        int i1 = this.getXWithOffset(posX, posZ);
        int j1 = this.getYWithOffset(posY);
        int k1 = this.getZWithOffset(posX, posZ);

        if (boundingBox.isVecInside(i1, j1, k1) && world.getBlock(i1, j1, k1) != Blocks.chest)
        {
            world.setBlock(i1, j1, k1, Blocks.chest, 0, 2);
            TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(i1, j1, k1);

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

        if (boundingBox.isVecInside(j1, k1, l1) && world.getBlock(j1, k1, l1) != Blocks.dispenser)
        {
            world.setBlock(j1, k1, l1, Blocks.dispenser, this.getMetadataWithOffset(Blocks.dispenser, meta), 2);
            TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getTileEntity(j1, k1, l1);

            if (tileentitydispenser != null)
            {
                WeightedRandomChestContent.generateDispenserContents(random, dispenserContents, tileentitydispenser, maxIterations);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected void placeDoorAtCurrentPosition(World world, MetropolisBaseBB boundingBox, Random random, int posX, int posY, int posZ,
                                              int meta, Block doorBlock)
    {
        int i1 = this.getXWithOffset(posX, posZ);
        int j1 = this.getYWithOffset(posY);
        int k1 = this.getZWithOffset(posX, posZ);

        if (boundingBox.isVecInside(i1, j1, k1))
        {
            ItemDoor.placeDoorBlock(world, i1, j1, k1, meta, doorBlock);
        }
    }

    public abstract static class BlockSelector{

        protected Block block;
        protected int selectedBlockMetaData;

        protected BlockSelector()
        {
            this.block = Blocks.air;
        }


        public abstract void selectBlocks(Random random, int posX, int posY, int posZ, boolean doBlockSelect);

        public Block getSelectedBlock()
        {
            return this.block;
        }

        public int getSelectedBlockMetaData()
        {
            return this.selectedBlockMetaData;
        }
    }
}
