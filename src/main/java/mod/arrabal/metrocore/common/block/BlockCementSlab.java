package mod.arrabal.metrocore.common.block;

import mod.arrabal.metrocore.common.init.Blocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.api.BlockHelper;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 1/15/14.
 */

public abstract class BlockCementSlab extends BlockMetroCoreSlab {

    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", CementSlabType.class);
    public static final PropertyBool SEEMLESS = PropertyBool.create("seemless");

    protected BlockCementSlab(){
        super(Material.rock);
        IBlockState state = this.blockState.getBaseState();

        if (this.isDouble()){
            state = state.withProperty(SEEMLESS, Boolean.valueOf(false));
        } else {
            state = state.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.setDefaultState(state.withProperty(VARIANT,CementSlabType.LIGHTGRAY));

    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune){
        return Item.getItemFromBlock(Blocks.blockCementSlab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, BlockPos pos){
        return Item.getItemFromBlock(Blocks.blockCementSlab);
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return null;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public IProperty getVariantProperty() {
        return null;
    }

    @Override
    public Object getVariant(ItemStack stack) {
        return null;
    }

    public static enum SlabCategory {SMOOTH, PAVER;}

    private static final String[] smoothTypes = new String[] {
            "blockCement",
            "blockCementBlack",
            "blockCementBrown",
            "blockCementGray",
            "blockCementLightGray",
            "blockCementRed",
            "blockCementTan",
            "blockCementTerraCotta"
    };
    private static final String[] paverTypes = new String[] {
            "blockCementPaver",
            "blockCementPaverBlack",
            "blockCementPaverBrown",
            "blockCementPaverGray",
            "blockCementPaverLightGray",
            "blockCementPaverRed",
            "blockCementPaverTan",
            "blockCementPaverTerraCotta"
    };

    public BlockCementSlab(boolean isDoubleSlab, Material material, SlabCategory cat) {
        super( material);
        //category = cat;
        this.setHardness(3.0F);
        this.setResistance(3.0F);
        this.setStepSound(Block.soundTypeStone);
        useNeighborBrightness = true;
        this.setHarvestLevel("pickaxe", 2);


        if (!isDoubleSlab)
        {
            this.setCreativeTab(MetropolisCore.tabMetroWorld);
        }
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getSubBlocks(Item block, CreativeTabs creativeTabs, List list)
    {
        int max = 0;

       // if (category == SlabCategory.SMOOTH) {
            max = 8;
        //}
        //else if (category == SlabCategory.PAVER) {
            //max = 8;
        //}
        for (int i = 0; i < max; ++i) {
            list.add(new ItemStack(block, 1, i));
        }
    }

    private static int getTypeFromMeta(int meta){
        return meta & 7;
    }

    public static enum CementSlabType implements IStringSerializable {

        LIGHTGRAY,
        GRAY,
        WHITE,
        BLACK,
        RED,
        TAN,
        BROWN,
        TERRACOTTA;

        public String getName(){
            return this.name().toLowerCase();
        }

        @Override
        public String toString(){
            return getName();
        }
    }
}
