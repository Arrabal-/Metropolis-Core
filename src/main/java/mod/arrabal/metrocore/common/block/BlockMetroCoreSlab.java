package mod.arrabal.metrocore.common.block;

import com.google.common.collect.ImmutableSet;
import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.library.BlockStateHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Arrabal on 12/31/2014.
 */
public abstract class BlockMetroCoreSlab extends BlockSlab{

    public ImmutableSet<IBlockState> baseStates;

    protected BlockMetroCoreSlab(Material material){
        super(material);
        this.baseStates = BlockStateHelper.getValidStatesForProperties(this.getDefaultState(), this.getBaseProperties());
        this.setCreativeTab(MetropolisCore.tabMetroWorld);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {

        if (this.hasBaseProperties()) {
            for (IBlockState state : baseStates) {
                list.add(new ItemStack(item, 1, this.getMetaFromState(state)));
            }
        } else {
            list.add(new ItemStack(item, 1, 0));
        }
    }

    @SideOnly(Side.CLIENT)
    protected static boolean isSlab(Block blockIn){
        return (blockIn instanceof BlockMetroCoreSlab);
    }

    @Override
    public int damageDropped(IBlockState state){
        return this.getMetaFromState(state);
    }

    public IProperty[] getBaseProperties(){
        return null;
    }

    public boolean hasBaseProperties(){
        return getBaseProperties() != null;
    }

    @Override
    public String getUnlocalizedName(int meta){
        IBlockState state = this.getStateFromMeta(meta);
        String unlocalizedName = state.getBlock().getUnlocalizedName();
        //String stateName = getStateName(state, false);
        //String unlocalizedName = super.getUnlocalizedName() + stateName;
        return unlocalizedName;
    }

    public String getStateName(IBlockState state, boolean full){
        String unlocalizedName = state.getBlock().getUnlocalizedName();
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}
