package mod.arrabal.metrocore.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

/**
 * Created by Arrabal on 1/6/2015.
 */

public class BlockCementPaver extends BlockMetroCore {

    public static PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", PaverType.class);
    public static PropertyBool ETCHED_PROP = PropertyBool.create("etched");

    public BlockCementPaver(){

        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, PaverType.LIGHTGRAY).withProperty(ETCHED_PROP,Boolean.valueOf(false)));
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(3.0F);
        this.setResistance(10.0F);
    }

    @Override
    public IBlockState getStateFromMeta(int meta){

        boolean etched = (meta & 8) > 0;
        int color = meta & 7;
        return this.getDefaultState().withProperty(VARIANT_PROP, PaverType.values()[color]).withProperty(ETCHED_PROP, Boolean.valueOf(etched));
    }

    @Override
    public int getMetaFromState(IBlockState state){
        int meta = ((PaverType)state.getValue(VARIANT_PROP)).ordinal();
        boolean etched = (Boolean)state.getValue(ETCHED_PROP);
        return etched ? meta | 8 : meta;
    }

    @Override
    protected BlockState createBlockState(){
        return new BlockState(this, new IProperty[] {VARIANT_PROP, ETCHED_PROP});
    }

    @Override
    public IProperty[] getBaseProperties(){
        return new IProperty[] {VARIANT_PROP, ETCHED_PROP};
    }

    @Override
    public String getStateName(IBlockState state, boolean fullName){
        boolean etched = (Boolean)state.getValue(ETCHED_PROP);
        return ((fullName && etched) ? "etched_" : "") + ((PaverType)state.getValue(VARIANT_PROP)).getName();
    }

    public static enum PaverType implements IStringSerializable {

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
