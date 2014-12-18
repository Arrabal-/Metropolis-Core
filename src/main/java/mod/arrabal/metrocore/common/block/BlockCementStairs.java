package mod.arrabal.metrocore.common.block;

import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Arrabal on 12/29/13.
 */
public class BlockCementStairs extends BlockStairs {

    public static enum Category {
        WHITE ("stone"), BLACK ("stone"), BROWN ("stone"), GRAY ("stone"), LGRAY ("stone"), RED ("stone"), TAN ("stone"), CLAY ("stone"),
        ETCHEDWHITE ("stone"), ETCHEDBLACK ("stone"), ETCHEDBROWN ("stone"), ETCHEDGRAY ("stone"), ETCHEDLGRAY ("stone"), ETCHEDRED ("stone"),
        ETCHEDTAN ("stone"), ETCHEDCLAY ("stone");

        private final List<String> values;
        private String type;

        private Category(String type) {
            this.type = type;
            values = Arrays.asList(type);
        }
    }

    private final static String[] types = new String[] {
            "blockCement",
            "blockCementBlack",
            "blockCementBrown",
            "blockCementGray",
            "blockCementLightGray",
            "blockCementRed",
            "blockCementTan",
            "blockCementTerraCotta",
            "blockCementPaver",
            "blockCementPaverBlack",
            "blockCementPaverBrown",
            "blockCementPaverGray",
            "blockCementPaverLightGray",
            "blockCementPaverRed",
            "blockCementPaverTan",
            "blockCementPaverTerraCotta"
    };

    private final Category category;

    public BlockCementStairs(Block model, Category cat, IExtendedBlockState state) {
        super(state);
        category = cat;
        this.setHardness(3.0F);
        this.setHarvestLevel("pickaxe", 2);
        this.setStepSound(Block.soundTypeStone);
        useNeighborBrightness = true;
        this.setCreativeTab(MetropolisCore.tabMetroWorld);
    }

}
