package mod.arrabal.metrocore.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by Arrabal on 1/16/14.
 */
public class BlockGlassDoor extends BlockMetroCoreDoor {


    public BlockGlassDoor(){
        super(Material.glass);
        this.setHarvestLevel(null, 0);
        this.setStepSound(Block.soundTypeGlass);
        this.disableStats();
    }

}
