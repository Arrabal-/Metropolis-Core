package mod.arrabal.metrocore.common.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.api.BlockHelper;
import mod.arrabal.metrocore.common.library.ItemRef;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Created by Arrabal on 12/23/13.
 */
public class ItemGlassDoor extends Item {

    private Material doorMaterial;

    public ItemGlassDoor() {
        this.doorMaterial = Material.glass;
        this.maxStackSize = 16;
        this.setCreativeTab(MetropolisCore.tabMetroWorld);
    }

    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        return false;
    }

    public static void placeDoorBlock(World p_150924_0_, int p_150924_1_, int p_150924_2_, int p_150924_3_, int p_150924_4_, Block p_150924_5_) {

    }
}
