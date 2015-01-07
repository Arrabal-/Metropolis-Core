package mod.arrabal.metrocore.common.init;

import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.block.BlockCement;
import mod.arrabal.metrocore.common.block.BlockMetroCore;
import mod.arrabal.metrocore.common.itemblocks.ItemBlockMetroCoreWithVariants;
import mod.arrabal.metrocore.common.library.BlockStateHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;


/**
 * Created by Arrabal on 12/19/13.
 */

@GameRegistry.ObjectHolder(ModRef.MOD_ID)
public class Blocks {

    //public static Block doorGlass;

    public static Block blockCement;
    

    public static void init() {
        //Blocks
        blockCement = registerBlock(new BlockCement(), "cement");

        //Slabs


        //Stairs


        //Doors


    }

    public static void oreRegistration() {

    }

    public static Block registerBlock(BlockMetroCore block, String name)
    {
        if (block.baseStates == null) block.baseStates = BlockStateHelper.getValidStatesForProperties(block.getDefaultState(), block.getBaseProperties());
        block.setUnlocalizedName(name);
        if (block.hasBaseProperties()){
            GameRegistry.registerBlock(block, ItemBlockMetroCoreWithVariants.class, name);
            for (IBlockState state : block.baseStates){
                String stateName = block.getStateName(state, true);
                ModelBakery.addVariantName(Item.getItemFromBlock(block), ModRef.MOD_ID + ":" + stateName + "_" + name);
                MetropolisCore.proxy.registerBlockForMeshing(block, block.getMetaFromState(state), stateName + "_" + name);
            }
        } else {
            GameRegistry.registerBlock(block, name);

            ModelBakery.addVariantName(Item.getItemFromBlock(block), ModRef.MOD_ID + ":" + name);
            MetropolisCore.proxy.registerBlockForMeshing(block, 0, name);
        }
        return block;
    }


}
