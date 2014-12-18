package mod.arrabal.metrocore.common.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.api.BlockHelper;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 1/15/14.
 */
public class BlockCementSlab extends BlockSlab {

    public static enum SlabCategory {SMOOTH, PAVER;}

    private static final String[] smoothTypes = new String[] {
            "blockCement",
            "blockCementBlack",
            "blockCementBrown",
            "blockCementGray",
            "blockCementLightGray",
            "blockCementRed",
            "blockCementTan",
            "blockCementTerraCotta"
    };
    private static final String[] paverTypes = new String[] {
            "blockCementPaver",
            "blockCementPaverBlack",
            "blockCementPaverBrown",
            "blockCementPaverGray",
            "blockCementPaverLightGray",
            "blockCementPaverRed",
            "blockCementPaverTan",
            "blockCementPaverTerraCotta"
    };

    private IIcon[] icons;
    private IIcon[] sides;
    private final SlabCategory category;

    public BlockCementSlab(boolean isDoubleSlab, Material material, SlabCategory cat) {
        super(isDoubleSlab, material);
        category = cat;
        this.setHardness(3.0F);
        this.setResistance(3.0F);
        this.setStepSound(Block.soundTypeStone);
        useNeighborBrightness = true;
        this.setHarvestLevel("pickaxe", 2);


        if (!isDoubleSlab)
        {
            this.setCreativeTab(MetropolisCore.tabMetroWorld);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister){
        if (category == SlabCategory.SMOOTH)
        {
            icons = new IIcon[smoothTypes.length];

            for (int i = 0; i < smoothTypes.length; ++i)
            {
                icons[i] = iconRegister.registerIcon(ModRef.TEXTURE_LOCATION + smoothTypes[i]);
            }
        }
        else
        {
            icons = new IIcon[paverTypes.length];
            sides = new IIcon[paverTypes.length];

            for (int i = 0; i < paverTypes.length; ++i)
            {
                icons[i] = iconRegister.registerIcon(ModRef.TEXTURE_LOCATION + paverTypes[i]);
                sides[i] = iconRegister.registerIcon(ModRef.TEXTURE_LOCATION + paverTypes[i] + "_side");
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (category == SlabCategory.SMOOTH)
            return icons[getTypeFromMeta(meta)];
        else if (side ==1 || side == 0) {
            return icons[(getTypeFromMeta(meta) /*+ category.ordinal() * 8*/)];
        }
        return sides[getTypeFromMeta(meta)];
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getSubBlocks(Item block, CreativeTabs creativeTabs, List list)
    {
        int max = 0;

        if (category == SlabCategory.SMOOTH) {
            max = 8;
        }
        else if (category == SlabCategory.PAVER) {
            max = 8;
        }
        for (int i = 0; i < max; ++i) {
            list.add(new ItemStack(block, 1, i));
        }
    }

    @Override
    //TODO:		  getFullSlabName()
    public String func_150002_b(int meta)
    {
        if (category == SlabCategory.SMOOTH)
            return (new StringBuilder()).append(smoothTypes[getTypeFromMeta(meta)]).append("Slab").toString();
        else
            return (new StringBuilder()).append(paverTypes[getTypeFromMeta(meta)]).append("Slab").toString();
    }

    @Override
    public int damageDropped(int meta)
    {
        return meta & 7;
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune)
    {
        //TODO: isDoubleSlab
        if (field_150004_a)
        {
            if (this == BlockHelper.get("cementDoubleSlab"))
                return Item.getItemFromBlock(BlockHelper.get("cementSingleSlab"));
            else
                return Item.getItemFromBlock(BlockHelper.get("cementPaverSingleSlab"));
        }
        else
            return Item.getItemFromBlock(this);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        //TODO:  	   isDoubleSlab		                getItemFromBlock()
        Block block = !field_150004_a ? this : (this == BlockHelper.get("cementDoubleSlab") ?
                BlockHelper.get("cementSingleSlab") : BlockHelper.get("cementPaverSingleSlab"));

        return new ItemStack(block, 1);
    }

    @Override
    protected ItemStack createStackedBlock(int meta)
    {
        return new ItemStack(this, 2, meta);
    }

    private static int getTypeFromMeta(int meta){
        return meta & 7;
    }
}
