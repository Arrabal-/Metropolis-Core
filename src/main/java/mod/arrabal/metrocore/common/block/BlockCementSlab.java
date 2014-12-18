package mod.arrabal.metrocore.common.block;

import net.minecraft.block.properties.IProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.api.BlockHelper;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 1/15/14.
 */
public class BlockCementSlab extends BlockSlab {

    @Override
    public String getUnlocalizedName(int meta) {
        return null;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public IProperty getVariantProperty() {
        return null;
    }

    @Override
    public Object getVariant(ItemStack stack) {
        return null;
    }

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

    private final SlabCategory category;

    public BlockCementSlab(boolean isDoubleSlab, Material material, SlabCategory cat) {
        super( material);
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

    private static int getTypeFromMeta(int meta){
        return meta & 7;
    }
}
