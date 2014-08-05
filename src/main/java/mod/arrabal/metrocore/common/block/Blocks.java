package mod.arrabal.metrocore.common.block;

import cpw.mods.fml.common.registry.GameRegistry;
import mod.arrabal.metrocore.api.BlockHelper;
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

    //Blocks

    public static void init() {
        //Blocks
        registerBlock(new BlockCement(BlockCement.BlockCategory.PLAIN).setBlockName("blockCement").setHardness(3.0F).setResistance(8.0F), ItemBlockCement.class);
        registerBlock(new BlockCement(BlockCement.BlockCategory.POLISHED).setBlockName("polishedCement").setHardness(3.0F).setResistance(8.0F), ItemBlockCement.class);

        //Slabs
        BlockCementSlab cementSingleSlab = (BlockCementSlab)new BlockCementSlab(false, Material.rock, BlockCementSlab.SlabCategory.SMOOTH).
                setBlockName("cementSingleSlab");
        BlockCementSlab cementDoubleSlab = (BlockCementSlab)new BlockCementSlab(true, Material.rock, BlockCementSlab.SlabCategory.SMOOTH).
                setBlockName("cementDoubleSlab");
        registerBlock(cementSingleSlab, ItemBlockCementSlab.class, cementSingleSlab.getUnlocalizedName().replace("tile.", ""), cementSingleSlab, cementDoubleSlab);
        registerBlock(cementDoubleSlab, ItemBlockCementSlab.class, cementDoubleSlab.getUnlocalizedName().replace("tile.", ""), cementSingleSlab, cementDoubleSlab);
        BlockCementSlab cementPaverSingleSlab = (BlockCementSlab)new BlockCementSlab(false, Material.rock, BlockCementSlab.SlabCategory.PAVER).
                setBlockName("cementPaverSingleSlab");
        BlockCementSlab cementPaverDoubleSlab = (BlockCementSlab)new BlockCementSlab(true, Material.rock, BlockCementSlab.SlabCategory.PAVER).
                setBlockName("cementPaverDoubleSlab");
        registerBlock(cementPaverSingleSlab, ItemBlockCementSlab.class, cementPaverSingleSlab.getUnlocalizedName().replace("tile.", ""), cementPaverSingleSlab, cementPaverDoubleSlab);
        registerBlock(cementPaverDoubleSlab, ItemBlockCementSlab.class, cementPaverDoubleSlab.getUnlocalizedName().replace("tile.", ""), cementPaverSingleSlab, cementPaverDoubleSlab);

        //Stairs
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.WHITE).setBlockName("stairsCement"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.BLACK).setBlockName("stairsCementBlack"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.BROWN).setBlockName("stairsCementBrown"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.GRAY).setBlockName("stairsCementGray"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.LGRAY).setBlockName("stairsCementLightGray"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.RED).setBlockName("stairsCementRed"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.TAN).setBlockName("stairsCementTan"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.CLAY).setBlockName("stairsCementTerraCotta"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.ETCHEDWHITE).setBlockName("stairsCement2"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.ETCHEDBLACK).setBlockName("stairsCementBlack2"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.ETCHEDBROWN).setBlockName("stairsCementBrown2"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.ETCHEDGRAY).setBlockName("stairsCementGray2"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.ETCHEDLGRAY).setBlockName("stairsCementLightGray2"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.ETCHEDRED).setBlockName("stairsCementRed2"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.ETCHEDTAN).setBlockName("stairsCementTan2"));
        registerBlock(new BlockCementStairs(BlockHelper.get("blockCement"), BlockCementStairs.Category.ETCHEDCLAY).setBlockName("stairsCementTerraCotta2"));

        //Doors
        registerBlock(new BlockGlassDoor().setBlockName("doorGlass").setHardness(1.0F).setResistance(1.0F));

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
