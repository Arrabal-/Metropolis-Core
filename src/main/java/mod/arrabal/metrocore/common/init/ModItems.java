package mod.arrabal.metrocore.common.init;

import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Evan on 1/20/2015.
 */
@GameRegistry.ObjectHolder(ModRef.MOD_ID)
public class ModItems {

    // Doors
    public static Item itemGlassDoor;

    // Tools
    public static Item itemDataTablet;

    private static Item getRegisteredItem(String name){
        return (Item)GameRegistry.findItem(ModRef.MOD_ID, name);
    }

    public static void assignItems(){
        itemGlassDoor = getRegisteredItem("glass_door");
    }
}
