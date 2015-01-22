package mod.arrabal.metrocore.common.block;

import com.google.common.collect.ImmutableSet;
import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.library.BlockStateHelper;
import net.minecraft.block.BlockStairs;
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
public class BlockMetroCoreStairs extends BlockStairs{

    public BlockMetroCoreStairs(IBlockState state){
        super(state);
        this.setCreativeTab(MetropolisCore.tabMetroWorld);
    }


}
