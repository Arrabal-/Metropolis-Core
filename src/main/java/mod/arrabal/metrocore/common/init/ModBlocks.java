package mod.arrabal.metrocore.common.init;

import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Arrabal on 1/13/2015.
 */

@GameRegistry.ObjectHolder(ModRef.MOD_ID)
public class ModBlocks {

    // Blocks
    public static Block blockCement;
    public static Block blockCementPaver;

    // Slabs
    public static BlockSlab blockCementSlab;
    public static BlockSlab blockDoubleCementSlab;
    public static BlockSlab blockPolishedCementSlab;
    public static BlockSlab blockDoublePolishedCementSlab;
    public static BlockSlab blockEtchedCementSlab;
    public static BlockSlab blockDoubleEtchedCementSlab;
    public static BlockSlab blockCementPaverSlab;
    public static BlockSlab blockDoubleCementPaverSlab;

    // Doors
    public static Block blockGlassDoor;

    private static Block getRegisteredBlock(String name){
        return (Block)GameRegistry.findBlock(ModRef.MOD_ID, name);
    }

    public static void assignBlocks(){
        blockCement = getRegisteredBlock("cement");
        blockCementPaver = getRegisteredBlock("paver");
        blockGlassDoor = getRegisteredBlock("glass_door");
    }
}
