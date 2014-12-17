package mod.arrabal.metrocore.common.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;

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


    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
    }


    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }


    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        //dropInventory(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

}
