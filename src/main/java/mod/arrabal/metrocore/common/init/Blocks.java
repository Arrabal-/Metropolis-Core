package mod.arrabal.metrocore.common.init;

import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.block.*;
import mod.arrabal.metrocore.common.itemblocks.*;
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
        // Blocks
        ModBlocks.blockCement = new BlockCement();
        ModBlocks.blockCementPaver = new BlockCementPaver();
        registerBlock(ModBlocks.blockCement, "cement");
        registerBlock(ModBlocks.blockCementPaver, "paver");


        // Slabs
        ModBlocks.blockDoubleCementSlab = new BlockDoubleCementSlab();
        ModBlocks.blockCementSlab = new BlockHalfCementSlab();
        ModBlocks.blockDoublePolishedCementSlab = new BlockDoublePolishedCementSlab();
        ModBlocks.blockPolishedCementSlab = new BlockHalfPolishedCementSlab();
        ModBlocks.blockDoubleEtchedCementSlab = new BlockDoubleEtchedCementSlab();
        ModBlocks.blockEtchedCementSlab = new BlockHalfEtchedCementSlab();
        ModBlocks.blockDoubleCementPaverSlab = new BlockDoubleCementPaverSlab();
        ModBlocks.blockCementPaverSlab = new BlockHalfCementPaverSlab();
        registerBlock(ModBlocks.blockDoubleCementSlab, "double_cement_slab", ModBlocks.blockCementSlab, ModBlocks.blockDoubleCementSlab).setUnlocalizedName("cement_slab");
        registerBlock(ModBlocks.blockCementSlab, "cement_slab", ModBlocks.blockCementSlab, ModBlocks.blockDoubleCementSlab).setUnlocalizedName("cement_slab");
        registerBlock(ModBlocks.blockDoublePolishedCementSlab, "double_polished_slab", ModBlocks.blockPolishedCementSlab, ModBlocks.blockDoublePolishedCementSlab).setUnlocalizedName("polished_slab");
        registerBlock(ModBlocks.blockPolishedCementSlab, "polished_slab", ModBlocks.blockPolishedCementSlab, ModBlocks.blockDoublePolishedCementSlab).setUnlocalizedName("polished_slab");
        registerBlock(ModBlocks.blockDoubleEtchedCementSlab, "double_etched_slab", ModBlocks.blockEtchedCementSlab, ModBlocks.blockDoubleEtchedCementSlab).setUnlocalizedName("etched_slab");
        registerBlock(ModBlocks.blockEtchedCementSlab, "etched_slab", ModBlocks.blockEtchedCementSlab, ModBlocks.blockDoubleEtchedCementSlab).setUnlocalizedName("etched_slab");
        registerBlock(ModBlocks.blockDoubleCementPaverSlab, "double_paver_slab", ModBlocks.blockCementPaverSlab, ModBlocks.blockDoubleCementPaverSlab).setUnlocalizedName("paver_slab");
        registerBlock(ModBlocks.blockCementPaverSlab, "paver_slab", ModBlocks.blockCementPaverSlab, ModBlocks.blockDoubleCementPaverSlab).setUnlocalizedName("paver_slab");

        // Doors
        ModBlocks.blockGlassDoor = new BlockGlassDoor();
        registerBlock(ModBlocks.blockGlassDoor, "glass_door");

        // Stairs
        ModBlocks.blockCementStairs = new BlockMetroCoreStairs(ModBlocks.blockCement.getDefaultState());
        ModBlocks.blockCementStairsBlack = new BlockMetroCoreStairs(ModBlocks.blockCement.getDefaultState().withProperty(BlockCement.VARIANT_PROP, BlockCement.CementType.BLACK));
        ModBlocks.blockCementStairsWhite = new BlockMetroCoreStairs(ModBlocks.blockCement.getDefaultState().withProperty(BlockCement.VARIANT_PROP, BlockCement.CementType.WHITE));
        ModBlocks.blockCementStairsGray = new BlockMetroCoreStairs(ModBlocks.blockCement.getDefaultState().withProperty(BlockCement.VARIANT_PROP, BlockCement.CementType.GRAY));
        ModBlocks.blockCementStairsBrown = new BlockMetroCoreStairs(ModBlocks.blockCement.getDefaultState().withProperty(BlockCement.VARIANT_PROP, BlockCement.CementType.BROWN));
        ModBlocks.blockCementStairsRed = new BlockMetroCoreStairs(ModBlocks.blockCement.getDefaultState().withProperty(BlockCement.VARIANT_PROP, BlockCement.CementType.RED));
        ModBlocks.blockCementStairsTan = new BlockMetroCoreStairs(ModBlocks.blockCement.getDefaultState().withProperty(BlockCement.VARIANT_PROP, BlockCement.CementType.TAN));
        ModBlocks.blockCementStairsTerraCotta = new BlockMetroCoreStairs(ModBlocks.blockCement.getDefaultState().withProperty(BlockCement.VARIANT_PROP, BlockCement.CementType.TERRACOTTA));
        registerBlock(ModBlocks.blockCementStairs,"cement_stairs").setUnlocalizedName("cement_stairs");
        registerBlock(ModBlocks.blockCementStairsBlack,"cement_stairs_black").setUnlocalizedName("cement_stairs_black");
        registerBlock(ModBlocks.blockCementStairsWhite,"cement_stairs_white").setUnlocalizedName("cement_stairs_white");
        registerBlock(ModBlocks.blockCementStairsGray,"cement_stairs_gray").setUnlocalizedName("cement_stairs_gray");
        registerBlock(ModBlocks.blockCementStairsBrown,"cement_stairs_brown").setUnlocalizedName("cement_stairs_brown");
        registerBlock(ModBlocks.blockCementStairsRed,"cement_stairs_red").setUnlocalizedName("cement_stairs_red");
        registerBlock(ModBlocks.blockCementStairsTan,"cement_stairs_tan").setUnlocalizedName("cement_stairs_tan");
        registerBlock(ModBlocks.blockCementStairsTerraCotta,"cement_stairs_terracotta").setUnlocalizedName("cement_stairs_terracotta");

        //ModBlocks.assignBlocks();
    }

    public static void oreRegistration() {

    }

    public static Block registerBlock(Block block, String name, BlockSlab singleSlab, BlockSlab doubleSlab){
        BlockMetroCoreSlab slab = (BlockMetroCoreSlab)block;
        if(slab.baseStates==null)
            slab.baseStates = BlockStateHelper.getValidStatesForProperties(slab.getDefaultState(), slab.getBaseProperties());
        slab.setUnlocalizedName(name);
        if(slab.hasBaseProperties()){
            GameRegistry.registerBlock(slab, ItemBlockMetroCoreSlab.class, name, singleSlab, doubleSlab);
            for (IBlockState state : slab.baseStates) {
                String stateName = slab.getStateName(state, true);
                name = slab.isDouble() ? name.replace("double_", "") : name;
                ModelBakery.addVariantName(Item.getItemFromBlock(block), ModRef.MOD_ID + ":" + stateName + "_" + name);
                MetropolisCore.proxy.registerBlockForMeshing(slab, slab.getMetaFromState(state), stateName + "_" + name);
            }
        } else {
            GameRegistry.registerBlock(slab, name);
            name = slab.isDouble() ? name.replace("double_", "") : name;
            ModelBakery.addVariantName(Item.getItemFromBlock(slab), ModRef.MOD_ID + ":" + name);
            MetropolisCore.proxy.registerBlockForMeshing(slab, 0, name);
        }
        return slab;
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
        }  else {
            block.setUnlocalizedName(name);
            GameRegistry.registerBlock(block, name);
            MetropolisCore.proxy.registerItemForMeshing(Item.getItemFromBlock(block),0,name);
            return block;
        }
    }

}
