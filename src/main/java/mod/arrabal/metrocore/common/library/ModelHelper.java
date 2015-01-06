package mod.arrabal.metrocore.common.library;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Arrabal on 1/6/2015.
 */

@SideOnly(Side.CLIENT)
public class ModelHelper {

    public static void registerItem(Item item, int meta, String itemName){
        getItemModelMesher().register(item, meta, new ModelResourceLocation(itemName, "inventory"));
    }

    public static void registerBlock(Block block, int meta, String blockName){
        registerItem(Item.getItemFromBlock(block), meta, blockName);
    }

    public static void registerBlock(Block block, String blockName){
        registerBlock(block, 0, blockName);
    }

    public static void registerItem(Item item, String itemName){
        registerItem(item, 0, itemName);
    }

    public static ItemModelMesher getItemModelMesher(){
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
    }

    public static BlockModelShapes getBlockModelShapes(){
        return getItemModelMesher().getModelManager().getBlockModelShapes();
    }
}
