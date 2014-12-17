package mod.arrabal.metrocore.common.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.arrabal.metrocore.api.BlockHelper;
import mod.arrabal.metrocore.api.ItemHelper;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Arrabal on 1/16/14.
 */
public class BlockGlassDoor extends BlockDoor {

    @SideOnly(Side.CLIENT)
    private static IIcon[] icons_up;
    @SideOnly(Side.CLIENT)
    private static IIcon[] icons_low;

    public BlockGlassDoor(){
        super(Material.glass);
        this.setHarvestLevel(null, 0);
        this.setStepSound(Block.soundTypeGlass);
        this.disableStats();
        this.setBlockTextureName("doorGlass");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        return BlockGlassDoor.icons_low[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        if (par5 != 1 && par5 != 0) {
            int i1;
            i1 = this.getFullMetadata(par1IBlockAccess, par2, par3, par4);
            int j1 = i1 & 3;
            boolean flag = (i1 & 4) != 0;
            boolean flag1 = false;
            boolean flag2 = (i1 & 8) != 0;

            if (flag) {
                if (j1 == 0 && par5 == 2) {
                    flag1 = !flag1;
                } else if (j1 == 1 && par5 == 5) {
                    flag1 = !flag1;
                } else if (j1 == 2 && par5 == 3) {
                    flag1 = !flag1;
                } else if (j1 == 3 && par5 == 4) {
                    flag1 = !flag1;
                }
            } else {
                if (j1 == 0 && par5 == 5) {
                    flag1 = !flag1;
                } else if (j1 == 1 && par5 == 3) {
                    flag1 = !flag1;
                } else if (j1 == 2 && par5 == 4) {
                    flag1 = !flag1;
                } else if (j1 == 3 && par5 == 2) {
                    flag1 = !flag1;
                }

                if ((i1 & 16) != 0) {
                    flag1 = !flag1;
                }
            }

            return flag2 ? BlockGlassDoor.icons_up[flag1 ? 1 : 0] : BlockGlassDoor.icons_low[flag1 ? 1 : 0];
        } else {
            return BlockGlassDoor.icons_low[0];
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        BlockGlassDoor.icons_up = new IIcon[2];
        BlockGlassDoor.icons_low = new IIcon[2];
        BlockGlassDoor.icons_up[0] = par1IconRegister.registerIcon(ModRef.TEXTURE_LOCATION + "doorGlass_upper");
        BlockGlassDoor.icons_low[0] = par1IconRegister.registerIcon(ModRef.TEXTURE_LOCATION + "doorGlass_lower");
        BlockGlassDoor.icons_up[1] = new IconFlipped(BlockGlassDoor.icons_up[0], true, false);
        BlockGlassDoor.icons_low[1] = new IconFlipped(BlockGlassDoor.icons_low[0], true, false);
    }


    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3) {
        return (par1 & 8) != 0 ? null : ItemHelper.get("itemDoorGlass");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        Item item = ItemHelper.get("itemDoorGlass");
        if (item == null)
        {
            return null;
        }
        Block block = item instanceof ItemBlock ? Block.getBlockFromItem(item) : this;
        return new ItemStack(item, 1, block.getDamageValue(world, x, y, z));
    }

    @Override
    public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
        if (par6EntityPlayer.capabilities.isCreativeMode && (par5 & 8) != 0 && par1World.getBlock(par2, par3 - 1, par4) ==
                BlockHelper.get("blockGlassDoor")) {
            par1World.setBlockToAir(par2, par3 - 1, par4);
        }
    }


    public int getFullMetadata(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        int l = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        boolean flag = (l & 8) != 0;
        int i1;
        int j1;

        if (flag) {
            i1 = par1IBlockAccess.getBlockMetadata(par2, par3 - 1, par4);
            j1 = l;
        } else {
            i1 = l;
            j1 = par1IBlockAccess.getBlockMetadata(par2, par3 + 1, par4);
        }

        boolean flag1 = (j1 & 1) != 0;
        return i1 & 7 | (flag ? 8 : 0) | (flag1 ? 16 : 0);
    }

}
