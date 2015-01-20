package mod.arrabal.metrocore.common.block;

import com.google.common.collect.ImmutableSet;
import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.library.BlockStateHelper;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 12/30/2014.
 */
public abstract class BlockMetroCoreDoor extends BlockDoor{

    public ImmutableSet<IBlockState> baseStates;

    protected BlockMetroCoreDoor(Material material){
        super(material);
        this.baseStates = BlockStateHelper.getValidStatesForProperties(this.getDefaultState(), this.getBaseProperties());
        this.setCreativeTab(MetropolisCore.tabMetroWorld);
    }

    protected abstract Item getItem();

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){ return null;}

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
