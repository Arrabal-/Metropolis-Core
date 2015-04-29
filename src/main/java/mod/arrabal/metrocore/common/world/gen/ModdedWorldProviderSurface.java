package mod.arrabal.metrocore.common.world.gen;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * Created by Evan on 4/21/2015.
 */
public class ModdedWorldProviderSurface extends WorldProvider {

    @Override
    public String getDimensionName() {
        return "Overworld";
    }

    @Override
    public String getInternalNameSuffix() {
        return "";
    }

    @Override
    public IChunkProvider createChunkGenerator(){

        return new ModdedChunkProviderSurface(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled(), worldObj.getWorldInfo().getGeneratorOptions());
    }

}
