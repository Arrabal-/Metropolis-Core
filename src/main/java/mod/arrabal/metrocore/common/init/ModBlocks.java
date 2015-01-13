package mod.arrabal.metrocore.common.init;

import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Evan on 1/13/2015.
 */

@GameRegistry.ObjectHolder(ModRef.MOD_ID)
public class ModBlocks {

    public static Block blockCement;
    public static Block blockCementPaver;
    public static BlockSlab blockDoubleCementSlab;
    public static BlockSlab blockCementSlab;

    private static Block getRegisteredBlock(String name){
        return (Block)GameRegistry.findBlock(ModRef.MOD_ID, name);
    }

    public static void AssignBlocks(){
        blockCement = getRegisteredBlock("cement");
        blockCementPaver = getRegisteredBlock("paver");
        blockDoubleCementSlab = (BlockSlab)getRegisteredBlock("double_cement_slab");
        blockCementSlab = (BlockSlab)getRegisteredBlock("cement_slab");
    }
}
