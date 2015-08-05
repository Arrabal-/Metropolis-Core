package mod.arrabal.metrocore.proxy;

import mod.arrabal.metrocore.common.block.BlockMetroCore;
import mod.arrabal.metrocore.common.block.BlockMetroCoreDoor;
import mod.arrabal.metrocore.common.block.BlockMetroCoreSlab;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * Created by Arrabal on 6/26/2014.
 */
public interface IProxy {

    //client methods
    public void registerRenderers();

    public void registerBlockForMeshing(BlockMetroCore block, int meta, String name);

    public void registerBlockForMeshing(BlockMetroCoreSlab block, int meta, String name);

    public void registerItemForMeshing(Item item, int meta, String name);

    public void registerSounds();

    //server methods
    public void addBiomes();

}
