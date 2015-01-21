package mod.arrabal.metrocore.common.init;

import mod.arrabal.metrocore.MetropolisCore;
import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.arrabal.metrocore.common.item.ItemGlassDoor;
import net.minecraft.item.Item;

/**
 * Created by Arrabal on 12/23/13.
 */

public class Items {

    public static void init() {

        //Doors
        ModItems.itemGlassDoor = new ItemGlassDoor(ModBlocks.blockGlassDoor).setUnlocalizedName("glass_door");
        registerItem(ModItems.itemGlassDoor);

        //ModItems.assignItems();
    }

    public static void registerRecipies() {

    }

    public static void registerItem(Item item){
        GameRegistry.registerItem(item, item.getUnlocalizedName());
        MetropolisCore.proxy.registerItemForMeshing(item, 0, item.getUnlocalizedName());
    }

}
