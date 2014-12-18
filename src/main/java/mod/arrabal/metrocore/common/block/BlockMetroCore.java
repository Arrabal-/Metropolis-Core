package mod.arrabal.metrocore.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 7/14/2014.
 */
public class BlockMetroCore extends Block {

    public BlockMetroCore(Material material) {
        super(material);
        this.setCreativeTab(MetropolisCore.tabMetroWorld);
    }

    public BlockMetroCore(){
        super(Material.rock);
    }

    @Override
    public String getUnlocalizedName()
    {
        return String.format("tile.%s%s", ModRef.TEXTURE_LOCATION, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    public void breakBlock(World world, BlockPos blockpos, IExtendedBlockState blockstate)
    {
        //dropInventory(world, x, y, z);
        super.breakBlock(world, blockpos, blockstate);
    }

    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IExtendedBlockState state, int fortune)
    {
        return super.getDrops(world, pos, state, fortune);
    }

}
