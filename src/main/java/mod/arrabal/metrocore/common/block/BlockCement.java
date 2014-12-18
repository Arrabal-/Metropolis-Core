package mod.arrabal.metrocore.common.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.library.BlockRef;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Created by Arrabal on 12/19/13.
 */
public class  BlockCement extends BlockMetroCore {

    public static enum BlockCategory {PLAIN, POLISHED};
    private final BlockCategory category;

    public BlockCement(BlockCategory cat) {
        super(Material.rock);
        category = cat;
        this.setStepSound(Block.soundTypeStone);
        this.setHarvestLevel("pickaxe", 2);
    }

    private static IIcon[] icons;
    private static IIcon[] icons_p;


    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister){
        if (category == BlockCategory.PLAIN) {
            icons = new IIcon[BlockRef.CEMENT_UNLOCALIZED_NAME.length];
            for (int i = 0; i < icons.length; i++) {
                icons[i] = iconRegister.registerIcon(ModRef.TEXTURE_LOCATION + BlockRef.CEMENT_UNLOCALIZED_NAME[i]);
            }
        }
        else {
            icons_p = new IIcon[BlockRef.POLISHED_CEMENT_UNLOCALIZED_NAME.length];
            for (int i = 0; i < icons_p.length; i++) {
                icons_p[i] = iconRegister.registerIcon(ModRef.TEXTURE_LOCATION + BlockRef.POLISHED_CEMENT_UNLOCALIZED_NAME[i]);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
        if (category == BlockCategory.PLAIN) {
            if (meta < 0 || meta >= BlockRef.CEMENT_UNLOCALIZED_NAME.length)
            {
             meta = 0;
            }
            return icons[meta];
        } else {
            if (meta < 0 || meta >= BlockRef.POLISHED_CEMENT_UNLOCALIZED_NAME.length)
            {
                meta = 0;
            }
            return icons_p[meta];
        }
    }

    @Override
    public int damageDropped(int meta){
        return meta;
    }

    @Override
    public void getSubBlocks(Item block, CreativeTabs creativeTabs, List list){
        int max = 0;
        if (category == BlockCategory.PLAIN) {
            max = 16;
        } else if (category == BlockCategory.POLISHED) {
            max = 8;
        }
        for (int i = 0; i < max; i++) {
            list.add(new ItemStack(block, 1, i));
        }
    }

    public String getBlockTypedName(int meta){
        if (category == BlockCategory.PLAIN) {
            return (new StringBuilder()).append(BlockRef.CEMENT_UNLOCALIZED_NAME[meta]).toString();
        }
        return (new StringBuilder()).append(BlockRef.POLISHED_CEMENT_UNLOCALIZED_NAME[meta]).toString();
    }
}
