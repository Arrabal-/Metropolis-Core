package mod.arrabal.metrocore.common.init;

import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.item.ItemDataTablet;
import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.arrabal.metrocore.common.item.ItemGlassDoor;
import net.minecraft.item.Item;

/**
 * Created by Arrabal on 12/23/13.
 */

public class Items {

    public static void init() {

        // Doors
        ModItems.itemGlassDoor = new ItemGlassDoor(ModBlocks.blockGlassDoor).setUnlocalizedName("item_glass_door");
        registerItem(ModItems.itemGlassDoor, "item_glass_door");

        // Tools
        ModItems.itemDataTablet = new ItemDataTablet().setUnlocalizedName("item_data_tablet");
        registerItem(ModItems.itemDataTablet, "item_data_tablet");

        //ModItems.assignItems();
    }

    public static void registerRecipies() {

    }

    public static void registerItem(Item item, String name){
        GameRegistry.registerItem(item, name);
        MetropolisCore.proxy.registerItemForMeshing(item, 0, name);
    }

}
