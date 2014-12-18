package mod.arrabal.metrocore.common.block;

import net.minecraft.util.BlockPos;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.arrabal.metrocore.api.BlockHelper;
import mod.arrabal.metrocore.api.ItemHelper;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Arrabal on 1/16/14.
 */
public class BlockGlassDoor extends BlockDoor {


    public BlockGlassDoor(){
        super(Material.glass);
        this.setHarvestLevel(null, 0);
        this.setStepSound(Block.soundTypeGlass);
        this.disableStats();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos blockpos) {
        Item item = ItemHelper.get("itemDoorGlass");
        if (item == null)
        {
            return null;
        }
        Block block = item instanceof ItemBlock ? Block.getBlockFromItem(item) : this;
        return new ItemStack(item, 1, block.getDamageValue(world, blockpos));
    }

    public void onBlockHarvested(World world, BlockPos blockpos, IExtendedBlockState state, EntityPlayer player) {
        super.onBlockHarvested(world, blockpos, state, player);
    }

}
