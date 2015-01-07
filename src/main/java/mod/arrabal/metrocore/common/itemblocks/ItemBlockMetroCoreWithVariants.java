package mod.arrabal.metrocore.common.itemblocks;

import mod.arrabal.metrocore.common.block.BlockMetroCore;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by Arrabal on 1/6/2015.
 */
public class ItemBlockMetroCoreWithVariants extends ItemBlock {

    public ItemBlockMetroCoreWithVariants(Block block){
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta){
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack){
        BlockMetroCore block = (BlockMetroCore)this.block;
        if (block.hasBaseProperties()){
            return super.getUnlocalizedName() + "." + block.getStateName(block.getStateFromMeta(stack.getMetadata()), true);
        }
        else return super.getUnlocalizedName();
    }

}
