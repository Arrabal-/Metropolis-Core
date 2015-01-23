package mod.arrabal.metrocore.common.library;

import mod.arrabal.metrocore.common.init.ModBlocks;
import mod.arrabal.metrocore.common.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Arrabal on 1/15/14.
 */
public abstract class CreativeTabsMetroCore extends CreativeTabs{

    public CreativeTabsMetroCore(int position, String tabID){
        super(position, tabID);
    }

    public static final CreativeTabs tabMetroWorld = new CreativeTabs(CreativeTabs.getNextID(), "tabMetroWorld"){
        @SideOnly(Side.CLIENT)
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ModBlocks.blockCement);
        }
    };
    public static final CreativeTabs tabMetroCoreItems = new CreativeTabs(CreativeTabs.getNextID(), "tabMetroCoreItems"){
        @SideOnly(Side.CLIENT)
        @Override
        public Item getTabIconItem() {
            return ModItems.itemDataTablet;
        }
    };
}
