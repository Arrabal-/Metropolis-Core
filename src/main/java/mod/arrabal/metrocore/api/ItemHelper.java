package mod.arrabal.metrocore.api;

import cpw.mods.fml.common.registry.GameRegistry;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.item.Item;

/**
 * Created by Arrabal on 1/17/14.
 */
public class ItemHelper {
    public static Item get(String name){
        return GameRegistry.findItem(ModRef.MOD_ID, name);
    }
}
