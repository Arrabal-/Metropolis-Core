package mod.arrabal.metrocore.common.block;

import mod.arrabal.metrocore.common.init.ModBlocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 1/15/14.
 */

public abstract class BlockCementSlab extends BlockMetroCoreSlab {

    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", CementSlabType.class);
    public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");

    protected BlockCementSlab(){
        super(Material.rock);
        IBlockState state = this.blockState.getBaseState();

        if (this.isDouble()){
            state = state.withProperty(SEAMLESS, Boolean.valueOf(true));
        } else {
            state = state.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.setDefaultState(state.withProperty(VARIANT,CementSlabType.LIGHTGRAY));
        this.setHardness(3.0F);
        this.setResistance(10.0F);
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune){
        return Item.getItemFromBlock(ModBlocks.blockCementSlab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, BlockPos pos){
        return Item.getItemFromBlock(ModBlocks.blockCementSlab);
    }

    @Override
    public String getUnlocalizedName(int meta) {

        return super.getUnlocalizedName(meta) + "." + BlockCementSlab.CementSlabType.byMetadata(meta).getUnlocalizedName();
    }

    @Override
    public IProperty getVariantProperty()
    {
        return VARIANT;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public Object getVariant(ItemStack stack) {
        return BlockCementSlab.CementSlabType.byMetadata(stack.getMetadata() & 7);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTab, List list){
        if (item != Item.getItemFromBlock(ModBlocks.blockDoubleCementSlab)){
            BlockCementSlab.CementSlabType[] types =  BlockCementSlab.CementSlabType.values();
            int typeCount = types.length;
            for (int i = 0; i < typeCount; ++i){
                BlockCementSlab.CementSlabType type = types[i];
                list.add(new ItemStack(item, 1, type.getMetadata()));
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        IBlockState state = this.getDefaultState().withProperty(VARIANT, CementSlabType.byMetadata(meta & 7));
        if (this.isDouble()){
            state = state.withProperty(SEAMLESS, Boolean.valueOf((meta & 8) != 0));
        } else {
            state = state.withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }
        return state;
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        byte b = 0;
        int meta = b | ((BlockCementSlab.CementSlabType)state.getValue(VARIANT)).getMetadata();
        if (this.isDouble()) {
            if (((Boolean) state.getValue(SEAMLESS)).booleanValue()) {
                meta |= 8;
            }
        }
        else {
            if (state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
                meta |= 8;
            }
        }
        return meta;
    }

    @Override
    public IProperty[] getBaseProperties() {
        return this.isDouble() ? new IProperty[] {SEAMLESS, VARIANT} : new IProperty[] {HALF, VARIANT};}

    @Override
    protected BlockState createBlockState()
    {
        return this.isDouble() ? new BlockState(this, new IProperty[] {SEAMLESS, VARIANT}): new BlockState(this, new IProperty[] {HALF, VARIANT});
    }

    @Override
    public int damageDropped(IBlockState state){
        return ((CementSlabType)state.getValue(VARIANT)).getMetadata();
    }

    @Override
    public String getStateName(IBlockState state, boolean fullName){
        return ((CementSlabType)state.getValue(VARIANT)).getName();

    }

    public static enum CementSlabType implements IStringSerializable {

        LIGHTGRAY(0,"lightgray"),
        GRAY(1,"gray"),
        WHITE(2,"white"),
        BLACK(3,"black"),
        RED(4,"red"),
        TAN(5,"tan"),
        BROWN(6,"brown"),
        TERRACOTTA(7,"terracotta");
        private static final BlockCementSlab.CementSlabType[] META_LOOKUP = new BlockCementSlab.CementSlabType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private CementSlabType(int meta, String name){
            this(meta, name, name);
        }

        private CementSlabType(int meta, String name, String unlocalizedName){
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public int getMetadata(){return this.meta;}

        public static BlockCementSlab.CementSlabType byMetadata(int meta){
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName(){
            return this.name().toLowerCase();
        }

        public String getUnlocalizedName(){
            return this.unlocalizedName;
        }

        @Override
        public String toString(){
            return getName();
        }

        static{
            BlockCementSlab.CementSlabType[] types = values();
            int typeNum = types.length;

            for (int i = 0; i < typeNum; ++i){
                BlockCementSlab.CementSlabType variant = types[i];
                META_LOOKUP[variant.getMetadata()] = variant;
            }
        }
    }
}
