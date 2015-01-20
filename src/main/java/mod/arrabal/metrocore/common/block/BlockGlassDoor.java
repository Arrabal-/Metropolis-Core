package mod.arrabal.metrocore.common.block;

import mod.arrabal.metrocore.common.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Arrabal on 1/16/14.
 */
public class BlockGlassDoor extends BlockMetroCoreDoor {


    public BlockGlassDoor(){
        super(Material.glass);
        this.setHarvestLevel(null, 0);
        this.setStepSound(Block.soundTypeGlass);
        this.setHardness(1.5F);
        this.setResistance(1.0F);
        this.disableStats();
    }

    @Override
    protected Item getItem() {
        return ModItems.itemGlassDoor;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return state.getValue(HALF) == BlockMetroCoreDoor.EnumDoorHalf.UPPER ? null : this.getItem();
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
        super.onBlockHarvested(world, pos, state, player);
        if (this.canSilkHarvest(world, pos, world.getBlockState(pos), player) && EnchantmentHelper.getSilkTouchModifier(player)){
            BlockPos position = pos.down();
            if (state.getValue(HALF) == BlockGlassDoor.EnumDoorHalf.UPPER && world.getBlockState(position).getBlock() == this){
                world.setBlockToAir(position);
            } else{
                /*java.util.ArrayList<ItemStack> items = new java.util.ArrayList<ItemStack>();
                ItemStack itemstack = this.createStackedBlock(state);

                if (itemstack != null)
                {
                    items.add(itemstack);
                }

                net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, world, pos, world.getBlockState(pos), 0, 1.0f, true, player);
                for (ItemStack stack : items)
                {
                    spawnAsEntity(world, pos, stack);
                }*/
            }
        } else {world.setBlockToAir(pos);}
    }
}
