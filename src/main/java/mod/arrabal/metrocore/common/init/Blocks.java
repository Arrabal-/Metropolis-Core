package mod.arrabal.metrocore.common.init;

import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.block.*;
import mod.arrabal.metrocore.common.itemblocks.ItemBlockMetroCoreWithVariants;
import mod.arrabal.metrocore.common.itemblocks.ItemCementSlab;
import mod.arrabal.metrocore.common.library.BlockStateHelper;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;


/**
 * Created by Arrabal on 12/19/13.
 */


public class Blocks {

    public static void init() {
        //Blocks
        registerBlock(new BlockCement(), "cement");
        registerBlock(new BlockCementPaver(), "paver");


        //Slabs
        registerBlock(new BlockDoubleCementSlab(), "double_cement_slab").setUnlocalizedName("cementslab");
        registerBlock(new BlockHalfCementSlab(), "cement_slab").setUnlocalizedName("cementslab");

        //Stairs


        //Doors


    }

    public static void oreRegistration() {

    }

    public static Block registerBlock(Block block, String name)
    {
        if (block instanceof BlockMetroCore) {
            BlockMetroCore MCblock = (BlockMetroCore)block;
            if (MCblock.baseStates == null)
                MCblock.baseStates = BlockStateHelper.getValidStatesForProperties(MCblock.getDefaultState(), MCblock.getBaseProperties());
            MCblock.setUnlocalizedName(name);
            if (MCblock.hasBaseProperties()) {
                GameRegistry.registerBlock(MCblock, ItemBlockMetroCoreWithVariants.class, name);
                for (IBlockState state : MCblock.baseStates) {
                    String stateName = MCblock.getStateName(state, true);
                    ModelBakery.addVariantName(Item.getItemFromBlock(MCblock), ModRef.MOD_ID + ":" + stateName + "_" + name);
                    MetropolisCore.proxy.registerBlockForMeshing(MCblock, MCblock.getMetaFromState(state), stateName + "_" + name);
                }
            } else {
                GameRegistry.registerBlock(MCblock, name);

                ModelBakery.addVariantName(Item.getItemFromBlock(MCblock), ModRef.MOD_ID + ":" + name);
                MetropolisCore.proxy.registerBlockForMeshing(MCblock, 0, name);
            }
            return MCblock;
        } //else if (block instanceof MetroCoreSlab){
        BlockMetroCoreSlab slab = (BlockMetroCoreSlab)block;
        if (slab.baseStates == null) slab.baseStates = BlockStateHelper.getValidStatesForProperties(slab.getDefaultState(), slab.getBaseProperties());
        slab.setUnlocalizedName(name);
        if(slab.hasBaseProperties()){
            GameRegistry.registerBlock(block, ItemCementSlab.class, name, ModBlocks.blockCementSlab, ModBlocks.blockDoubleCementSlab);
            for (IBlockState state : slab.baseStates){
                String stateName = slab.getStateName(state, true);
                ModelBakery.addVariantName(Item.getItemFromBlock(block),ModRef.MOD_ID + ":" + stateName + "_" + name);
                MetropolisCore.proxy.registerBlockForMeshing(slab, slab.getMetaFromState(state), stateName + "_" + name);
            }
        } else {
            GameRegistry.registerBlock(block, name);
            ModelBakery.addVariantName(Item.getItemFromBlock(block),ModRef.MOD_ID + ":" + name);
            MetropolisCore.proxy.registerBlockForMeshing(slab, 0, name);
        }
        return block;
    }

    /*public static Block registerBlock(BlockMetroCoreSlab block, String name){
        BlockMetroCoreSlab slab = (BlockMetroCoreSlab)block;
        if (slab.baseStates == null) slab.baseStates = BlockStateHelper.getValidStatesForProperties(block.getDefaultState(), slab.getBaseProperties());
        slab.setUnlocalizedName(name);
        if(slab.hasBaseProperties()){
            GameRegistry.registerBlock(block, ItemCementSlab.class, name);
            for (IBlockState state : slab.baseStates){
                String stateName = slab.getStateName(state, true);
                ModelBakery.addVariantName(Item.getItemFromBlock(block),ModRef.MOD_ID + ":" + stateName + "_" + name);
                MetropolisCore.proxy.registerBlockForMeshing(slab, slab.getMetaFromState(state), stateName + "_" + name);
            }
        } else {
            GameRegistry.registerBlock(block, name);
            ModelBakery.addVariantName(Item.getItemFromBlock(block),ModRef.MOD_ID + ":" + name);
            MetropolisCore.proxy.registerBlockForMeshing(slab, 0, name);
        }
        return block;
    }*/


}
