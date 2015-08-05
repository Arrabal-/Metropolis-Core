package mod.arrabal.metrocore.proxy;

import mod.arrabal.metrocore.common.block.BlockMetroCore;
import mod.arrabal.metrocore.common.block.BlockMetroCoreSlab;
import mod.arrabal.metrocore.common.init.Biomes;
import mod.arrabal.metrocore.common.library.LogHelper;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;

/**
 * Created by Arrabal on 12/19/13.
 */
public abstract class CommonProxy implements IProxy{

    @Override
    public void addBiomes() {
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(Biomes.plainsMetro, 5));
        BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(Biomes.plainsMetro, 5));
    }
}
