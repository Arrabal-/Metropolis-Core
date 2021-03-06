package mod.arrabal.metrocore.common.block;

import com.google.common.collect.ImmutableSet;
import mod.arrabal.metrocore.common.library.BlockStateHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.arrabal.metrocore.MetropolisCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.List;

/**
 * Created by Arrabal on 7/14/2014.
 */
public abstract class BlockMetroCore extends Block {

    public ImmutableSet<IBlockState> baseStates;

    protected BlockMetroCore(Material material) {
        super(material);
        this.baseStates = BlockStateHelper.getValidStatesForProperties(this.getDefaultState(), this.getBaseProperties());
        this.setCreativeTab(MetropolisCore.tabMetroWorld);
    }

    protected BlockMetroCore(){
        super(Material.rock);
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

    public String getStateName(IBlockState state, boolean full){
        String unlocalizedName = state.getBlock().getUnlocalizedName();
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

}
