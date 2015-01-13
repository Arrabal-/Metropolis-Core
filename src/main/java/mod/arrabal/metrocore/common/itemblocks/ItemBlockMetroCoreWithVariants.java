package mod.arrabal.metrocore.common.itemblocks;

import mod.arrabal.metrocore.common.block.BlockMetroCore;
import mod.arrabal.metrocore.common.block.BlockMetroCoreSlab;
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
        //need case to account for slabs
        if(this.block instanceof BlockMetroCoreSlab) {
            BlockMetroCoreSlab slab = (BlockMetroCoreSlab)this.block;
            if (slab.hasBaseProperties()){
                return super.getUnlocalizedName() + "." + slab.getStateName(slab.getStateFromMeta(stack.getMetadata()),true);
            }
            else return super.getUnlocalizedName();
        }
        BlockMetroCore block = (BlockMetroCore)this.block;
        if (block.hasBaseProperties()){
            return super.getUnlocalizedName() + "." + block.getStateName(block.getStateFromMeta(stack.getMetadata()), true);
        }
        else return super.getUnlocalizedName();
    }

}
