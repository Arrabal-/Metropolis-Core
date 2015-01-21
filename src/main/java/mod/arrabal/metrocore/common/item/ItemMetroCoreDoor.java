package mod.arrabal.metrocore.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Evan on 1/20/2015.
 */
public class ItemMetroCoreDoor extends ItemDoor {

    private Block block;

    public ItemMetroCoreDoor(Block block){
        super(block);
        this.block = block;

    }

    /*@Override
    public String getUnlocalizedName()
    {
        String unlocalizedName = super.getUnlocalizedName();
        return unlocalizedName.substring(unlocalizedName.indexOf(".")+1);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        String unlocalizedName = super.getUnlocalizedName();
        return unlocalizedName.substring(unlocalizedName.indexOf(".")+1);
    }*/

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (side != EnumFacing.UP)
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block blockToPlaceOn = iblockstate.getBlock();

            if (!blockToPlaceOn.isReplaceable(worldIn, pos))
            {
                pos = pos.offset(side);
            }

            if (!playerIn.canPlayerEdit(pos, side, stack))
            {
                return false;
            }
            else if (!this.block.canPlaceBlockAt(worldIn, pos))
            {
                return false;
            }
            else
            {
                placeDoor(worldIn, pos, EnumFacing.fromAngle((double)playerIn.rotationYaw), this.block);
                --stack.stackSize;
                return true;
            }
        }
    }
}
