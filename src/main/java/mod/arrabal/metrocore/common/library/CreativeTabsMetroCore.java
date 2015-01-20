package mod.arrabal.metrocore.common.library;

import mod.arrabal.metrocore.api.ItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Arrabal on 1/15/14.
 */
public class CreativeTabsMetroCore extends CreativeTabs{
    public CreativeTabsMetroCore(int position, String tabID){
        super(position, tabID);
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return (new ItemStack((Item) GameRegistry.findItem(ModRef.MOD_ID,"glass_door"), 1, 0));
    }

    @Override
    public Item getTabIconItem() {
        return null;
    }
}
