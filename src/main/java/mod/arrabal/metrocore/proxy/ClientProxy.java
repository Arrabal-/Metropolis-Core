package mod.arrabal.metrocore.proxy;

import mod.arrabal.metrocore.common.block.BlockMetroCore;
import mod.arrabal.metrocore.common.init.Blocks;
import mod.arrabal.metrocore.common.library.ModRef;
import mod.arrabal.metrocore.common.library.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

/**
 * Created by Arrabal on 12/19/13.
 */
public class ClientProxy extends CommonProxy {

    private static ArrayList<ModelEntry> blocksToRegister = new ArrayList();

    @Override
    public void registerRenderers() {
        for (ModelEntry modelEntry : blocksToRegister){
            ModelHelper.registerBlock(modelEntry.block, modelEntry.meta, ModRef.MOD_ID + "." + modelEntry.name);
            //Item itemBlockVariants = GameRegistry.findItem(ModRef.MOD_ID, modelEntry.block.getUnlocalizedName());
            //ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(ModRef.MOD_ID + ":" + modelEntry.name,"inventory");
            //Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlockVariants,modelEntry.meta, itemModelResourceLocation);
            //ModelHelper.registerItem(itemBlockVariants, modelEntry.meta, ModRef.MOD_ID + ":" + modelEntry.name);
        }
    }

    @Override
    public void registerBlockForMeshing(BlockMetroCore block, int meta, String name){
        blocksToRegister.add(new ModelEntry(block, meta, name));
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
}
