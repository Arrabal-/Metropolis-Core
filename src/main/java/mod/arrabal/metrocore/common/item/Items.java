package mod.arrabal.metrocore.common.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

/**
 * Created by Arrabal on 12/23/13.
 */
public class Items {

    public static void init() {

        //Doors
        registerItem(new ItemGlassDoor().setUnlocalizedName("itemDoorGlass"));
    }

    public static void registerRecipies() {

    }

    public static void registerItem(Item item)
    {
        GameRegistry.registerItem(item, item.getUnlocalizedName().replace("item.", ""));
    }

}
