package mod.arrabal.metrocore.common.itemblocks;

import mod.arrabal.metrocore.common.block.BlockCementSlab;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;

/**
 * Created by Arrabal on 1/15/14.
 */
public class ItemBlockCementSlab extends ItemSlab {
    public ItemBlockCementSlab (Block block, BlockCementSlab singleSlab, BlockCementSlab doubleSlab) {
        super(block, singleSlab, doubleSlab);
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta & 7;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        BlockCementSlab slab = (BlockCementSlab)Block.getBlockFromItem(itemStack.getItem());

        //TODO:																		getFullSlabName()
        return super.getUnlocalizedName() + "." + (new StringBuilder()).append(slab.func_150002_b(itemStack.getItemDamage())).toString();
    }
}
