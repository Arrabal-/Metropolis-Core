package mod.arrabal.metrocore.common.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.arrabal.metrocore.api.BlockHelper;
import mod.arrabal.metrocore.common.block.BlockCement;
import mod.arrabal.metrocore.common.block.BlockCementSlab;
import mod.arrabal.metrocore.common.block.BlockCementStairs;
import mod.arrabal.metrocore.common.block.BlockGlassDoor;
import mod.arrabal.metrocore.common.itemblocks.ItemBlockCement;
import mod.arrabal.metrocore.common.itemblocks.ItemBlockCementSlab;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;


/**
 * Created by Arrabal on 12/19/13.
 */

@GameRegistry.ObjectHolder(ModRef.MOD_ID)
public class Blocks {

    //public static Block doorGlass;

    //Blocks for future use
    public static Block cementBlock;
    public static Block polishedCementBlock;
    

    public static void init() {
        //Blocks


        //Slabs


        //Stairs


        //Doors


    }

    public static void oreRegistration() {

    }

    public static void registerBlock(Block block)
    {
        GameRegistry.registerBlock(block, block.getUnlocalizedName().replace("tile.", ""));
    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlockClass)
    {
        GameRegistry.registerBlock(block, itemBlockClass, block.getUnlocalizedName().replace("tile.", ""));
    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlockClass, String name, Object... constructorArgs)
    {
        GameRegistry.registerBlock(block, itemBlockClass, name, constructorArgs);
    }


}
