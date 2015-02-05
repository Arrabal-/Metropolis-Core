package mod.arrabal.metrocore.common.item;

import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.library.GuiIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Created by Evan on 1/23/2015.
 */
public class ItemDataTablet extends ItemMetroCore {

    public ItemDataTablet(){
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setCreativeTab(MetropolisCore.tabMetroCoreItems);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
    }

    // used for clicking on something in world
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        //FMLNetworkHandler.openGui(playerIn, MetropolisCore.instance, GuiIDs.IN_GAME_GUIDE, worldIn, (int)hitX, (int)hitY, (int)hitZ);
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        FMLNetworkHandler.openGui(playerIn, MetropolisCore.instance, GuiIDs.IN_GAME_GUIDE, worldIn, 0,0,0);
        return itemStackIn;
    }
}
