package mod.arrabal.metrocore.common.init;

import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.block.*;
import mod.arrabal.metrocore.common.itemblocks.*;
import mod.arrabal.metrocore.common.library.BlockStateHelper;
import net.minecraft.block.BlockDoor;
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
        ModBlocks.blockCement = new BlockCement();
        ModBlocks.blockCementPaver = new BlockCementPaver();
        registerBlock(ModBlocks.blockCement, "cement");
        registerBlock(ModBlocks.blockCementPaver, "paver");


        //Slabs
        ModBlocks.blockDoubleCementSlab = new BlockDoubleCementSlab();
        ModBlocks.blockCementSlab = new BlockHalfCementSlab();
        ModBlocks.blockDoublePolishedCementSlab = new BlockDoublePolishedCementSlab();
        ModBlocks.blockPolishedCementSlab = new BlockHalfPolishedCementSlab();
        ModBlocks.blockDoubleEtchedCementSlab = new BlockDoubleEtchedCementSlab();
        ModBlocks.blockEtchedCementSlab = new BlockHalfEtchedCementSlab();
        ModBlocks.blockDoubleCementPaverSlab = new BlockDoubleCementPaverSlab();
        ModBlocks.blockCementPaverSlab = new BlockHalfCementPaverSlab();
        registerBlock(ModBlocks.blockDoubleCementSlab, "double_cement_slab").setUnlocalizedName("cement_slab");
        registerBlock(ModBlocks.blockCementSlab, "cement_slab").setUnlocalizedName("cement_slab");
        registerBlock(ModBlocks.blockDoublePolishedCementSlab, "double_polished_slab").setUnlocalizedName("polished_slab");
        registerBlock(ModBlocks.blockPolishedCementSlab, "polished_slab").setUnlocalizedName("polished_slab");
        registerBlock(ModBlocks.blockDoubleEtchedCementSlab, "double_etched_slab").setUnlocalizedName("etched_slab");
        registerBlock(ModBlocks.blockEtchedCementSlab, "etched_slab").setUnlocalizedName("etched_slab");
        registerBlock(ModBlocks.blockDoubleCementPaverSlab, "double_paver_slab").setUnlocalizedName("paver_slab");
        registerBlock(ModBlocks.blockCementPaverSlab, "paver_slab").setUnlocalizedName("paver_slab");

        //Stairs


        //Doors
        ModBlocks.blockGlassDoor = new BlockGlassDoor();
        registerBlock(ModBlocks.blockGlassDoor, "glass_door");

        //ModBlocks.assignBlocks();
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
        } else if (block instanceof BlockPolishedCementSlab) {
            BlockPolishedCementSlab slab = (BlockPolishedCementSlab) block;
            if (slab.baseStates == null)
                slab.baseStates = BlockStateHelper.getValidStatesForProperties(slab.getDefaultState(), slab.getBaseProperties());
            slab.setUnlocalizedName(name);
            if (slab.hasBaseProperties()) {
                GameRegistry.registerBlock(block, ItemPolishedCementSlab.class, name, ModBlocks.blockPolishedCementSlab, ModBlocks.blockDoublePolishedCementSlab);
                for (IBlockState state : slab.baseStates) {
                    String stateName = slab.getStateName(state, true);
                    name = name.contains("double_") ? name.replace("double_", "") : name;
                    ModelBakery.addVariantName(Item.getItemFromBlock(block), ModRef.MOD_ID + ":" + stateName + "_" + name);
                    MetropolisCore.proxy.registerBlockForMeshing(slab, slab.getMetaFromState(state), stateName + "_" + name);
                }
            } else {
                GameRegistry.registerBlock(block, name);
                name = name.contains("double_") ? name.replace("double_", "") : name;
                ModelBakery.addVariantName(Item.getItemFromBlock(block), ModRef.MOD_ID + ":" + name);
                MetropolisCore.proxy.registerBlockForMeshing(slab, 0, name);
            }
            return slab;
        } else if (block instanceof BlockEtchedCementSlab) {
            BlockEtchedCementSlab slab = (BlockEtchedCementSlab) block;
            if (slab.baseStates == null)
                slab.baseStates = BlockStateHelper.getValidStatesForProperties(slab.getDefaultState(), slab.getBaseProperties());
            slab.setUnlocalizedName(name);
            if (slab.hasBaseProperties()) {
                GameRegistry.registerBlock(block, ItemEtchedCementSlab.class, name, ModBlocks.blockEtchedCementSlab, ModBlocks.blockDoubleEtchedCementSlab);
                for (IBlockState state : slab.baseStates) {
                    String stateName = slab.getStateName(state, true);
                    name = name.contains("double_") ? name.replace("double_", "") : name;
                    ModelBakery.addVariantName(Item.getItemFromBlock(block), ModRef.MOD_ID + ":" + stateName + "_" + name);
                    MetropolisCore.proxy.registerBlockForMeshing(slab, slab.getMetaFromState(state), stateName + "_" + name);
                }
            } else {
                GameRegistry.registerBlock(block, name);
                name = name.contains("double_") ? name.replace("double_", "") : name;
                ModelBakery.addVariantName(Item.getItemFromBlock(block), ModRef.MOD_ID + ":" + name);
                MetropolisCore.proxy.registerBlockForMeshing(slab, 0, name);
            }
            return slab;
        } else if (block instanceof BlockCementPaverSlab) {
            BlockCementPaverSlab slab = (BlockCementPaverSlab) block;
            if (slab.baseStates == null)
                slab.baseStates = BlockStateHelper.getValidStatesForProperties(slab.getDefaultState(), slab.getBaseProperties());
            slab.setUnlocalizedName(name);
            if (slab.hasBaseProperties()) {
                GameRegistry.registerBlock(block, ItemCementPaverSlab.class, name, ModBlocks.blockCementPaverSlab, ModBlocks.blockDoubleCementPaverSlab);
                for (IBlockState state : slab.baseStates) {
                    String stateName = slab.getStateName(state, true);
                    name = name.contains("double_") ? name.replace("double_", "") : name;
                    ModelBakery.addVariantName(Item.getItemFromBlock(block), ModRef.MOD_ID + ":" + stateName + "_" + name);
                    MetropolisCore.proxy.registerBlockForMeshing(slab, slab.getMetaFromState(state), stateName + "_" + name);
                }
            } else {
                GameRegistry.registerBlock(block, name);
                name = name.contains("double_") ? name.replace("double_", "") : name;
                ModelBakery.addVariantName(Item.getItemFromBlock(block), ModRef.MOD_ID + ":" + name);
                MetropolisCore.proxy.registerBlockForMeshing(slab, 0, name);
            }
            return slab;
        } else if (block instanceof BlockCementSlab){
            BlockCementSlab slab = (BlockCementSlab)block;
            if (slab.baseStates == null)
                slab.baseStates = BlockStateHelper.getValidStatesForProperties(slab.getDefaultState(), slab.getBaseProperties());
            slab.setUnlocalizedName(name);
            if (slab.hasBaseProperties()) {
                GameRegistry.registerBlock(block, ItemCementSlab.class, name, ModBlocks.blockCementSlab, ModBlocks.blockDoubleCementSlab);
                for (IBlockState state : slab.baseStates) {
                    String stateName = slab.getStateName(state, true);
                    name = name.contains("double_") ? name.replace("double_", "") : name;
                    ModelBakery.addVariantName(Item.getItemFromBlock(block), ModRef.MOD_ID + ":" + stateName + "_" + name);
                    MetropolisCore.proxy.registerBlockForMeshing(slab, slab.getMetaFromState(state), stateName + "_" + name);
                }
            } else {
                GameRegistry.registerBlock(block, name);
                name = name.contains("double_") ? name.replace("double_", "") : name;
                ModelBakery.addVariantName(Item.getItemFromBlock(block), ModRef.MOD_ID + ":" + name);
                MetropolisCore.proxy.registerBlockForMeshing(slab, 0, name);
            }
            return slab;
        } else if (block instanceof BlockMetroCoreDoor) {
            BlockMetroCoreDoor MCblock = (BlockMetroCoreDoor)block;
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
        } else {
            return null;
        } //for later use
    }

}
