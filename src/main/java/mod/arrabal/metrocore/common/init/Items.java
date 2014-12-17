package mod.arrabal.metrocore.common.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.arrabal.metrocore.common.item.ItemGlassDoor;
import net.minecraft.item.Item;

/**
 * Created by Arrabal on 12/23/13.
 */

public class Items {

    public static void init() {

        //Doors here
        registerItem(new ItemGlassDoor().setUnlocalizedName("itemDoorGlass"));
    }

    public static void registerRecipies() {

    }

    public static void registerItem(Item item)
    {
        GameRegistry.registerItem(item, item.getUnlocalizedName().replace("item.", ""));
    }

}
