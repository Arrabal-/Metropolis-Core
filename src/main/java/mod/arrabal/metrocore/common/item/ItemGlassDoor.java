package mod.arrabal.metrocore.common.item;

import mod.arrabal.metrocore.MetropolisCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by Arrabal on 12/23/13.
 */
public class ItemGlassDoor extends ItemMetroCoreDoor {

    private Material doorMaterial;

    public ItemGlassDoor(Block block) {
        super(block);
        this.doorMaterial = Material.glass;
        this.setCreativeTab(MetropolisCore.tabMetroWorld);
    }

}
