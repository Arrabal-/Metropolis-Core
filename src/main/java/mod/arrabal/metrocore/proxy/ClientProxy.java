package mod.arrabal.metrocore.proxy;

import mod.arrabal.metrocore.common.block.*;
import mod.arrabal.metrocore.common.library.ModRef;
import mod.arrabal.metrocore.common.library.ModelHelper;
import net.minecraft.item.Item;

import java.util.ArrayList;

/**
 * Created by Arrabal on 12/19/13.
 */
public class ClientProxy extends CommonProxy {

    private static ArrayList<ModelEntry> blocksToRegister = new ArrayList();
    private static ArrayList<SlabModelEntry> slabsToRegister = new ArrayList();
    private static ArrayList<ItemModelEntry> itemsToRegister = new ArrayList();

    @Override
    public void registerRenderers() {
        for (ModelEntry modelEntry : blocksToRegister){
            ModelHelper.registerBlock(modelEntry.block, modelEntry.meta, ModRef.MOD_ID + "." + modelEntry.name);
            ModelHelper.registerItem(Item.getItemFromBlock(modelEntry.block), modelEntry.meta, ModRef.MOD_ID + ":" + modelEntry.name);
        }
        for (SlabModelEntry slabModelEntry : slabsToRegister){
            ModelHelper.registerBlock(slabModelEntry.block, slabModelEntry.meta, ModRef.MOD_ID + "." + slabModelEntry.name);
            if (!slabModelEntry.block.isDouble()){
                ModelHelper.registerItem(Item.getItemFromBlock(slabModelEntry.block), slabModelEntry.meta, ModRef.MOD_ID + ":" + slabModelEntry.name);
            }
        }
        for (ItemModelEntry modelEntry : itemsToRegister){
            ModelHelper.registerItem(modelEntry.item, ModRef.MOD_ID + ":" + modelEntry.name);
        }
    }

    @Override
    public void registerBlockForMeshing(BlockMetroCore block, int meta, String name){
        blocksToRegister.add(new ModelEntry(block, meta, name));
    }
    @Override
    public void registerBlockForMeshing(BlockMetroCoreSlab block, int meta, String name){
        slabsToRegister.add(new SlabModelEntry(block, meta, name));
    }

    @Override
    public void registerItemForMeshing(Item item, int meta, String name){
        itemsToRegister.add(new ItemModelEntry(item, meta, name));
    }

    @Override
    public void registerSounds() {
        // This is for initializing the sound system
    }

    private static class ModelEntry {
        public BlockMetroCore block;
        public int meta;
        public String name;

        public ModelEntry(BlockMetroCore block, int meta, String name){
            this.block = block;
            this.meta = meta;
            this.name = name;
        }
    }

    private static class SlabModelEntry {
        public BlockMetroCoreSlab block;
        public int meta;
        public String name;

        public SlabModelEntry(BlockMetroCoreSlab block, int meta, String name){
            this.block = block;
            this.meta = meta;
            this.name = name;
        }
    }

    private static class ItemModelEntry {
        public Item item;
        public int meta;
        public String name;

        public ItemModelEntry(Item item, int meta, String name){
            this.item = item;
            this.meta = meta;
            this.name = name;
        }
    }
}
