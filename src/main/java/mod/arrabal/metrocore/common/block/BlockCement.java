package mod.arrabal.metrocore.common.block;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;


/**
 * Created by Arrabal on 12/19/13.
 */
public class  BlockCement extends BlockMetroCore {

    public static PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", CementType.class);
    public static PropertyBool POLISHED_PROP = PropertyBool.create("polished");

    public BlockCement(){

        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, CementType.LIGHTGRAY).withProperty(POLISHED_PROP,Boolean.valueOf(false)));
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(3.0F);
        this.setResistance(8.0F);
    }

//    @Override
//    public EnumWorldBlockLayer getBlockLayer(){
//        return EnumWorldBlockLayer.SOLID;
//    }

    @Override
    public IBlockState getStateFromMeta(int meta){

        boolean polished = (meta & 8) > 0;
        int color = meta & 7;
        return this.getDefaultState().withProperty(VARIANT_PROP, CementType.values()[color]).withProperty(POLISHED_PROP, Boolean.valueOf(polished));
    }

    @Override
    public int getMetaFromState(IBlockState state){
        int meta = ((CementType)state.getValue(VARIANT_PROP)).ordinal();
        boolean polished = (Boolean)state.getValue(POLISHED_PROP);
        return polished ? meta | 8 : meta;
    }

    @Override
    protected BlockState createBlockState(){
        return new BlockState(this, new IProperty[] {VARIANT_PROP, POLISHED_PROP});
    }

    @Override
    public IProperty[] getBaseProperties(){
        return new IProperty[] {VARIANT_PROP, POLISHED_PROP};
    }

    @Override
    public String getStateName(IBlockState state, boolean fullName){
        boolean polished = (Boolean)state.getValue(POLISHED_PROP);
        return (fullName && polished ? "polished_" : "") + ((CementType)state.getValue(VARIANT_PROP)).getName();
    }

    public static enum CementType implements IStringSerializable{

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
