package mod.arrabal.metrocore.api;

import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;

/**
 * Created by Arrabal on 1/17/14.
 */
public class BlockHelper {
    public static Block get(String name){
        return GameRegistry.findBlock(ModRef.MOD_ID, name);
    }
}
