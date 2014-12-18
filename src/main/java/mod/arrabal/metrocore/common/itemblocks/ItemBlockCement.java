package mod.arrabal.metrocore.common.itemblocks;

import mod.arrabal.metrocore.common.block.BlockCement;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by Arrabal on 1/14/14.
 */
public class ItemBlockCement extends ItemBlock {

    public ItemBlockCement (Block block){
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int meta = itemStack.getItemDamage();
        BlockCement cementBlock = (BlockCement)Block.getBlockFromItem(itemStack.getItem());
        return super.getUnlocalizedName() + "." + (new StringBuilder()).append(cementBlock.getBlockTypedName(meta)).toString();
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}